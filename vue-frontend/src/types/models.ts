export interface User {
    id: number
    name: string
    nickname?: string
    email?: string
    registerTime: Date
    avatar?: ImageItem
}

export interface ImageItem {
    id: number
    name: string
}

export interface Tag {
    id: number
    name: string
    userId: number
}

export interface Category {
    id: number
    name: string
    createTime: Date
    userId: number
}

export interface Article {
    id: number
    title: string
    markdownContent: string
    htmlContent: string
    summary: string
    category?: Category
    author: User
    publishDate: Date
    editTime: Date
    status: ArticleStatus
    tags: Tag[]
}

export interface ArticleShortcut {
    id: number
    title: string
    summary: string
    category?: Category
    author: User
    publishDate: Date
    editTime: Date
    status: ArticleStatus
}

export enum ArticleStatus {
    Draft = 0,
    Published,
    Dustbin,
    Deleted
}