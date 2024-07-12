'use client'


import React, { useState } from "react";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormField, FormControl, FormItem, FormMessage } from "@/components/ui/form";
import { frontEndApi } from "@/lib/api";
import { AxiosError } from "axios";
import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { ChangePasswordResponseType } from "@/app/api/user/password/logged/route";
import { CodeVerifierPassword } from "./verifier"; // Importe o componente CodeVerifier
import Modal from "react-modal";



const InsertPasswordChangeSchema = z.object({
  email: z.string().email({message: "isso nao é um email valido!"}), // Adicione validação de e-mail, se necessário
});

type InsertPasswordChangeType = z.infer<typeof InsertPasswordChangeSchema>;

export function ChangePasswordNotLoggedForm() {
  const [insertMessage, setInsertMessage] = useState<JSX.Element>(<></>);
  const [showCodeVerifier, setShowCodeVerifier] = useState(false); // Estado para controlar a exibição do CodeVerifier
  
  const insertEmail = useForm<InsertPasswordChangeType>({
    resolver: zodResolver(InsertPasswordChangeSchema),
    defaultValues: {
      email: ""
    }
  });

  async function onClickHandle({ email }: InsertPasswordChangeType) {
   

    const JSONdata = JSON.stringify({ email });

   

    try {
      const result = await frontEndApi.post("user/password/not-logged", JSONdata);
      const { response, errormessage } = result.data as ChangePasswordResponseType;

      if (response) {
        setInsertMessage(<CustomAlert title="Sucesso!" msg="E-mail enviado" type={CustomAlertType.SUCESS} />);
        setShowCodeVerifier(true); // Mostra o componente CodeVerifier automaticamente
      } else {
        const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao alterar a senha:" msg={errormessage || "ERRO DESCONHECIDO!"} />;
        setInsertMessage(message);
      }

    } catch (e) {
      const axiosError = e as AxiosError;
      const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao logar-se!" msg={axiosError.message} />;
      setInsertMessage(message);
    }
  }

  return (
    <>    
    <Dialog>
      <DialogTrigger asChild>
        <Button>Esqueci minha senha</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px] sm:max-h-800">
        <DialogHeader>
          <DialogTitle>Mudando sua senha</DialogTitle>
          <DialogDescription>Insira o email que deseja trocar a senha</DialogDescription>
        </DialogHeader>
        <Form {...insertEmail}>
          <form onSubmit={insertEmail.handleSubmit(onClickHandle)}>
            <FormField
              control={insertEmail.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      Email:
                      <Input className="col-span-3" type="text" {...field} />
                    </div>
                  </FormControl>
                </FormItem>
              )}
            />
            <Button type="submit">Enviar</Button>
          {insertMessage}
          </form>
        </Form>
        {showCodeVerifier && <CodeVerifierPassword />}
      </DialogContent>
    </Dialog>
    </>

  );
}
