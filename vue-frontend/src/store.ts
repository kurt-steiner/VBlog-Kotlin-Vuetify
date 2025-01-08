import { defineStore } from "pinia";
import { type User, type Article, type Category, type ArticleShortcut, type PostArticleRequest, ArticleSortBy, type PutArticleRequest, type PostCategoryRequest, type PutCategoryRequest, type PostTagRequest, type Tag, type PutTagRequest } from "./types";
import * as api from "./api"

const useVBlogStore = defineStore("vblog", {
    state: () => {
        return {
            currentUser: null as User | null,
            currentArticle: null as Article | null,
            articleShortcuts: [] as ArticleShortcut[],
            categories: [] as Category[],
            tags: [] as Tag[],
            sortBy: ArticleSortBy.ByPublishDate
        }
    },

    actions: {
        set sortBy(value: ArticleSortBy) {
            this.sortBy = value
            switch (value) {
                case ArticleSortBy.ByPublishDate: {
                    this.articleShortcuts.sort((left, right) => left.publishDate < right.publishDate ? 1 : -1)
                    break
                }

                case ArticleSortBy.ByTitle: {
                    this.articleShortcuts.sort((left, right) => left.title < right.title ? 1 : -1)
                    break
                }

                case ArticleSortBy.ByEditTime: {
                    this.articleShortcuts.sort((left, right) => left.editTime < right.editTime ? 1 : -1)
                    break
                }
            }
        },
        
        logout(): void {
            localStorage.removeItem("token")
            this.currentUser = null
            this.currentArticle = null
            this.articleShortcuts = []
            this.categories = []
            this.tags = []
            this.sortBy = ArticleSortBy.ByPublishDate
        },

        async login(token: string): Promise<void> {
            localStorage.setItem("token", token)
            await this.loadCurrentUser()

            await Promise.all([
                this.loadArticles({}),
                this.loadCategories()
            ])
        },


        async loadCurrentUser(): Promise<void> {
            this.currentUser = await api.currentUser()
        },

        async loadArticles({page, size, sortBy}: {page ?: number, size ?: number, sortBy ?: 0 | 1 | 2}): Promise<void> {
            let result = await api.findAllArticles({query: "author", page, size, sortBy})
            this.articleShortcuts = result.content
            this.sortBy = sortBy ?? ArticleSortBy.ByPublishDate
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
            this.sortBy = this.sortBy // 重新排序
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
                status: article.status
            }

            this.sortBy = this.sortBy // resort
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