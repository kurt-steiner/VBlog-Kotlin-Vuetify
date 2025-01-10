import { authenticateInstance } from "."
import type { Article, ArticleShortcut, Page, PostArticleRequest, PutArticleRequest, Response } from "../types"

export const insertArticle = async (request: PostArticleRequest): Promise<ArticleShortcut> => {
    let response = await authenticateInstance.post("/article", request)
    return (response.data as Response<ArticleShortcut>).data!
}

export const deleteArticle = async (id: number): Promise<void> => {
    await authenticateInstance.delete(`/article/${id}`)
}

export const updateArticle = async (request: PutArticleRequest): Promise<Article> => {
    let response = await authenticateInstance.put("/article", request)
    return (response.data as Response<Article>).data!
}

export const findAllArticles = async ({query, content, page, size, sortBy}: { query: "author" | "status" | "title", content?: string, page?: number, size?: number, sortBy ?: 0 | 1 | 2}): Promise<Page<ArticleShortcut>> => {
    let response = await authenticateInstance.get("/article", {
        params: {
            query,
            content,
            page,
            size,
            "sort-by": sortBy
        }
    })

    return (response.data as Response<Page<ArticleShortcut>>).data!
}

export const findArticle = async (id: number): Promise<Article> => {
    let response = await authenticateInstance.get(`/article/${id}`)

    return (response.data as Response<Article>).data!
}