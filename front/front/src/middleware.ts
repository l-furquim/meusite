import { NextRequest, NextResponse } from "next/server";

export default async function middleware(request: NextRequest){

    const authToken = request.cookies.get("meusite-token")?.value;

    if(authToken){
        const isTokenvalid = await validateToken(authToken);

        if(isTokenvalid)
            return NextResponse.next();
        
    }
        return NextResponse.redirect(new URL("/login", request.url));
    
    

}   

export const config = {
    matcher: ['/home/0']
}

type backendValidateTokenResponseType = {
    is_Valid : boolean
};

type backendValidateTokenRequestType = {
    token: string
}

async function validateToken(token: string){
    var isValid = false;

    try{
        const response = await fetch("http://localhost:8080/user/login/validate", {
            method: "POST",
            headers: {
                "Content-Type" : "application/json",
                'Authorization' : `Bearer ${token}`
            },
            body: JSON.stringify({token} as backendValidateTokenRequestType)
        });

        const jsonResponse = await response.json() as backendValidateTokenResponseType;
        
        isValid = jsonResponse.is_Valid;

    }catch(e){
            isValid = false;
    }

    return isValid;
}