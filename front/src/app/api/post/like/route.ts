
import { backendApi } from "@/lib/api";
import { AxiosError } from "axios";
import { NextRequest } from "next/server";
import { BackendLoginErrorResponseType } from "../../user/login/route";

export type ListLikeContentType = {
   likes: LikeContentType[]
}   
export interface LikeContentType {
    likeId: number;
    userId: number | null; // Ajuste o tipo conforme necessário
    postId: number;
    commentId: number;
}


export async function POST(request: NextRequest){
    
    
    const authToken = request.cookies.get("meusite-token")?.value


    const id = await request.json()

    

    const headers = {
        headers: {
            "Authorization": `Bearer ${authToken}`
        }
    };


    try{
        
       const response =  await backendApi.post(`/likes/like/${id}`,{},headers)
        return new Response("true");
    }catch(error){
        throw error;
        
    }
}

export async function GET(request: NextRequest){

    const authToken = request.cookies.get("meusite-token")?.value

    const headers = {
        headers: {
            "Authorization": `Bearer ${authToken}`
        }
    };
    var frontResponse;
    try{
        const response =  await backendApi.get(`/likes/${authToken}`,headers)
         
        const likeList = response.data;

       

        frontResponse = JSON.stringify(likeList), {status: 200};
     }catch(e){
        const axiosError = e as AxiosError;

        const {status, errormessage}= axiosError.response?.data as BackendLoginErrorResponseType;

        if (status){            
            frontResponse = { errormessage }
        }else{
            frontResponse = { errormessage : axiosError.message}
        }
     }
     return new Response(JSON.stringify(frontResponse));
 
}

