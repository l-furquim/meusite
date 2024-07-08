'use client'
import { PostContentType, PostsType } from "@/app/api/post/route";
import ListaPosts from "@/components/post/post-container";
import { backendApi, frontEndApi } from "@/lib/api";
import { createContext , useState, useEffect} from "react";

export type PostsTable = {
    posts: PostContentType[],
    refreshPosts: () => void;

}
  
  export const GetPosts: React.FC = () => {
    const [postsTable, setPostsTable] = useState<PostsTable>({
      posts: [],
      refreshPosts: () => {}
    });
  
    useEffect(() => {
      const fetchPosts = async () => {
        try {
          const response = await frontEndApi.get<PostContentType[]>('/post'); // ajuste o endpoint conforme necessário
            
        console.log(response);

          setPostsTable({
            posts: response.data,
            refreshPosts: fetchPosts
          });
        } catch (error) {
          console.error('Erro ao buscar posts:', error);
        }
      };
  
      fetchPosts();
    }, []);
  
    return (
        <div>
          <ListaPosts posts={postsTable.posts} /> {/* Passando os posts como prop para ListaPosts */}
        </div>

    );
};

export default ListaPosts
