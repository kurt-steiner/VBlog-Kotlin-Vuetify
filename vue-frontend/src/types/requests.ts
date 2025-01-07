import type { Tag } from "./models"

export interface LoginRequest {
    name: string
    passwordHash: string
}

export interface PostArticleRequest {
    title: string
    markdownContent: string
    htmlContent: string
    categoryId?: number
    tags?: Tag[]
    status: number
    authorId: number
}

export interface PostCategoryRequest {
    name: string
    userId: number
}

export interface PostTagRequest {
    name: string
    userId: number
}

export interface PutArticleRequest {
    id: number
    title?: string
    markdownContent?: string
    htmlContent?: string
    categoryId?: number
    tags?: Tag[]
}

export interface PutCategoryRequest {
    id: number
    name: string
}

export interface PutTagRequest {
    id: number
    name: string
}

export interface PutUserRequest {
    id: number
    name?: string
    nickname?: string
    passwordHash?: string
    email?: string
    avatarId?: number
}

export interface RegisterRequest {
    name: string
    passwordHash: string

    nickname?: string
    email?: string
    avatarId?: number
}