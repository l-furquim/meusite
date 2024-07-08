import { backendApi } from "@/lib/api";
import { NextRequest } from "next/server";
import { Axios, AxiosError } from "axios";
import { BackendLoginErrorResponseType } from "../login/route";



export type recoveryUserEmailByTokenRequestType = {
    token: string,
}

export type recoveryUserEmailByTokenResponseType = {
    email?: string,
    message?: string
}


export async function GET(request: NextRequest){

    const token = request.cookies.get("meusite-token")?.value;

    

    var response: recoveryUserEmailByTokenResponseType;

    

    try{

        const request = await backendApi.get("/user/recoveryEmail", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        const email = request.data as  recoveryUserEmailByTokenResponseType;

        
        
        
        return new Response(JSON.stringify(email), {status: 200});
        }catch(e){
            const axiosError = e as AxiosError;

            const {message} = axiosError;

            response  = {message};
        
            return new Response(JSON.stringify(response), {status: 500});
        }

        
    
}