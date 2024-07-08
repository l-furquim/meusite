import { backendApi } from "@/lib/api"
import { AxiosError, CreateAxiosDefaults } from "axios"
import { NextRequest } from "next/server"
import { BackendLoginResponseType } from "../user/login/route"

type BackendPostErrorType = {
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
            return new Response(JSON.stringify(new AxiosError(message, status.toString())), { status });
        } else {
            return new Response(JSON.stringify(new AxiosError(axiosError.message, axiosError.code)),
                { status: axiosError.status || 500 });
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

        const posts = result.data as PostsType;

        

        return new Response(JSON.stringify(posts), {status: 200});



    }catch(e){
        const axiosError = e as AxiosError;

        const { status, message } = axiosError.response?.data as BackendPostErrorType;

        if (status) {
            return new Response(JSON.stringify(new AxiosError(message, status.toString())), { status });
        } else {
            return new Response(JSON.stringify(new AxiosError(axiosError.message, axiosError.code)),
                { status: axiosError.status || 500 });
        }
    }

}