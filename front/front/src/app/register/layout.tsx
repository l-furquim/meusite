"use client"

import React, { useState } from 'react';
import UploadButton from '@/components/file/upload-file-button';
import { Button } from '@/components/ui/button';
import { Input } from "@/components/ui/input";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { frontEndApi } from "@/lib/api";
import { AppWindowMac } from "lucide-react";
import { useRouter } from "next/navigation";
import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { AxiosError } from "axios";
import { BackEndRegisterResponseType } from '../api/user/register/route';

const RegisterFormSchema = z.object({
    email: z.string().email({ message: "Email inválido!" }),
    password: z.string().min(1, "Senha inválida!"),
    confirmPassword: z.string(),
    icon: z.unknown(), // Manter como z.unknown() para não validar o tipo de 'icon'
});
    


type RegisterFormType = z.infer<typeof RegisterFormSchema>;

export default function RegisterLayout() {
    const [message, setMessage] = useState<React.ReactNode>(<></>);
    const router = useRouter();
    const { handleSubmit, register, setValue } = useForm<RegisterFormType>({
        resolver: zodResolver(RegisterFormSchema),
        defaultValues: {
            email: "",
            password: "",
            confirmPassword: "",
            icon: null,
        }
    });

    const handleFileSelect = (file: File) => {
        setValue("icon", file); // Define o valor do campo 'icon' no estado do formulário
        
    };

    const handleRegisterSubmit = async (data: RegisterFormType) => {
        if(data.password !== data.confirmPassword){
            setMessage( <CustomAlert type={CustomAlertType.ERROR} msg='as senhas nao conhecidem !' title='Erro: '/>);
        }else{
        const {email, password } = data;

        
        
        const JsonData = JSON.stringify({email, password});
        
        try {

            const response = await frontEndApi.post("/user/register", JsonData);
            const { sucessmessage, errormessage } = response.data as BackEndRegisterResponseType;

            if (sucessmessage) {
                setMessage(<CustomAlert msg={sucessmessage} title="Registro realizado com sucesso" type={CustomAlertType.SUCESS} />);
                setTimeout(() => router.push("/login"), 1000);
            } else {
                setMessage(<CustomAlert msg={errormessage || "Erro desconhecido"} title="Erro" type={CustomAlertType.ERROR} />);
            }
        } catch (error) {
            const axiosError = error as AxiosError;
            setMessage(<CustomAlert type={CustomAlertType.ERROR} title="Erro ao registrar!" msg={axiosError.message} />);
        }
    }
    };

    return (
        <div className="flex pb-60 items-center h-screen">
            <div className="container space-y-4 p-8 rounded-xl max-w-md mt-40 bg-slate-200">
            <span className='flex items-center gap-2'>
                <h1 className="uppercase font-bold">Meu site</h1>
                <AppWindowMac className='text-blue-400 ' size={27}/>
                </span>

                <form onSubmit={handleSubmit(handleRegisterSubmit)}>
                    {message}

                    <div className="mb-4 shadow-sm " >
                        <label>Email</label>
                        <Input {...register("email")} type="text" className="form-input mt-1 block w-full" />
                    </div>

                    <div className="mb-4 shadow-sm">
                        <label>Senha</label>
                        <Input {...register("password")} type="password" className="form-input mt-1 block w-full" />
                    </div>

                    <div className="mb-4 shadow-sm">
                        <label>Confirmar Senha</label>
                        <Input {...register("confirmPassword")} type="password" className="form-input mt-1 block w-full" />
                    </div>

                    <div className="mb-4 shadow-sm" >
                        <label>Ícone</label>
                        <Input type='file' className=''/>
                        {/* Input hidden apenas necessário se o upload for controlado manualmente */}
                        {/* <input type="hidden" {...register("icon")} /> */}
                    </div>

                    <Button type="submit">Enviar</Button>
                </form>
            </div>
        </div>
    );
}