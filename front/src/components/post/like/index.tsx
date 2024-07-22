import { useState } from "react";
import { frontEndApi } from "@/lib/api";
import { Heart, HeartOff } from "lucide-react";

interface LikeContainerProps {
  post: any;
  initialIsLiked: boolean;
}

const LikeContainer: React.FC<LikeContainerProps> = ({ post, initialIsLiked }) => {
  const [isLiked, setIsLiked] = useState(initialIsLiked);
  const [nLikes, setNLikes] = useState(post.likes);

  const handleLike = async () => {
    try {
      if (isLiked) {
        await frontEndApi.post("/post/unlike", post.tweet_id)
        setNLikes((prevLikes: number) => prevLikes - 1);
      } else {
        await frontEndApi.post("/post/like", post.tweet_id);
        setNLikes((prevLikes: number) => prevLikes + 1);
      }
      setIsLiked(!isLiked);
    } catch (error) {
      console.error("Erro ao curtir/descurtir post:", error);
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
    </div>
  );
};

export default LikeContainer;
