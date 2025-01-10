<template>
    <VContainer class="login-view pt-12" width="60%">
        <VCard width="100%" class="pa-12" rounded="lg">
            <VCardTitle>登录</VCardTitle>
            <VCardText>
                <VTextField v-model="username" 
                        label="用户名" 
                        prepend-icon="mdi-account"
                        validate-on="eager"
                        :rules="usernameRules"/>
                <VTextField v-model="password" 
                            label="密码" 
                            type="password" 
                            validate-on="eager"
                            prepend-icon="mdi-lock"
                            :rules="passwordRules"/>

                <VBtn @click="login" color="primary" block>登录</VBtn>


                <Row main-axis-size="max" main-axis-alignment="end">
                    <router-link to="/register">
                        现在注册 <VIcon icon="mdi-chevron-right"/>
                    </router-link>
                </Row>
                

            </VCardText>
        </VCard>
        
    </VContainer>

    <VSnackbar v-model="alertActived" 
               color="error" 
               :timeout="3000" 
               width="50%"
               :height="60"
               position="fixed"
               location="top"
               :text="errorMessage"/>

</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { Row } from '../components/layouts';
import useVBlogStore from '../store';
import { ref } from 'vue';
import type { Response } from '../types';
import axios from 'axios';

const store = useVBlogStore()
const router = useRouter()

const username = ref("steiner")
const password = ref("779151714")
const alertActived = ref(false)
const errorMessage = ref("")

const usernameRules = [
    () => username.value.length > 0 || "username cannot be empty"
]

const passwordRules = [
    () => password.value.length > 0 || "password cannot be empty"
]
const login = async (): Promise<void> => {
    try {
        await store.login(username.value, password.value)
        router.push('/')
    } catch (error: any) {
        if (axios.isAxiosError(error)) {
            alertActived.value = true
            errorMessage.value = (error.response?.data as Response<string> | undefined)?.message ?? "login error"
            password.value = ""
        }
    }

}
</script>

<style lang="scss" scoped>
a {
    text-decoration: none;
}
</style>