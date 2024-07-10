'use client'
import { PostContentType } from "@/app/api/post/route";
import { PostsTable } from "@/context/get-feed-context";

type ListaPostsProps = {
    posts: PostContentType[];
  };

  //className="container items-center space-y-10 rounded-md"

  const PostContainer: React.FC<ListaPostsProps> = ({ posts }) => {
    return (
      <div className="container mt-8 items-center rounded-md ">
        <ul>
          {posts.map(post => (
            <li className="container mt-5 flex-col flex space-y-4 max-w-screen-md pb-10 bg-gray-700 rounded-md border-stone-950 border-2">
              <a href={`/profile/${post.userEmail}`}>
              <strong className="font-bold mt-2 mr-40">{post.userEmail}</strong>
              </a>
              <p className=" text-slate-300">{post.content}</p>
              <p className="text-sm text-right ">{post.postedAt}</p>
            </li>
          ))}
        </ul>
      </div>
    );
  };
  
  export default PostContainer;