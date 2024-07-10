import type { Metadata } from "next";
import { Poppins } from "next/font/google";
import "./globals.css";
import {cn } from '@/lib/utils'
import { AuthContextProvider } from "@/context/auth-context";



const poppins = Poppins({weight: '300', subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Meu site",
  description: "meu site fullstack",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <AuthContextProvider>
    <html lang="pt-BR">
    <link rel="icon" href="https://images.tcdn.com.br/img/img_prod/460977/pelucia_guaxinim_cinza_e_marrom_grande_50cm_32497_1_20201211162123.jpeg" />
      <body className={cn(poppins.className, "bg-zinc-500")}>{children}</body>
    </html>
    </AuthContextProvider>
  );
}
