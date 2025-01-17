import axios from "axios";
import router from "../router"
import {useVBlogStore, useSnackbarStore} from "../store";

export const BACKEND_URL: string = import.meta.env.VITE_BACKEND_URL

const normalInstance = axios.create({
    baseURL: BACKEND_URL,
    headers: {
        "Content-Type": "application/json",
    }
})

const errorHandler = (error: any): any => {
    const snackBarStore = useSnackbarStore()
    snackBarStore.showSnackbar({
        status: "error",
        message: (error.response.data.message as string) || "Unknown error"
    })

    if (error.response.status === 401) {
        const store = useVBlogStore()
        store.logout()
        router.replace({ name: "Login" })
    }

    return Promise.reject(error)
}

const authenticateInstance = axios.create({
    baseURL: BACKEND_URL,
    headers: {
        "Content-Type": "application/json",
    }
})

authenticateInstance.interceptors.request.use(
    config => {
        const token = localStorage.getItem("token")
        if (token != null) {
            config.headers["Authentication"] = `Bearer ${token}`
        }

        return config
    }
)

authenticateInstance.interceptors.response.use(
    response => {
        return response
    },
    errorHandler
)

normalInstance.interceptors.response.use(
    response => {
        return response
    },
    errorHandler
)

export {
    authenticateInstance,
    normalInstance
}

export * from "./article"
export * from "./authenticate"
export * from "./category"
export * from "./imageitem"
export * from "./tag"
export * from "./user"