import { backendApi } from "@/lib/api";
import { NextRequest } from "next/server";

export async function POST(request: NextRequest){
    const cookie = request.cookies.get("meusite-token")?.value;

    try{

        const response = await backendApi.post("/comment/{")

    }catch(e){

    }

}