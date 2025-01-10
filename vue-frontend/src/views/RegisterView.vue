<template>
    <VContainer class="register-view" width="60%">
        <VCard class="pa-12" width="100%" rounded="lg">
            <VCardTitle>注册</VCardTitle>
            <VCardText>
                <VTextField 
                    v-model="request.name" 
                    label="用户名" 
                    prepend-icon="mdi-account" 
                    :rules="usernameRules"
                    validate-on="eager"/>
                    
                <VTextField 
                    v-model="request.passwordHash" 
                    label="密码" 
                    type="password" 
                    prepend-icon="mdi-lock" 
                    :rules="passowrdRules"
                    validate-on="eager"/>
                    
                <VTextField v-model="request.nickname" label="昵称" prepend-icon="mdi-account"/>
                <VTextField v-model="request.email" label="邮箱" type="email" prepend-icon="mdi-email"/>


                <VFileInput accept="image/*" 
                            clearable 
                            v-model="uploadFile"
                            label="头像上传" 
                            prepend-icon="mdi-camera"/>

                            

                <Row class="pb-4" main-axis-size="max" main-axis-alignment="center">
                    <VAvatar size="x-large" v-if="avatarUrl != null">
                        <VImg :src="avatarUrl"/>
                    </VAvatar>
                </Row>

                
                
                <VBtn @click="register" color="primary" block>注册</VBtn>
            </VCardText>
            
        </VCard>
    </VContainer>

    <VSnackbar v-model="alertActived"
               color="error"
               width="50%"
               :height="60"
               position="fixed"
               location="top"
               :text="errorMessage"/>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import useVBlogStore from '../store';
import { computed, reactive, ref } from 'vue';
import { type RegisterRequest, type Response } from '../types';
import axios from 'axios';
import { uploadImage } from '../api';
import { Row } from '../components/layouts';
const router = useRouter()
const store = useVBlogStore()
const request = reactive<RegisterRequest>({
    name: "steiner",
    passwordHash: "779151714",
    nickname: "kurt_steiner",
    email: "steiner3044@163.com"
})

const uploadFile = ref<File | null>(null)
const alertActived = ref(false)
const errorMessage = ref("")

const avatarUrl = computed((): string | null => {
    if (uploadFile.value == null) {
        return null
    } else {
        return URL.createObjectURL(uploadFile.value)
    }
    
})

const usernameRules = [
    () => request.name.length > 0 || "用户名不能为空"
]

const passowrdRules = [
    () => request.passwordHash.length > 0 || "密码不能为空"
]

const register = async () => {
    try {

        if (uploadFile.value != null) {
            let avatar = await uploadImage(uploadFile.value)
            request.avatarId = avatar.id
        }
        
        await store.register(request)
        router.replace({name: "Login"})
    } catch (error: any) {
        alertActived.value = true
        if (axios.isAxiosError(error)) {
            errorMessage.value = (error.response?.data as Response<string>).message
        } else {
            errorMessage.value = `${error}`
        }
    }
}
</script>