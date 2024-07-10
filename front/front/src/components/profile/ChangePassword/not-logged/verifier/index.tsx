import React, { useState } from "react";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Form, FormField, FormControl, FormItem } from "@/components/ui/form";
import { frontEndApi } from "@/lib/api";
import { AxiosError } from "axios";
import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { ChangePasswordResponseType } from "@/app/api/user/password/logged/route";

const InsertCodeSchema = z.object({
  code: z.string(),
  newpassword: z.string(),
});

type InsertCodeType = z.infer<typeof InsertCodeSchema>;

interface CodeVerifierProps {
  onClose: () => void; // Função para fechar o diálogo
}

export function CodeVerifier({ onClose }: CodeVerifierProps) {
  const [insertMessage, setInsertMessage] = useState<JSX.Element>(<></>);
  const insertCode = useForm<InsertCodeType>({
    resolver: zodResolver(InsertCodeSchema),
    defaultValues: {
      code: "",
      newpassword: "",
    }
  });

  async function verifierHandle({ code, newpassword }: InsertCodeType) {
    const JSONdata = JSON.stringify({ code, newpassword });

    try {
      const result = await frontEndApi.post("user/password/verifiercode", JSONdata);
      
      console.log(result.data);
      
      const {response, errormessage} = result.data as ChangePasswordResponseType;

      if (response) {
        setInsertMessage(<CustomAlert title="Sucesso!" msg="Senha alterada com sucesso" type={CustomAlertType.SUCESS} />);
        setTimeout(() => location.reload(), 1500)
      } else {
        const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao alterar a senha:" msg={errormessage || "ERRO DESCONHECIDO!"} />;
        setInsertMessage(message);
      }

    } catch (e) {
      const axiosError = e as AxiosError;
      const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao verificar código!" msg={axiosError.message} />;
      setInsertMessage(message);
    }
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button>Enviar codigo</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px] sm:max-h-800">
        <DialogHeader>
          <DialogTitle>Verificação</DialogTitle>
          <DialogDescription>Insira o código enviado para seu email</DialogDescription>
        </DialogHeader>
        <Form {...insertCode}>
          <form onSubmit={insertCode.handleSubmit(verifierHandle)}>
            <FormField
              control={insertCode.control}
              name="code"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      Código:
                      <Input className="col-span-3" type="text" {...field} />
                    </div>
                  </FormControl>
                </FormItem>
              )}
            />
            <FormField
              control={insertCode.control}
              name="newpassword"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <div className="grid grid-cols-4 items-center gap-4">
                      Sua nova senha:
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
      </DialogContent>
    </Dialog>
  );
}
