import { useState } from "react";
import { frontEndApi } from "@/lib/api";
import { Heart, HeartOff } from "lucide-react";
import { PostContentType } from "@/app/api/post/route";
import { CommentContentType } from "@/app/api/post/splited/route";

interface LikeContainerProps {
  comment: CommentContentType;  
  initialIsLiked: boolean;
}

const LikeContainerComment: React.FC<LikeContainerProps> = ({ comment, initialIsLiked }) => {
  const [isLiked, setIsLiked] = useState(initialIsLiked);
  const [nLikes, setNLikes] = useState(comment.likes);
  const [error, setError] = useState(null);


  const handleLike = async () => {
    try {
      if (isLiked) {
        await frontEndApi.post("/comment/unlike", JSON.stringify(comment.comentId))
       
        setNLikes((prevLikes: number) => prevLikes - 1);
      
      } else {
        
        console.log(comment.comentId);
        await frontEndApi.post("/comment/like", JSON.stringify(comment.comentId));
        setNLikes((prevLikes: number) => prevLikes + 1);
        
      
      }
      setIsLiked(!isLiked);
    } catch (error) {
      console.error("Erro ao curtir/descurtir comentario:", error);
    }
  };

  return (
    <div>
      {isLiked ? (
        <HeartOff
          size={16}
          className="stroke-red-500 hover:stroke-primary-foreground hover:cursor-pointer"
          onClick={handleLike}
        />
      ) : (
        <Heart
          size={16}
          onClick={handleLike}
          className="hover:stroke-red-500 hover:cursor-pointer"
        />
      )}
      <span>{nLikes}</span>
      <span>{}</span>
      </div>
  );
};

export default LikeContainerComment;
