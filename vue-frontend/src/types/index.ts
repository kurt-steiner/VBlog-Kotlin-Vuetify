export interface Response<T> {
    message: string
    data?: T
}

export * from "./models"
export * from "./requests"