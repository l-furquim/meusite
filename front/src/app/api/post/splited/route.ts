import { backendApi } from "@/lib/api";
import { NextRequest } from "next/server";
import { BackendPostErrorType, PostContentType } from "../route";
import axios, { AxiosError } from "axios";
export type CommentContentType = {
    comentId : number,
    content: string,
    userEmail: string,
    postId: number,
    likes: number
}
export type GetPostType = {
    post: PostContentType,
    comments: CommentContentType[]
}


export async function POST(request: NextRequest) {
    const authToken = request.cookies.get("meusite-token")?.value;
    if(!authToken){
    return new Response(JSON.stringify(new Error( "Usuario nao autorizado !")), {status: 401});
    }

    const postId = await request.json();

    try{
        const result = await backendApi.get(`/post/${postId}`, {
            headers: {
                "Authorization": `Bearer ${authToken}`
            }
        });

        const {post, comments} = result.data as GetPostType;


        //const postPage = posts.posts.slice(0, 20);

        return new Response(JSON.stringify({post, comments}), { status: 200 });

    }catch(e){
        console.error("Erro na requisição GET para /api/post:", e);

        if (axios.isAxiosError(e)) {
        const axiosError = e as AxiosError;

        if (axiosError.response) {
            const responseData = axiosError.response.data;

            if (responseData && typeof responseData === "object") {
                const { status, message } = responseData as BackendPostErrorType;

                if (status && message) {
                    return new Response(JSON.stringify({ error: message }), { status });
                }
            }

            return new Response(JSON.stringify({ error: axiosError.message }), { status: axiosError.response.status || 500 });
        }
    }

    return new Response(JSON.stringify({ error: "Erro interno no servidor" }), { status: 500 });
    }
}