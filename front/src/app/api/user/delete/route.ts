
import { backendApi } from "@/lib/api";
import { removeCookie } from "@/services/cookie/cookies";
import { AxiosError } from "axios";
import { NextRequest } from "next/server";

export async function DELETE(request: NextRequest){
    const cookie = request.cookies.get("meusite-token")?.value
    
    
    
    try{
        const response = await backendApi.delete("user/delete", {
            headers:  {
                "Authorization": `Bearer ${cookie}`
            }
        });

        

        var backResponse;

        if(response.data){
                       

            backResponse = response.data;

        }else{
            backResponse = "erro ao apagar sua conta";
        }
    }
        catch(e){
            const axiosError = e as AxiosError;

            backResponse = axiosError.message;
        }

        return new Response(JSON.stringify(backResponse));

        
    
}