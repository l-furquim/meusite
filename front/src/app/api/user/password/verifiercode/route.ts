import { backendApi } from "@/lib/api";
import { AxiosError } from "axios";
import { NextRequest } from "next/server"
import { BackendLoginErrorResponseType, ChangePasswordResponseType } from "../logged/route";

 type InsertCodeType = {
    code: string,
    newpassword: string
}

export async function POST(request: NextRequest){
    var frontResponse : ChangePasswordResponseType;
    const {code, newpassword} = await request.json();
    const JSONDATA = JSON.stringify({code, newpassword});
    
    
    
    try{
        const data = await backendApi.post("/user/password/verifiercode", JSONDATA);

        const {response} = data.data;

        frontResponse = {response};

    }catch(e){
        const axiosError = e as AxiosError;

        const {status, errormessage}= axiosError.response?.data as BackendLoginErrorResponseType;

        if(status){
            frontResponse = {errormessage};
        }else{
            frontResponse= {errormessage: axiosError.message};
        }
    }

    return new Response(JSON.stringify(frontResponse));


}