import { NextRequest } from "next/server";



export function DELETE(request: NextRequest){
    try{    
        const  cookie = request.cookies.get("meusite-token")?.value;

        var response;
        if(cookie){

            
            request.cookies.delete("meusite-token");

            response = "logout realizado com sucesso"
        }else{
            response = "login falhou !, nao possui cookies";
        }

        return new Response(JSON.stringify(response));
    }catch(e){

    }
}