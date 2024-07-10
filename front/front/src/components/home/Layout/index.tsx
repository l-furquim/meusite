import { AppWindowMac } from "lucide-react";
import { UserButton} from "../button/UserButton";
import DeslogButton from "../button/DeslogUserButton";

export default function HomeLayout() {
    return (
        
  
    
    <div className="container  p-1 items-center gap-4 bg-zinc-200">
        <span className="flex items-center space-x-3 p-3 mb-2">
            <AppWindowMac/>
            <a href="/home/0">
            <h1> Pagina inicial </h1>
            </a>
           <UserButton></UserButton>
           <DeslogButton></DeslogButton>
        </span>
    </div>
   
    )
}