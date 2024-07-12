import { backendApi } from "@/lib/api";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";
import { BackendLoginErrorResponseType, LoginResponseType } from "../route";


type backEndLoginValidateResponseType  = {
    response?: string,
    errormessage?: string
}


export async function POST(request: NextRequest){
    const {code} = await request.json();

    const data =  JSON.stringify({code});

    var responseFront: backEndLoginValidateResponseType;

    try{
        const result = await backendApi.post("/user/register/validate", data);
        const {response} = result.data;
        
        responseFront = {response};
    }catch(e){
        const axiosError = e as AxiosError;

        const {status, errormessage}= axiosError.response?.data as BackendLoginErrorResponseType;

        if (status){            
            responseFront = { errormessage }
        }else{
            responseFront = { errormessage : axiosError.message}
        }
    
    }
    
        

        return new Response(JSON.stringify(responseFront))
}

