import { backendApi } from "@/lib/api";
import { AxiosError } from "axios";
import { NextRequest, NextResponse } from "next/server";


export type LoginResponseType = {
    token? :  string;
    errormessage? : string;
};

export type BackendLoginResponseType = {
    token: string;
};

export type BackendLoginErrorResponseType = {
    errormessage : string;
    status: number;
    uri: string
};

type BackendEmailResponseType = {
    email: string
};




export async function POST(request: NextRequest){
    const {email, password} = await request.json();

    const data =  JSON.stringify({email , password});

    

    var response: LoginResponseType;

    try{
        const result = await backendApi.post("/user/login", data);
        const { token } = result.data;
        
        response = {token};
    }catch(e){
        const axiosError = e as AxiosError;

        const {status, errormessage}= axiosError.response?.data as BackendLoginErrorResponseType;

        if (status){            
            response = { errormessage }
        }else{
            response = { errormessage : axiosError.message}
        }
    
    }
    
        

        return new Response(JSON.stringify(response))
}

