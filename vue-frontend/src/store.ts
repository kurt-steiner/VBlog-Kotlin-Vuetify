import { defineStore } from "pinia";
import { type User, type Article, type Category, type ArticleShortcut, type PostArticleRequest, type PutArticleRequest, type PostCategoryRequest, type PutCategoryRequest, type PostTagRequest, type Tag, type PutTagRequest, type RegisterRequest, type ArticleQuery } from "./types";
import * as api from "./api"

import { sha256 } from "js-sha256";

const useVBlogStore = defineStore("vblog", {
    state: () => {
        return {
            isLogined: false,
            page: 1,
            size: 20,
            totalPages: 1,
            currentUser: null as User | null,
            currentArticle: null as Article | null,
            articleShortcuts: [] as ArticleShortcut[],
            categories: [] as Category[],
            tags: [] as Tag[],
            /* sortBy: ArticleSortBy.ByPublishDate */
        }
    },

    actions: {
        logout(): void {
            localStorage.removeItem("token")
            this.currentUser = null
            this.currentArticle = null
            this.articleShortcuts = []
            this.categories = []
            this.tags = []
            /* this.sortBy = ArticleSortBy.ByPublishDate */
            this.isLogined = false
        },

        async login(username: string, password: string): Promise<void> {
            let passwordHash = sha256(password)
            let token = await api.login({name: username, passwordHash})
            localStorage.setItem("token", token)
            await this.loadCurrentUser()

            await Promise.all([
                this.loadArticles({}),
                this.loadCategories(),
                this.loadTags()
            ])

            this.isLogined = true
        },

        async register(request: RegisterRequest): Promise<void> {
            let passwordHash = sha256(request.passwordHash)
            await api.register({...request, passwordHash})
        },

        async reload(): Promise<void> {
            await this.loadCurrentUser()
            this.isLogined = true
            await Promise.all([
                this.loadArticles({}),
                this.loadCategories(),
                this.loadTags()
            ])
        },

        async loadCurrentUser(): Promise<void> {
            this.currentUser = await api.currentUser()
        },

        async loadArticles(query: ArticleQuery): Promise<void> {
            let result = await api.findAllArticles(query, {page: this.page - 1, size: this.size})
            this.articleShortcuts = result.content
            this.totalPages = result.totalPages
            /* this.sortBy = query["sort-by"] ?? ArticleSortBy.ByPublishDate */
        },

        async loadCategories(): Promise<void> {
            this.categories = await api.findAllCategories()
            this.categories.sort((left, right) => left.createTime < right.createTime ? 1 : -1)
        },

        async loadTags(): Promise<void> {
            this.tags = await api.findAllTags()
        },

        async insertArticle(request: PostArticleRequest): Promise<void> {
            let shortcut = await api.insertArticle(request)
            this.articleShortcuts.push(shortcut)
            /* this.sortBy = this.sortBy // 重新排序 */
        },

        async deleteArticle(id: number): Promise<void> {
            await api.deleteArticle(id)
            let index = this.articleShortcuts.findIndex(it => it.id == id)
            this.articleShortcuts.splice(index, 1)
        },

        async updateArticle(request: PutArticleRequest): Promise<void> {
            let article = await api.updateArticle(request)
            let index = this.articleShortcuts.findIndex(it => it.id == article.id)
            this.articleShortcuts[index] = {
                id: article.id,
                title: article.title,
                summary: article.summary,
                category: article.category,
                author: article.author,
                publishDate: article.publishDate,
                editTime: article.editTime,
                status: article.status,
                tags: article.tags
            }

            /* this.sortBy = this.sortBy // resort */
        },

        async loadArticle(id: number): Promise<void> {
            this.currentArticle = await api.findArticle(id)
        },

        async insertCategory(request: PostCategoryRequest): Promise<void> {
            let category = await api.insertCategory(request)
            this.categories.push(category)
            this.categories.sort((left, right) => left.createTime < right.createTime ? 1 : -1)
        },

        async deleteCategory(id: number): Promise<void> {
            await api.deleteCategory(id)
            let index = this.categories.findIndex(it => it.id == id)
            this.categories.splice(index, 1)
        },

        async updateCategory(request: PutCategoryRequest): Promise<void> {
            let category = await api.updateCategory(request)
            let index = this.categories.findIndex(it => it.id == category.id)
            this.categories[index] = category
            this.categories.sort((left, right) => left.createTime < right.createTime ? 1 : -1)
        },

        async insertTag(request: PostTagRequest): Promise<void> {
            let tag = await api.insertTag(request)
            this.tags.push(tag)
        },

        async deleteTag(id: number): Promise<void> {
            await api.deleteTag(id)
            let index = this.tags.findIndex(tag => tag.id == id)
            this.tags.splice(index, 1)
        },

        async updateTag(request: PutTagRequest): Promise<void> {
            let tag = await api.updateTag(request)
            let index = this.tags.findIndex(it => it.id == tag.id)
            this.tags[index] = tag
        }
    }
    
})

export default useVBlogStore