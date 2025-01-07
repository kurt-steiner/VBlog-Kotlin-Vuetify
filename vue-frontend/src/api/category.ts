import { authenticateInstance } from ".";
import type { Category, PostCategoryRequest, Response } from "../types";

export const insertCategory = async (request: PostCategoryRequest): Promise<Category> => {
    let response = await authenticateInstance.post("/category", request)
    return (response.data["data"] as Response<Category>).data!
}

export const updateCategory = async (request: PostCategoryRequest): Promise<Category> => {
    let response = await authenticateInstance.put("/category", request)
    return (response.data["data"] as Response<Category>).data!
}

export const deleteCategory = async (id: number): Promise<void> => {
    await authenticateInstance.delete(`/category/${id}`)
}

export const findAllCategory = async (userId: number): Promise<Category[]> => {
    let response = await authenticateInstance.get(`/category/user/${userId}`)
    return (response.data["data"] as Response<Category[]>).data!
}