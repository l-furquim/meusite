
import type { Metadata } from "next";
import { Poppins } from "next/font/google";
import "./globals.css";
import {cn } from '@/lib/utils'
import { AuthContextProvider } from "@/context/auth-context";
import { AppWindowIcon } from "lucide-react";





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
      <head>
    <link rel="icon" href="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiBzdHJva2U9ImN1cnJlbnRDb2xvciIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiIGNsYXNzPSJsdWNpZGUgbHVjaWRlLWFwcC13aW5kb3ctbWFjIj48cmVjdCB3aWR0aD0iMjAiIGhlaWdodD0iMTYiIHg9IjIiIHk9IjQiIHJ4PSIyIi8+PHBhdGggZD0iTTYgOGguMDEiLz48cGF0aCBkPSJNMTAgOGguMDEiLz48cGF0aCBkPSJNMTQgOGguMDEiLz48L3N2Zz4=" />
    </head>
      <body className={cn(poppins.className, "bg-zinc-500")}>{children}</body>
    </html>
    </AuthContextProvider>
  );
}
