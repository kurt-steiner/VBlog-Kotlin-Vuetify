<template>
    <VContainer class="login-view pt-12" width="60%">
        <VCard width="100%" class="pa-12" rounded="lg">
            <VCardTitle>登录</VCardTitle>
            <VCardText>
                <VForm>
                    <template #default="{ isValid }">
                        <VTextField v-model="username" 
                                label="用户名" 
                                prepend-icon="mdi-account"
                                validate-on="eager"
                                :rules="[notEmptyRule, maxLengthRule(64)]"/>
                        <VTextField v-model="password" 
                                    label="密码" 
                                    type="password" 
                                    validate-on="eager"
                                    prepend-icon="mdi-lock"
                                    :rules="[notEmptyRule]"/>

                        <VBtn @click="login" color="primary" block :disabled="!isValid.value">登录</VBtn>
                    </template>
                    
                </VForm>
                

                <Row main-axis-size="max" main-axis-alignment="end">
                    <router-link to="/register">
                        现在注册 <VIcon icon="mdi-chevron-right"/>
                    </router-link>
                </Row>
                

            </VCardText>
        </VCard>
        
    </VContainer>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import { Row } from '@/components/layouts';
import {useVBlogStore} from '@/store';
import { ref } from 'vue';
import { maxLengthRule, notEmptyRule } from '@/utils';

const store = useVBlogStore()
const router = useRouter()

const username = ref("steiner")
const password = ref("779151714")

const login = async (): Promise<void> => {
    try {
        await store.login(username.value, password.value)
        router.push('/')
    } catch (error: any) {
        password.value = ""
    }
}
</script>

<style lang="scss" scoped>
a {
    text-decoration: none;
}
</style>