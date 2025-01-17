import { authenticateInstance, normalInstance } from "."
import type { PutUserRequest, RegisterRequest, Response, User } from "../types"

export const register = async (request: RegisterRequest): Promise<void> => {
    await normalInstance.post("/register", request)
}

export const currentUser = async (): Promise<User> => {
    let response = await authenticateInstance.get("/user")
    response.data.data.registerTime = new Date(response.data.data.registerTime)
    return (response.data as Response<User>).data!
}

export const updateUser = async (request: PutUserRequest): Promise<void> => {
    await authenticateInstance.put("/user", request)
}

export const deleteUser = async (id: number): Promise<void> => {
    await authenticateInstance.delete(`/user/${id}`)
}