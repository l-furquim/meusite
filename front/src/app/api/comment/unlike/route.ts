import { backendApi } from "@/lib/api";
import axios, { AxiosError } from "axios";
import { NextRequest } from "next/server";

export async function POST(request: NextRequest){
    const cookies = request.cookies.get("meusite-token")?.value;

    var frontResponse;

    try{    

        const commentId = await request.json();

        console.log(commentId);

        const response = await backendApi.delete(`/comments/${commentId}/unlike`, {
            headers: {
                "Authorization": `Bearer ${cookies}`
            }
        });

        if(response.data){
            frontResponse = response.data;
        }
        
    }catch(e){
        
        if (axios.isAxiosError(e)) {
            const axiosError = e as AxiosError;
        }  
                frontResponse = e
               }
        return new Response(frontResponse, {status: 200});
}