'use client'

import { Button } from "@/components/ui/button";
import { frontEndApi } from "@/lib/api";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";
    
export async function UserButton () {
    const router  = useRouter();
    
    const handleClick = async () => {
    
    try{
        const data = await frontEndApi.get("user/email");          
        
        const {email} = data.data;

        router.push(`profile/${email}`)
        
    }catch(e){
        const axiosError = e as AxiosError;
        console.log(axiosError.message);
    } 

    };
    
    return (
        <Button className="text-sky-600 bg-zinc-200" onClick={handleClick}>
            User
        </Button>
     )
    }