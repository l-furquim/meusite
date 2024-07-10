import { backendApi } from "@/lib/api";
import { AxiosError } from "axios";
import { NextRequest } from "next/server";

export type ChangePasswordResponseType = {
    errormessage?: string,
    response?: string,
    
};


export type BackendLoginErrorResponseType = {
    errormessage : string;
    status: number;
    uri: string
};

export async function POST(request: NextRequest){
    const cookie = request.cookies.get("meusite-token")?.value;

    const {password, newpassword} = await request.json();

    const data = JSON.stringify({password,newpassword});

    var responseFront: ChangePasswordResponseType;

    

    try{

        const result = await backendApi.post("user/changepassword/logged",data, {
            headers: {
                "Authorization": `Bearer ${cookie}`
            }
        });

        

        const {response} = result.data;
    
        responseFront = {response};

    }catch(e){
        const axiosError = e as AxiosError;

        const {status, errormessage}= axiosError.response?.data as BackendLoginErrorResponseType;

        if(status){
            responseFront = {errormessage};
        }else{
            responseFront= {errormessage: axiosError.message};
        }
    }

    

    return new Response(JSON.stringify(responseFront));
}