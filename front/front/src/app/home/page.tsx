
import ListaPosts, { GetPosts } from "@/context/get-feed-context";
import HomeLayout from "./layout";
import { PostForm } from "@/components/post";
import PostContainer from "@/components/post/post-container";
export  default function HomePage(){
    return(
    <>    
    <div>
        
        <PostForm></PostForm>
        <GetPosts></GetPosts>
        
        </div>
    </>
    )
}