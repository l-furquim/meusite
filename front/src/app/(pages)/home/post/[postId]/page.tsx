'use client'
import { LikeContentType } from "@/app/api/post/like/route";
import { PostContentType } from "@/app/api/post/route";
import { CommentContentType, GetPostType } from "@/app/api/post/splited/route";
import LikeContainerComment from "@/components/comment/like";
import CommentContainer from "@/components/post/comment/comment-container";
import LikeContainerPost from "@/components/post/like";
import LikeContainer from "@/components/post/like";
import { frontEndApi } from "@/lib/api";
import { useEffect, useState } from "react";

export default function PostSplited({
    params,
}: {
    params: { postId: number };
}) {
    const [userLikes, setUserLikes] = useState<LikeContentType[]>([]);
    const [postt, setPostt] = useState<PostContentType | null>(null);
    const [comments, setComments] = useState<CommentContentType[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
            
                const response = await frontEndApi.get("/post/like");
                const likesData: LikeContentType[] = JSON.parse(response.data);
                setUserLikes(likesData);

                const response2 = await frontEndApi.post<GetPostType>("/post/splited", params.postId);
                const { post, comments } = response2.data;
                setPostt(post);
                setComments(comments);
            } catch (error) {
                console.error("Erro ao buscar dados:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [params.postId]);

    
    const isPostLikedByUser = (postId: number) => {
        return userLikes.some((like) => like.postId === postId);
    };

    const isCommentLikedByUser = (commentId: number) => {
        return userLikes.some((like) => like.commentId === commentId);
    };

    
    if (loading || !postt) {
        return <div>Carregando...</div>;
    }

    return (
    <div className="container p-2 mt-4 rounded-xl space-y-5 bg-gray-400 min-h-screen">
        <div className="container mt-8 items-center rounded-md">
            
            <div className="container mt-5 flex-col flex space-y-10 max-w-screen-md h-60 pb-3 bg-gray-700 rounded-md border-stone-950 border-2">
               
                <strong className="">{postt.userEmail}</strong>
                <p className="text-slate-300">{postt.content}</p>
                <p className="text-right">{postt.postedAt}</p>

                <p className="text-left items-row flex gap-8">
                    <LikeContainerPost post={postt} initialIsLiked={isPostLikedByUser(postt.tweet_id)} />
                    <CommentContainer ncomments={postt.ncoments} postId={params.postId} />
                </p>
               
                    {comments.map((comment) => (
                         <div className="container flex flex-col gap-4 bg-gray-700 rounded-md border-stone-950 border-2 max-h-max  pb-15" >
                        <ul key={comment.comentId}>
                            <li className="space-y-10">
                                <p className="font-bold">{comment.userEmail}</p>
                                <p className="text-slate-300">{comment.content}</p>
                                <LikeContainerComment comment={comment} initialIsLiked={isCommentLikedByUser(comment.comentId)}/>
                            </li>
                        </ul>
                        </div>
                    ))}
                </div>
        </div>
        </div>
    );
}
