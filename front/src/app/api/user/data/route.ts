import { backendApi } from "@/lib/api";
import { NextRequest } from "next/server"
import { recoveryUserEmailByTokenRequestType } from "../email/route";
import { AxiosError } from "axios";

export type GetUserDataResponseType = {
    email: string,
    password: string,
    created_at : string
}

export type GetUserDataRequestType = {
    email: string
}

export async function GET(request: NextRequest){
    const cookie = request.cookies.get("meusite-token")?.value;

    

        try{
            const data = await backendApi.get(`/user/getData/${cookie}`,{
                headers: {
                "Authorization": `Bearer ${cookie}`
            } 
    })
        
        const userData = data.data as GetUserDataResponseType;

        return new Response(JSON.stringify(userData), {status: 200});
        
    
    }catch(e){
            const axiosError = e as AxiosError;

            return new Response(JSON.stringify(axiosError.message), {status: 500})
        }
    }
    


