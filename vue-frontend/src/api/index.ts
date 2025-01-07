import axios from "axios";

import router from "../router"

export const normalInstance = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    }
})

const authenticateInstance = axios.create({
    baseURL: "http://localhost:8080",
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
    error => {
        if (error.response.status === 401) {
            router.replace({ name: "login" })
        }

        return Promise.reject(error)
    }
)

export {
    authenticateInstance
}

export * from "./article"
export * from "./authenticate"
export * from "./category"
export * from "./imageitem"
export * from "./tag"
export * from "./user"