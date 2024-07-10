"use client"

import HomeLayout from "@/components/home/Layout";
import { GetUSerData, ProfileDataRequestType, ProfileDataType} from "@/components/profile";


export default async function ProfilePage({
    params,

}: {
    params: {profile: string};
}) {
    const { profile } = params;

    
    
    return(
        
        <div>    
          <HomeLayout/>  
                <div className="container flex-col flex space-y-10  p-10 items-center min-h-screen">
                    <h1>Seu perfil !</h1>
                    <GetUSerData/>
                </div> 
        </div>
    )

}




