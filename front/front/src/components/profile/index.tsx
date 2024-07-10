
import { frontEndApi } from "@/lib/api"
import { AxiosError } from "axios";
import { useEffect, useState } from "react";
import { ChangePasswordLoggedForm } from "./ChangePassword/logged";

export type ProfileDataType = {
    email : string,
    password: string,
    created_at : string
}

export type ProfileDataRequestType = {
    email : string
}
export const GetUSerData= ()=> {
const [userData, setUserData] = useState<ProfileDataType | null>(null);

useEffect(() => {
    const fetchData = async () => {
        try {
            const data = await ProfilePageData() as ProfileDataType;
            setUserData(data)
        } catch (error) {
            console.error("Error fetching profile data:", error);
        }
    };

    fetchData();
}, []);

    return (
    <div className="container flex flex-col space-y-5 min-h-60 rounded-xl text-slate-300 p-10 bg-gray-700">
        {userData ? (
            

            <>    
                <p className="container p-2 shadow-md">Email: {`${userData.email}`}</p>
                <p className="container space-x-10 flex-row  p-2 shadow-md">Senha  <ChangePasswordLoggedForm></ChangePasswordLoggedForm></p>
                <p className="container p-2 shadow-md ">Data de criação da conta : {userData.created_at}</p>
            
            </>
            
            
        ) : (
            <p>Loading...</p>
        )}
    </div>
);

}


async function ProfilePageData() : Promise<ProfileDataType>{
    
    try{

        const response = await frontEndApi.get("/user/data");

        if(response){
        const userData = response.data as ProfileDataType;

        

        return userData

        }else{
            throw Error;
        }
    }catch(e){
        const axiosError = e as AxiosError;

        
        throw axiosError;
    }
    

}