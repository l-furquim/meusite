'use client'
import { createContext, useState, useEffect, useContext, ReactNode } from "react";
import { frontEndApi } from "@/lib/api";
import { LikeContentType } from "@/app/api/post/like/route";

export type LikesContextType = {
  likes: LikeContentType[];
  refreshLikes: () => void;
};

const initialLikesContext: LikesContextType = {
  likes: [],
  refreshLikes: () => {},
};

export const LikesContext = createContext<LikesContextType>(initialLikesContext);

type LikesProviderProps = {
  children: ReactNode; // Define children como ReactNode
};

export const LikesProvider: React.FC<LikesProviderProps> = ({ children }) => {
  const [likes, setLikes] = useState<LikeContentType[]>(initialLikesContext.likes);

  const refreshLikes = async () => {
    try {
      const response = await frontEndApi.get<LikeContentType[]>("/post/like");
      
      setLikes(response.data);
    } catch (error) {
      console.error("Erro ao buscar likes:", error);
    }
  };

  useEffect(() => {
    refreshLikes();
  }, []);

  return (
    <LikesContext.Provider value={{ likes, refreshLikes }}>
      {children} {/* Renderiza os children aqui */}
    </LikesContext.Provider>
  );
};
