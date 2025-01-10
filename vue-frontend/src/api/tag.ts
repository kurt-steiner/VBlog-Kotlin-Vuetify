import { authenticateInstance } from ".";
import type { PostTagRequest, PutTagRequest, Response, Tag } from "../types";

export const insertTag = async (request: PostTagRequest): Promise<Tag> => {
    let response = await authenticateInstance.post("/tag", request)
    return (response.data as Response<Tag>).data!
}

export const updateTag = async (request: PutTagRequest): Promise<Tag> => {
    let response = await authenticateInstance.put("/tag", request)
    return (response.data as Response<Tag>).data!
}

export const deleteTag = async (id: number): Promise<void> => {
    await authenticateInstance.delete(`/tag/${id}`)
}

export const findAllTags = async (): Promise<Tag[]> => {
    let response = await authenticateInstance.get("/tag")
    return (response.data as Response<Tag[]>).data!
}