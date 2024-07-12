import { backendApi } from "@/lib/api"
import axios, { AxiosError, CreateAxiosDefaults } from "axios"
import { NextRequest } from "next/server"
import { BackendLoginResponseType } from "../user/login/route"

export type BackendPostErrorType = {
    message : string,
    status: number,
    uri : string
}

type CreatePostRequestType = {
    content: string
}

type FrontResponsePostType = {
    content: string,
    userEmail : string
}


export type  PostContentType = {
    content: string,
    userEmail: string,
    postedAt: string
}

export type PostsType = {
    posts: PostContentType[];
}




export async function POST(request: NextRequest) {
    const authToken = request.cookies.get("meusite-token")?.value;

    if(!authToken)
        return new Response(JSON.stringify(new Error( "Usuario nao autorizado !")), {status: 401});

    try{
        
       const data = await request.json() as CreatePostRequestType;

        

        const jsonData = JSON.stringify(data);

        

        const result = await backendApi.post("/post", jsonData, {
                headers: {
                    "Authorization": `Bearer ${authToken}`
                } 
        }) as BackendLoginResponseType;

        return new Response("", {status: 200});

    }catch (e){ 
        const axiosError = e as AxiosError;

        const { status, message } = axiosError.response?.data as BackendPostErrorType;

        if (status) {
            return new Response(JSON.stringify(new Error(message)), { status });
        } else {
            return new Response(JSON.stringify(new Error(axiosError.message)), { status: axiosError.response?.status || 500 });
        }
    }
}

export async function GET(request: NextRequest) {
    const authToken = request.cookies.get("meusite-token")?.value;
    if(!authToken){
    return new Response(JSON.stringify(new Error( "Usuario nao autorizado !")), {status: 401});
    }

    try{
        const result = await backendApi.get("/post/feed", {
            headers: {
                "Authorization": `Bearer ${authToken}`
            }
        });

        const posts = result.data


        //const postPage = posts.posts.slice(0, 20);

        return new Response(JSON.stringify(posts.slice(0,20)), { status: 200 });

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