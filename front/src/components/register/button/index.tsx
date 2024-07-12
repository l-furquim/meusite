import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";

export function RegisterButton() {
    const router = useRouter();
    
    function onClickHandle(){
        router.push("/register")
    }
    
    
    
    return(
        <Button onClick={onClickHandle}>Registrar</Button>
    )
}