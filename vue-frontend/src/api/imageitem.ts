import { normalInstance } from ".";
import type { ImageItem, Response } from "../types";
import { BACKEND_URL } from ".";
export const uploadImage = async (file: File): Promise<ImageItem> => {
    let formData = new FormData()
    formData.append("file", file)
    let response = await normalInstance.post("/image/upload", formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
    return (response.data as Response<ImageItem>).data!
}

export const findImage = (id: number): string => {
    return `${BACKEND_URL}/image/download/${id}`
}