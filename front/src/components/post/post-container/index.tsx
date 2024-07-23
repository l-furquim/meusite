'use client'

import { PostContentType } from "@/app/api/post/route";
import { MessageSquareDot } from "lucide-react";
import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import LikeContainer from "../like";
import { useEffect, useState } from "react";
import { LikeContentType, ListLikeContentType } from "@/app/api/post/like/route";
import { frontEndApi } from "@/lib/api";
import CommentContainer from "../comment/comment-container";



export type ListaPostsProps = {
    posts: PostContentType[];
  };


  //className="container items-center space-y-10 rounded-md"

  const PostContainerr: React.FC<ListaPostsProps> = ({ posts }) => {
    
    const [open, setOpen] = React.useState(false);
    const [userLikes, setUserLikes] = useState<LikeContentType[]>([]);
    
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
        console.log(likesData);
        setUserLikes(likesData);
      } catch (error) {
        console.error("Erro ao buscar likes do usuário:", error);
      }
    };

    fetchUserLikes();
  }, []);


  const isPostLikedByUser = (postId: number) => {
    return userLikes.some((like) => like.postId === postId);
  };
    
    return (
      
      <div className="container mt-8 items-center rounded-md ">
        <ul>
          {posts.map((post) => (
            <li className="container mt-5 flex-col flex space-y-16 max-w-screen-md h-60 pb-3 bg-gray-700 rounded-md border-stone-950 border-2" key={post.tweet_id}>
              <a href={`/profile/${post.userEmail}`}>
              <strong className="font-bold mt-2 mr-40">{post.userEmail}</strong>
              </a>
              <a href={`/home/post/${post.tweet_id}`}>
              <p className=" text-slate-300">{post.content}</p>
              </a>
              <p className="text-left items-row flex gap-8 ">    
              
              <LikeContainer post={post} initialIsLiked={isPostLikedByUser(post.tweet_id)}/>
              <CommentContainer postId={post.tweet_id} ncomments={post.ncoments} />
            </p>
            </li>
    
              ))} </ul>
      </div>
      
  )
}
  export default PostContainerr