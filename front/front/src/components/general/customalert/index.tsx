import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertTriangleIcon, CheckIcon } from "lucide-react";

export const enum CustomAlertType{
    SUCESS = "sucess",
    ERROR = "error"
}


export type CustomAlertProps = {
    type: CustomAlertType.SUCESS  | CustomAlertType.ERROR;
    title: string;
    msg: string;

}

export function CustomAlert({type, title, msg} : CustomAlertProps){
    
    const alert = type === CustomAlertType.SUCESS ? 
    <CustomSucessAlert title={title} message={msg}/> :
    <CustomErrorAlert title={title} message={msg}/>
    
    return alert;
}


type SucessAlertProps = {
    title:string;
    message:string
};


type ErrorAlertProps = {
    title:string;
    message:string
};

function CustomSucessAlert({title , message}: SucessAlertProps) {
    return(
        <Alert className="flex-row stroke-emerald-500">
        <CheckIcon className="h-8 w-8 stroke-emerald-500"/>
        <AlertTitle className="font-bold mx-4 ">
        {title}
        </AlertTitle>
        <AlertDescription className="font-thin mx-4 ">
        {message}
        </AlertDescription> 
        </Alert>
    )
}

function CustomErrorAlert({title, message}: ErrorAlertProps){
    return (
        <Alert className="flex-row border-red-500">
        <AlertTriangleIcon className="h-8 w-8 stroke-red-500"/>
        <AlertTitle className="font-bold mx-4 ">
        {title}
        </AlertTitle>
        <AlertDescription className="font-thin mx-4 ">
        {message}
        </AlertDescription>
        </Alert>
    )
}