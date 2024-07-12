import { backendApi } from "@/lib/api";
import { AxiosError } from "axios";
import { NextRequest } from "next/server";
import { BackendLoginErrorResponseType } from "../login/route";

export type BackEndRegisterResponseType =  {
    sucessmessage: string,
    errormessage: string,
}


export async function POST(request: NextRequest) {
    try{
        var frontResponse;

        const data = await request.json()

        const JsonData = JSON.stringify(data);

        const response = await backendApi.post("user/register", JsonData);

        const {sucessmessage, errormessage}  = response.data as BackEndRegisterResponseType;

        if(sucessmessage){
            frontResponse = {sucessmessage};
        }else{
            frontResponse = {errormessage}; 
        }

        



    }catch(e){
        const axiosError = e as AxiosError;

        const {status, errormessage}= axiosError.response?.data as BackendLoginErrorResponseType;

        if (status){            
            frontResponse = { errormessage }
        }else{
            frontResponse = { errormessage : axiosError.message}
        }
    
    }

    return new Response(JSON.stringify(frontResponse));
}