'use client'

import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Form, FormField, FormControl, FormItem, FormMessage } from "@/components/ui/form";
import { frontEndApi } from "@/lib/api";
import { removeCookie } from "@/services/cookie/cookies";
import { AxiosError } from "axios";
import { TriangleAlert } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

export function DeleteProfileButton(){
    const [insertMessage, setInsertMessage] = useState(<></>);
    const router = useRouter();
    
    async function onDelete(){
        try{
            const response = await frontEndApi.delete("user/delete");

            const data = response.data;

            if(data){
                const message = <CustomAlert type={CustomAlertType.SUCESS} title="Sucesso! " msg={"conta excluida"}/>
                setInsertMessage(message);
                removeCookie("meusite-token");
                setTimeout(()=> router.push("login"), 15000);
            }else{
                const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao apagar sua conta! " msg="erro desconhecido" />;
                setInsertMessage(message);
            }

        }catch(e){
        const axiosError = e as AxiosError;
        const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao apagar sua conta! " msg={axiosError.message} />;
        setInsertMessage(message);
        }
    }
    
    
    
    
    
    return (
        <>    
    <Dialog>
      <DialogTrigger asChild>
        <Button>Deletar conta</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px] sm:max-h-800">
        <DialogHeader>
          <DialogTitle className="text-red-600 uppercase container flex items-center gap-3 font-bold">AVISO! <TriangleAlert></TriangleAlert>   </DialogTitle>
          <DialogDescription>Esta ação é irreversivel, tem certeza que deseja excluir sua conta?</DialogDescription>
        </DialogHeader>
            <Button onClick={onDelete} type="submit">Apagar</Button>
          {insertMessage}
      </DialogContent>
    </Dialog>
    </>
    )
}