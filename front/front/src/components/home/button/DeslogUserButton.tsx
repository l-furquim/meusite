'use client'

import { Button } from "@/components/ui/button"
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
  } from "@/components/ui/alert-dialog"
import { ComponentProps, JSXElementConstructor, useState } from "react"
import { removeCookie } from "@/services/cookie/cookies"
import { useRouter } from "next/navigation"
import { CustomAlert, CustomAlertType } from "@/components/general/customalert"
    
export type LogOutResponseType = {
    sucessmessage?: string,
    errormessage?: string
}

export default function DeslogButton() {
    const [message , setMessage] = useState(<></>)

    const router = useRouter();
    const onClick =() => {
        try {
            removeCookie("meusite-token");

            setMessage(<CustomAlert type={CustomAlertType.SUCESS} msg="logout feito!" title="Sucesso, "/>);

            setTimeout(() => router.push("/login"), 2000);
        } catch (error) {
            console.error("Erro ao tentar fazer logout:", error);
        }

    }
    return(
        <AlertDialog>
      <AlertDialogTrigger asChild>
        <Button className="text-red-600 bg-zinc-200">Logout</Button>
      </AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Tem certeza que deseja deslogar? </AlertDialogTitle>
          <AlertDialogDescription>
            verifique se possui a senha para login de volta!

            {message}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancelar</AlertDialogCancel>
          <AlertDialogAction onClick={onClick} className="text-red-600">Deslogar</AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
     )

    }
