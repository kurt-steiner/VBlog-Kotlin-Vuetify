import { authenticateInstance } from ".";
import type { ImageItem, Response } from "../types";

export const uploadImage = async (file: File): Promise<ImageItem> => {
    let formData = new FormData()
    formData.append("file", file)
    let response = await authenticateInstance.post("/image/upload", formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
    return (response.data["data"] as Response<ImageItem>).data!
}

export const findImage = async (id: number): Promise<string> => {
    let response = await authenticateInstance.get(`/image/download/${id}`, {
        responseType: "blob"
    })

    return URL.createObjectURL(response.data)
}