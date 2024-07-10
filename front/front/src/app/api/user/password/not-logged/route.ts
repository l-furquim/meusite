import { NextRequest } from "next/server";
import { BackendLoginErrorResponseType, ChangePasswordResponseType } from "../logged/route";
import { AxiosError } from "axios";
import { backendApi } from "@/lib/api";

export async function POST(request: NextRequest){
    
    const {email} = await request.json();



    const data = JSON.stringify({email});

    var responseFront: ChangePasswordResponseType;

    try{

        const result = await backendApi.post("/user/changepassword/notlogged",data
        );


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