import { backendApi } from "@/lib/api";
import { NextRequest } from "next/server";

export async function POST(request: NextRequest){
    const authToken = request.cookies.get("meusite-token")?.value
    const id = await request.json()
    
    try{


        await backendApi.delete(`/likes/unlike/${id}`, {
            headers: {
                "Authorization": `Bearer ${authToken}`
            }
        });
        return new Response("true");
    }catch(error){
        throw error
    }
}