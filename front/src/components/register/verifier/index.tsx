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
import { useRouter } from "next/navigation";

const InsertCodeSchema = z.object({
  code: z.string(),
});

type InsertCodeType = z.infer<typeof InsertCodeSchema>;

export function CodeVerifierRegister() {
  const [insertMessage, setInsertMessage] = useState<JSX.Element>(<></>);
  const router = useRouter()
  
  const insertCode = useForm<InsertCodeType>({
    resolver: zodResolver(InsertCodeSchema),
    defaultValues: {
      code: "",
    }
  });

  async function verifierHandle({ code}: InsertCodeType) {
    const JSONdata = JSON.stringify({ code});

    try {
      const result = await frontEndApi.post("user/login/validate", JSONdata);
      
      
      
      const {response, errormessage} = result.data as ChangePasswordResponseType;

      if (response) {
        setInsertMessage(<CustomAlert title="Sucesso!" msg="conta validada com sucesso, bem vindo!" type={CustomAlertType.SUCESS} />);
        setTimeout(() => router.push("/login"), 1500)
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
        <Button>Validar codigo</Button>
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
            <Button type="submit">Enviar</Button>
            {insertMessage}
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
