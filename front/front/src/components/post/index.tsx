'use client'

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
import { CustomAlert, CustomAlertType } from "../general/customalert";





const insertPostSchema = z.object({
  content : z.string(),
});

type InsertPostType = z.infer<typeof insertPostSchema>;


export function PostForm(){
  

  const [insertMessage, setInsertMEssage] = useState<JSX.Element>(<></>);
  
  const insertPost = useForm<InsertPostType>({
    resolver: zodResolver(insertPostSchema),
    defaultValues: {
      content: "",
    }
  });
  
async function onInsertPost({content}: InsertPostType){
    const formatedData = JSON.stringify({content});
    
    try{
        const result = await frontEndApi.post("/post", formatedData);

        console.log("Inserido com sucesso");
        setInsertMEssage(<><CustomAlert title="Sucesso !"msg="post criado" type={CustomAlertType.SUCESS} /></>);
        location.reload();
        

    }catch(e){
        const axiosError = e as AxiosError;

        console.log(axiosError.message, axiosError.response?.status);

        setInsertMEssage(<>deu ruim!</>);

        
    }
  }
  


  return(
        <Dialog>
      <DialogTrigger asChild>
        <Button variant="outline">Fazer um post</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Fazer um post</DialogTitle>
          <DialogDescription>
            Escreva oque deseja postar !
          </DialogDescription>
        </DialogHeader>
        <div>
        <Form {...insertPost}>
           <form onSubmit={insertPost.handleSubmit(onInsertPost)}>
        <FormField
          control={insertPost.control}
          name="content"
          render = {({ field }) => {
            return (
              <FormItem>
                <FormControl>
                <div className="grid grid-cols-4 items-center gap-4">
                    Descricao
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
        
          <Button type="submit">Postar</Button>
          </form>
          {insertMessage}
      </Form>
      </div>
      </DialogContent>
    </Dialog>
    );
}