import { authenticateInstance } from "."
import type { Article, ArticleQuery, ArticleShortcut, Page, PostArticleRequest, PutArticleRequest, Response } from "../types"

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

export const findAllArticles = async (query: ArticleQuery, {page, size}: {page ?: number, size ?: number}): Promise<Page<ArticleShortcut>> => {
    let response = await authenticateInstance.get("/article", {
        params: {
            ...query,
            page,
            size
        }
    })

    response.data.data.content = response.data.data.content.map((article: {publishDate: string, editTime: string, [key: string]: any}) => {
        let publishDate = new Date(article.publishDate)
        let editTime = new Date(article.editTime)
        return {
            ...article,
            publishDate,
            editTime
        }
    })

    return (response.data as Response<Page<ArticleShortcut>>).data!
}

export const findArticle = async (id: number): Promise<Article> => {
    let response = await authenticateInstance.get(`/article/${id}`)
    response.data.data.publishDate = new Date(response.data.data.publishDate)
    response.data.data.editTime = new Date(response.data.data.editTime)
    return (response.data as Response<Article>).data!
}