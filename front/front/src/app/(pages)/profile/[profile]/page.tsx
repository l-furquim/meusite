"use client"
import HomeLayout from "@/app/home/layout";
import { GetUSerData, ProfileDataRequestType, ProfileDataType} from "@/components/profile";
import { ChangePasswordForm } from "@/components/profile/button/password-button";


export default async function ProfilePage({
    params,

}: {
    params: {profile: string};
}) {
    const { profile } = params;

    
    
    return(
        
            
          <HomeLayout>  
                <div className="container flex-col flex space-y-10  p-10 items-center min-h-screen">
                    <h1>Seu perfil !</h1>
                    
                    <GetUSerData/>
                </div> 
          </HomeLayout>  
            
        

    )

}




