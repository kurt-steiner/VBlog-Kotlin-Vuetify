import type { LoginRequest, Response } from "../types";
import { normalInstance } from ".";
export const login = async (request: LoginRequest): Promise<string> => {
    let response = await normalInstance.post("/login", request)
    return (response.data["data"] as Response<string>).data!
}