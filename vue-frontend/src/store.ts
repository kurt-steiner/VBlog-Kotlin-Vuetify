import { defineStore } from "pinia";
import { type User, type Article, type Category, type ArticleShortcut, type PostArticleRequest, type PutArticleRequest, type PostCategoryRequest, type PutCategoryRequest, type PostTagRequest, type Tag, type PutTagRequest, type RegisterRequest, type ArticleQuery, ArticleStatus, type PutUserRequest } from "./types";
import * as api from "./api"

import { sha256 } from "js-sha256";
import { ref } from "vue";

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

        async reloadUser(): Promise<void> {
            await this.loadCurrentUser()
            this.isLogined = true
        },

        async updateUser(request: PutUserRequest): Promise<void> {
            if (request.passwordHash != undefined) {
                request.passwordHash = sha256(request.passwordHash)
            }
            
            await api.updateUser(request)
            this.logout()
        },

        async reloadMissingData(status: ArticleStatus = ArticleStatus.Published): Promise<void> {
            let futures: Promise<void>[] = []

            if (this.articleShortcuts.length == 0) {
                futures.push(this.loadArticles({status}))
            }

            if (this.categories.length == 0) {
                futures.push(this.loadCategories())
            }
             
            if (this.tags.length == 0) {
                futures.push(this.loadTags())
            }

            await Promise.all(futures)
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
            this.categories.sort((left, right) => left.id < right.id ? -1 : 1)
        },

        async loadTags(): Promise<void> {
            this.tags = await api.findAllTags()
            this.tags.sort((left, right) => left.id < right.id ? -1 : 1)
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
            let index = this.articleShortcuts.findIndex(it => it.id == request.id)
            let article = await api.updateArticle(request)

            if (this.articleShortcuts[index].status != request.status) {
                this.articleShortcuts.splice(index, 1)
                return
            }
            
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
            if (this.categories.some(it => it.id == category.id)) {
                return
            }


            this.categories.push(category)
            this.categories.sort((left, right) => left.id < right.id ? -1 : 1)
        },

        async deleteCategory(id: number): Promise<void> {
            await api.deleteCategory(id)
            let index = this.categories.findIndex(it => it.id == id)
            this.categories.splice(index, 1)
            this.articleShortcuts
                .filter((article) => article.category?.id == id)
                .forEach((article) => article.category = undefined)
        },

        async updateCategory(request: PutCategoryRequest): Promise<void> {
            let category = await api.updateCategory(request)
            let index = this.categories.findIndex(it => it.id == category.id)
            this.categories[index] = category
            this.categories.sort((left, right) => left.id < right.id ? -1 : 1)

            this.articleShortcuts
                .filter((article) => article.category?.id == request.id)
                .forEach((article) => article.category = category)
        },

        async insertTag(request: PostTagRequest): Promise<void> {
            let tag = await api.insertTag(request)
            if (this.tags.some(it => it.id == tag.id)) {
                return
            }
            
            this.tags.push(tag)
        },

        async deleteTag(id: number): Promise<void> {
            await api.deleteTag(id)
            let index = this.tags.findIndex(tag => tag.id == id)
            this.tags.splice(index, 1)

            this.articleShortcuts
                .filter((article) => article.tags.findIndex((tag) => tag.id == id) != -1)
                .forEach((article) => {
                    const index = article.tags.findIndex((tag) => tag.id == id)
                    article.tags.splice(index, 1)
                })
        },

        async updateTag(request: PutTagRequest): Promise<void> {
            let tag = await api.updateTag(request)
            let index = this.tags.findIndex(it => it.id == tag.id)
            this.tags[index] = tag

            this.articleShortcuts
                .filter((article) => article.tags.findIndex((tag) => tag.id == request.id))
                .forEach((article) => {
                    const index = article.tags.findIndex((tag) => tag.id == request.id)
                    article.tags[index] = tag
                })
        }
    }
    
})

const useSnackbarStore = defineStore("snackbar", () => {
    const actived = ref(false)
    const message = ref("")
    const status = ref<"success" | "error">("error")

    const showSnackbar = (data: {message: string, status: "success" | "error"}) => {
        actived.value = true
        message.value = data.message
        status.value = data.status

        setTimeout(closeSnackbar, 3000)
    }

    const closeSnackbar = () => {
        actived.value = false
    }

    return {
        actived,
        message,
        status,
        showSnackbar,
        closeSnackbar
    }
})

export {
    useVBlogStore,
    useSnackbarStore
}