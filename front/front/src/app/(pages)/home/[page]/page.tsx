'use client'

import { PostForm } from "@/components/post";
import { GetPosts } from "@/context/get-feed-context";
import { AppWindowMac } from "lucide-react"
import { UserButton } from "@/components/home/button/UserButton"
import DeslogButton from "@/components/home/button/DeslogUserButton"
import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
  } from "@/components/ui/pagination"
import { backendApi } from "@/lib/api";
import axios, { AxiosError } from "axios";
import { getCookie } from "@/services/cookie/cookies";
import { useEffect, useState } from "react";
import { BackendPostErrorType } from "@/app/api/post/route";
import HomeLayout from "@/components/home/Layout";
  

export default function HomePage({
    params,

}: {
    params: {page: number};

}) {   
    const {page} = params;
    const [currentPage, setCurrentPage] = useState(page);

    const handleClick = async (pageNumber: number) => {
        try {
            const authToken = getCookie("meusite-token"); // Obtenha o token de autenticação da forma apropriada
            const result = await backendApi.get(`/post/feed?page=${pageNumber}`, {
                headers: {
                    "Authorization": `Bearer ${authToken}`
                }
            });

            const posts = result.data;

            // Aqui você pode fazer algo com os posts obtidos, como atualizar o estado da sua aplicação
            // ou renderizar os posts na página.

            setCurrentPage(pageNumber); // Atualiza o estado para refletir a página atual

        } catch (e) {
            console.error("Erro na requisição GET para /api/post:", e);

        if (axios.isAxiosError(e)) {
        const axiosError = e as AxiosError;

        if (axiosError.response) {
            const responseData = axiosError.response.data;

            if (responseData && typeof responseData === "object") {
                const { status, message } = responseData as BackendPostErrorType;

                if (status && message) {
                    return new Response(JSON.stringify({ error: message }), { status });
                }
            }

            return new Response(JSON.stringify({ error: axiosError.message }), { status: axiosError.response.status || 500 });
        }
        }
    };
    }
    useEffect(() => {
        // Executa a primeira consulta ao carregar a página, usando o estado currentPage
        handleClick(currentPage);
    }, []); // Executa apenas uma vez ao carregar a página inicial
    
    
    
    return(
<>           
    <HomeLayout/>
    <div className="container p-5 mt-4 rounded-xl space-y-5 bg-gray-400 min-h-screen">
        <PostForm></PostForm>
        <GetPosts></GetPosts>
        
        <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious href="#" onClick={() => handleClick(currentPage - 1)} />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink href="#" onClick={() => handleClick(1)} isActive={currentPage === 1}>
                                    1
                                </PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink href="#" onClick={() => handleClick(2)} isActive={currentPage === 2}>
                                    2
                                </PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink href="#" onClick={() => handleClick(3)} isActive={currentPage === 3}>
                                    3
                                </PaginationLink>
                            </PaginationItem>
                            {/* Adicione mais links de paginação conforme necessário */}
                            <PaginationItem>
                                <PaginationEllipsis />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext href="#" onClick={() => handleClick(currentPage + 1)} />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
    
    </div>
    </>
    )
}

