import { createRouter, createWebHashHistory, type RouteRecordRaw } from "vue-router"

const routes: RouteRecordRaw[] = [
    {
        path: "/",
        name: "Home",
        component: () => import("./views/main/HomeView.vue"),
    },

    {
        path: "/login",
        name: "Login",
        component: () => import("./views/main/LoginView.vue")
    },

    {
        path: "/register",
        name: "Register",
        component: () => import("./views/main/RegisterView.vue")
    },

    {
        path: "/edit-article/:id",
        name: "EditArticle",
        component: () => import("./views/article/EditArticleView.vue")
    },

    {
        path: "/article/:id",
        name: "Article",
        component: () => import("./views/article/ArticleView.vue")
    },

    {
        path: "/post-article",
        name: "PostArticle",
        component: () => import("./views/article/PostArticleView.vue")
    },

    {
        path: "/setting",
        name: "Setting",
        component: () => import("./views/setting/SettingView.vue")
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router