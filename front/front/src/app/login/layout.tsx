'use client'
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { AppWindowMac } from "lucide-react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { useContext} from "react";
import {zodResolver} from "@hookform/resolvers/zod"
import { backendApi, frontEndApi } from "@/lib/api";
import { LoginResponseType } from "../api/user/login/route";
import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { AxiosError } from "axios";
import { AuthContext } from "@/context/auth-context";
import { useRouter } from "next/navigation";
import React, { useState } from 'react';
import { Redirect } from 'react-router-dom';
import { ChangePasswordNotLoggedForm } from "@/components/profile/ChangePassword/not-logged";
import { CodeVerifier } from "@/components/profile/ChangePassword/not-logged/verifier";

const LoginFormSchema = z.object({
    email: z.string().email({message: "Email invalido !"}),
    password: z.string().min(1, "Senha invalida !")
});

type loginFormType = z.infer<typeof LoginFormSchema>;


export default function LoginLayout() {

    const [message, setMessage] = useState(<></>);

    const authContex = useContext(AuthContext);

    const router = useRouter();

    const loginForm = useForm<loginFormType>({
        resolver: zodResolver(LoginFormSchema),
        defaultValues: {
            email: "",
            password: ""
        }
    });

async function handleLoginSubmit({email, password}: loginFormType){
    const data = JSON.stringify({
        email,
        password
    });
try{
    const  result =   await frontEndApi.post("/user/login", data)

    const {token, errormessage}= result.data as LoginResponseType;

    if(token){
        authContex.signIn(token);

        const message = <CustomAlert type={CustomAlertType.SUCESS}
        title= "Login efetuado com sucesso , "
        msg = {"bem vindo !"}/>;
        setMessage(message);
        
        

        setTimeout(() => router.push("/home/0"), 1000);
    
    }else{
    const message = <CustomAlert type={CustomAlertType.ERROR}
    title= "Erro ao logar-se !"
    msg = {errormessage || "ERRO DESCONHECIDO!"}/>;
    setMessage(message);
    }
    
    

}catch(e){
    const axiosError = e as AxiosError;
    const message = <CustomAlert type={CustomAlertType.ERROR}
    title= "Erro ao logar-se !"
    msg = {axiosError.message}/>;
    setMessage(message);
}



}

    


    return(
        <>
        
    <div className="flex pb-60 items-center h-screen">

        <div className="container space-y-4 p-8 rounded-xl max-w-md  bg-slate-200">

            <span className="flex items-center gap-2">
                <h1  className="uppercase font-bold">Meu site </h1>
                <AppWindowMac className="text-blue-400" size={27}/>
            </span>

       <Form {...loginForm}>
        <form onSubmit={loginForm.handleSubmit(handleLoginSubmit)}>
            {message}
            <FormField
            control={loginForm.control}
            name="email"
            render={({ field })=> {return (
                <FormItem>
                    <FormControl>
                    <Input type="text" {...field}/>
                    </FormControl>
                    <FormMessage/>
                </FormItem>
            )}}/>
        <FormField control={loginForm.control}
        name="password"
        render={({ field }) => {return (
            <FormItem>
                <FormControl>
                <Input type="password" {...field}/>
                </FormControl>
                <FormMessage/>
            </FormItem>
        )}}/>
            
       
       <Button type="submit">Enviar</Button>
       
       
            </form>
        </Form>
        <CodeVerifier onClose={function (): void {
                        throw new Error("Function not implemented.");
                    } }></CodeVerifier>
    </div>
</div>
        
    </>
    
    )


}