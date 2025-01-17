export const notEmptyRule = (v: string) => v.trim().length > 0 || "input cannot be empty"
export const maxLengthRule = (length: number) => {
    return (v: string) => {
        return v.length < length || "too long"
    }
}

export type Validator = (v: string) => string | boolean