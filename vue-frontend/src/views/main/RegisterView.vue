<template>
    <VContainer class="register-view" width="60%">
        <VCard class="pa-12" width="100%" rounded="lg">
            <VCardTitle>注册</VCardTitle>
            <VCardText>
                <VTextField 
                    v-model="request.name" 
                    label="用户名" 
                    prepend-icon="mdi-account" 
                    :rules="[notEmptyRule, maxLengthRule(64)]"
                    validate-on="eager"/>
                    
                <VTextField 
                    v-model="request.passwordHash" 
                    label="密码" 
                    type="password" 
                    prepend-icon="mdi-lock" 
                    :rules="[notEmptyRule]"
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

                <Row main-axis-size="max" main-axis-alignment="end">
                    <router-link to="/login" replace>
                        已有账户，可以登录? <VIcon icon="mdi-chevron-right"/>
                    </router-link>
                </Row>                
                
            </VCardText>
            
        </VCard>
    </VContainer>
</template>

<script lang="ts" setup>
import { useRouter } from 'vue-router';
import {useVBlogStore} from '@/store';
import { computed, reactive, ref } from 'vue';
import { type RegisterRequest } from '@/types';
import { uploadImage } from '@/api';
import { Row } from '@/components/layouts';
import { maxLengthRule, notEmptyRule } from '@/utils';
const router = useRouter()
const store = useVBlogStore()
const request = reactive<RegisterRequest>({
    name: "steiner",
    passwordHash: "779151714",
    nickname: "kurt_steiner",
    email: "steiner3044@163.com"
})

const uploadFile = ref<File | null>(null)

const avatarUrl = computed((): string | null => {
    if (uploadFile.value == null) {
        return null
    } else {
        return URL.createObjectURL(uploadFile.value)
    }
    
})

const register = async () => {
    if (uploadFile.value != null) {
        let avatar = await uploadImage(uploadFile.value)
        request.avatarId = avatar.id
    }
    
    await store.register(request)
    router.replace({name: "Login"})
}
</script>

<style lang="scss" scoped>
a {
    text-decoration: none;
}
</style>