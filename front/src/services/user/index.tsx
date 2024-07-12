'use client'

import { AuthContext } from "@/context/auth-context";
import { backendApi, frontEndApi } from "@/lib/api";
import { AxiosError } from "axios";
import { useContext } from "react";

export type getUserEmailType = {
    token: string
}

export type getUserEmailResponseType = {
    email: string
}



export async function recoveryEmail(){
      
    try{
    const userData = await frontEndApi.get("/user/email");  

    const userEmail = userData.data as getUserEmailResponseType;

    
    return userEmail;
    }catch(e){
        const error = e as AxiosError;
        throw error;
    }
}