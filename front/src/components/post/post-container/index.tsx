'use client'

import { PostContentType } from "@/app/api/post/route";
import * as React from 'react';
import LikeContainer from "../like";
import { useEffect, useState } from "react";
import { LikeContentType, ListLikeContentType } from "@/app/api/post/like/route";
import { frontEndApi } from "@/lib/api";
import CommentContainer from "../comment/comment-container";
import { useNavigate } from "react-router-dom";
import LikeContainerPost from "../like";



export type ListaPostsProps = {
    posts: PostContentType[];
  };


  //className="container items-center space-y-10 rounded-md"

  const PostContainerr: React.FC<ListaPostsProps> = ({ posts }) => {
  
    const [open, setOpen] = React.useState(false);
    const [userLikes, setUserLikes] = useState<LikeContentType[]>([]);
    const [postedAt, setPostedAt] = useState("");

  

    const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };
    
  useEffect(() => {
    const fetchUserLikes = async () => {
      try {
        const response = await frontEndApi.get("/post/like");
        const likesData: LikeContentType[] = JSON.parse(response.data);
        setUserLikes(likesData);

        const dateTimes = posts.map(({ postedAt }) => {
          const [datePart, timePart] = postedAt.split(" ");
          return { datePart, timePart };
      });
        console.log(dateTimes);

         
        
    
        //const [day, month, year] = datePart.split("/");
    
    
        //const [hours, minutes, seconds] = timePart.split(":");
    
      
}
    

      catch (error) {
        console.error("Erro ao buscar likes do usuário:", error);
      }
    };

    fetchUserLikes();
  }, []);


  const isPostLikedByUser = (postId: number) => {
    return userLikes.some((like) => like.postId === postId);
  };

  function formatDate(postedAt: String){
    const [datePart, timePart] = postedAt.split(" ");
    const [day, month, year] = datePart.split("/");
    const [hours, minutes, seconds] = timePart.split(":");
    const date = new Date(parseInt(year), parseInt(month) - 1, parseInt(day), parseInt(hours), parseInt(minutes), parseInt(seconds));

    const d1 = date
    const d2 = new Date(Date.now());
    const differenceInTime = d2.getTime() - d1.getTime();
    const differenceInDays = (differenceInTime / (1000 * 3600 * 24))

    

    if(differenceInDays < 1){
      const hours = d2.getHours() - date.getHours();

      return "há " + hours + " horas";
    }

    

    const diferenceInDaysString = differenceInDays.toString();

    if(diferenceInDaysString.charAt(1) == "."){
      return "há " +  diferenceInDaysString.slice(0,1) + " dias";
    }

    return "há " +  diferenceInDaysString.slice(0,2) + " dias";

  }
    
    return (
      
      <div className="container mt-8 items-center rounded-md ">
        <ul>
          {posts.map((post) => (
            <li className="container mt-5 flex-col flex space-y-16 max-w-screen-md h-60 pb-3 bg-gray-700 rounded-md border-stone-950 border-2" key={post.tweet_id}>
              
              <a href={`/profile/${post.userEmail}`}>
              <strong className="font-bold mt-2 mr-40 max-w-md">{post.userEmail}</strong>
              <p className="text-right flex-row">{formatDate(post.postedAt)}</p>
              </a>
              <a href={`/home/post/${post.tweet_id}`}>
              <p className=" text-slate-300">{post.content}</p>
              </a>
              <p className="text-left items-row flex gap-8 ">    
              
              <LikeContainerPost post={post} initialIsLiked={isPostLikedByUser(post.tweet_id)}/>
              <CommentContainer postId={post.tweet_id} ncomments={post.ncoments} />
            </p>
            </li>
    
              ))} </ul>
      </div>
      
  )
}
  export default PostContainerr