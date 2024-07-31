import { backendApi } from "@/lib/api";
import axios, { AxiosError } from "axios";
import { NextRequest } from "next/server";
import { BackendPostErrorType } from "../../post/route";

export async function POST(request: NextRequest) {

    const cookies = request.cookies.get("meusite-token")?.value;

    try{    

        const commentId = await request.json();

        console.log(commentId.data ," PORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRrrr");

        await backendApi.post(`/comments/${commentId}/like`, JSON.stringify(commentId), {
            headers: {
                "Authorization": `Bearer ${cookies}`
            }
        }
    
    );
    return new Response("", {status: 200});    
    }catch(e){
        
        if (axios.isAxiosError(e)) {
            const axiosError = e as AxiosError;
            console.log(axiosError);
        
        }  
    }
}