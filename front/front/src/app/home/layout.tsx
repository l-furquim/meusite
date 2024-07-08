'use client'

import { AppWindowMac } from "lucide-react"
import { PostForm } from "@/components/post"
import { AuthContext, AuthContextProvider } from "@/context/auth-context"
import { useContext } from "react"
import { frontEndApi } from "@/lib/api"
import { recoveryEmail } from "@/services/user"
import { services } from "@/services"
import { AxiosError } from "axios"
import { Button } from "@/components/ui/button"
import { UserButton } from "@/components/home/button/UserButton"



export default function HomeLayout({children}: {children: React.ReactNode}){
        return (
            <div className="flex flex-col min-h-screen">
    
            <div className="container  p-1 items-center gap-4 bg-zinc-200">
                <span className="flex items-center space-x-3 p-3 mb-2">
                    <AppWindowMac/>
                    <a href="/home">
                    <h1> Pagina inicial </h1>
                    </a>
                   <UserButton></UserButton>
                    <a href="#" className="text-red-400" > Logout </a>
                </span>
            </div>
            
            <div className="container p-5 mt-4 rounded-xl bg-gray-400 min-h-screen">
                {children}
            </div>
    
            </div>
        )
    
}