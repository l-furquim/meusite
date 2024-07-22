import { ChangePasswordResponseType } from "@/app/api/user/password/logged/route";
import { CustomAlert, CustomAlertType } from "@/components/general/customalert";
import { Button } from "@/components/ui/button"
import { Form, FormControl, FormField, FormItem } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { frontEndApi } from "@/lib/api";
import { zodResolver } from "@hookform/resolvers/zod";
import { AxiosError } from "axios";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";



const insertCommentSchema = z.object({
    content : z.string(),
  });
  
  type InsertCommentType = z.infer<typeof insertCommentSchema>;


  type CommentFormProps = {
    postId: number;
};


export const CommentForm: React.FC<CommentFormProps> = ({ postId }) => {

  const [insertMessage, setInsertMessage] = useState(<></>);

  const insertComment = useForm<InsertCommentType>({
     resolver: zodResolver(insertCommentSchema),
     defaultValues: {
            content: "",
          }
        });
    async function handleComment({content}: InsertCommentType){
    
    const commentJson = JSON.stringify({content});

    try{

    const result = await frontEndApi.post("user/post/comment", commentJson);
      
      
      
      const {response, errormessage} = result.data as ChangePasswordResponseType;

      if (response) {
        location.reload();
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
        <Form {...insertComment}>
           <form onSubmit={insertComment.handleSubmit(handleComment)}>
        <FormField
          control={insertComment.control}
          name="content"
          render = {({ field }) => {
            return (
              <FormItem>
                <FormControl>
                <div className="grid grid-cols-4 items-center gap-4">
                    Comentario: 
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
        {insertMessage}
          <Button type="submit">Comentar</Button>
          </form>
      </Form>
    )
}