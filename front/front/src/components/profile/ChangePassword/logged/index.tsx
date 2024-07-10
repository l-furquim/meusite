import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {date, z, ZodSchema} from "zod";
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { Form, FormControl, FormField, FormItem, FormMessage } from "@/components/ui/form";
import { frontEndApi } from "@/lib/api";
import { AxiosError } from "axios";
import { useContext, useState } from "react";
import { useRouter } from "next/navigation";
import { Router } from "lucide-react";
import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { ChangePasswordResponseType } from "@/app/api/user/password/logged/route";


const InsertPasswordChangeSchema = z.object({
    password: z.string(),
    newpassword: z.string()
});

type InsertPasswordChangeType= z.infer<typeof InsertPasswordChangeSchema>;


export  function ChangePasswordLoggedForm() {
    
   
  
  const [insertMessage, setInsertMEssage] = useState<JSX.Element>(<></>);
   
    const insertPost = useForm<InsertPasswordChangeType>({
        resolver: zodResolver(InsertPasswordChangeSchema),
        defaultValues: {
          password: "",
          newpassword: "",
        }
      });


    async function onClickHandle({password, newpassword}: InsertPasswordChangeType)  {
        const JSONdata = JSON.stringify({password, newpassword});
    
    try{

        const result = await frontEndApi.post("user/password/logged", JSONdata);

        const {response, errormessage} = result.data as ChangePasswordResponseType;

        if(response){
          setInsertMEssage(<><CustomAlert title="Sucesso !"msg="senha alterada" type={CustomAlertType.SUCESS} /></>);

          location.reload();

        }else{
          const message = <CustomAlert type={CustomAlertType.ERROR}
          title= "Erro ao alterar a senha:"
          msg = {errormessage || "ERRO DESCONHECIDO!"}/>;
          setInsertMEssage(message);
          }

    }catch(e){
      const axiosError = e as AxiosError;
    const message = <CustomAlert type={CustomAlertType.ERROR}
    title= "Erro ao logar-se !"
    msg = {axiosError.message}/>;
    setInsertMEssage(message);

    }

   }
    
    
    return (
        <Dialog>
      <DialogTrigger asChild>
        <Button>Mudar Senha</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px] sm:max-h-800">
        <DialogHeader>
          <DialogTitle>Mudando sua senha</DialogTitle>
          <DialogDescription>
            insira sua senha atual e sua nova senhha
          </DialogDescription>
        </DialogHeader>
        <div>
        <Form {...insertPost}>
           <form onSubmit={insertPost.handleSubmit(onClickHandle)}>
        <FormField
          control={insertPost.control}
          name="password"
          render = {({ field }) => {
            return (
              <FormItem>
                <FormControl>
                <div className="grid grid-cols-4 items-center gap-4">
                    Sua senha:
                  <Input
                    className="col-span-3"
                    type="text" {...field}/> 
                </div>
                </FormControl>
              </FormItem>
            )
          }
        }
        /><FormField
        control={insertPost.control}
        name="newpassword"
        render = {({ field }) => {
          return (
            <FormItem>
              <FormControl>
              <div className="grid grid-cols-4 items-center gap-4">
                  nova senha: 
                <Input
                  className="col-span-3"
                  type="text" {...field}/> 
              </div>
              </FormControl>
            </FormItem>
          )
        }
      }
      />
        
          <Button type="submit">Mudar</Button>
          </form>
          {insertMessage}
      </Form>
      </div>
      </DialogContent>
    </Dialog>
    );
}