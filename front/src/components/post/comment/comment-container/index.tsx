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
import { ChangePasswordResponseType } from "@/app/api/user/password/logged/route";
import { MessageSquareDot } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"



interface CommentContainerProps {
  ncomments: number,
  postId: number
}


const insertCommentSchema = z.object({
    content : z.string(),
  });
  
  type InsertCommentType = z.infer<typeof insertCommentSchema>;


  type CommentFormProps = {
    postId: number;
};

const CommentContainer: React.FC<CommentContainerProps> = ({ ncomments, postId}) => {
  const [insertMessage, setInsertMessage] = useState(<></>);
  const [nComments, setNComments] = useState(ncomments);

  const insertComment = useForm<InsertCommentType>({
    resolver: zodResolver(insertCommentSchema),
    defaultValues: {
           content: "",
         }
       });
   async function handleComment({content}: InsertCommentType){
   
   const dataJSON = JSON.stringify({content, postId});

    

   try{

    const result = await frontEndApi.post("/post/comment", dataJSON);

    setNComments(nComments +1);

     const response = result.data;

    console.log(dataJSON)

    const message = <CustomAlert type={CustomAlertType.SUCESS} title="Sucesso!" msg={response} />;

     setInsertMessage(message);

     setTimeout(() => location.reload(), 1000);
   } catch (e) {
     const axiosError = e as AxiosError;
     const message = <CustomAlert type={CustomAlertType.ERROR} title="Erro ao verificar código!" msg={axiosError.message} />;
     setInsertMessage(message);
   }
   }


   
   return (
    <>
      <Dialog>
      <DialogTrigger asChild>
      <p>
      <MessageSquareDot 
      size={16} className="hover:cursor-pointer hover:stroke-white"
      />
      {nComments}</p>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500]">
        <DialogHeader>
          <DialogTitle>Fazer um comentario</DialogTitle>
          <DialogDescription>
            Escreva oque deseja comentar !
          </DialogDescription>
        </DialogHeader>
       <Form {...insertComment}>
          <form onSubmit={insertComment.handleSubmit(handleComment)}>
       <FormField
         control={insertComment.control}
         name="content"
         render = {({ field }) => {
           return (
             <FormItem>
               <FormControl>
               <div className="grid grid-cols-4 items-center gap-10">
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
     </DialogContent>
     </Dialog>
     </>
   )
};

export default CommentContainer;
