import { backendApi } from "@/lib/api";
import { NextRequest } from "next/server";

export type insertCommentType = {
    content: string,
    postId: number
}

export async function POST(request: NextRequest){
    const cookie = request.cookies.get("meusite-token")?.value;

    const {content, postId} = await request.json() as insertCommentType
    try{

        const headers = {
            headers: {
                "Authorization": `Bearer ${cookie}`
            }
        };


        const dataJSON = JSON.stringify({content});

        const response = await backendApi.post(`/comments/${postId}`, dataJSON,
            headers
        );
            return new Response(response.data, {status: 200});
    }catch(error){
        throw error;
    }

}