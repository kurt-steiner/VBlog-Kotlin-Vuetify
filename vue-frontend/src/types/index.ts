import type { ArticleStatus } from "./models"

export interface Response<T> {
    message: string
    data?: T
}

export interface Page<T> {
    content: T[]
    totalPages: number
}

export enum ArticleSortBy {
    ByTitle,
    ByEditTime,
    ByPublishDate
}

export interface ArticleQuery {
    title?: string
    status?: ArticleStatus
    "tag-id"?: number
    "sort-by"?: ArticleSortBy
    "category-id"?: number
    reverse?: boolean
}

export * from "./models"
export * from "./requests"