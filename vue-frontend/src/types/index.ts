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

export * from "./models"
export * from "./requests"