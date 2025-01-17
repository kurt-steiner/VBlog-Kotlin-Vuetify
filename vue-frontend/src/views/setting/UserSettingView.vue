<template>
    <VForm>
        <template #default="{ isValid }">
            <VCard :elevation="10">
                <VCardTitle>
                    <VFileInput accept="image/*" 
                                clearable 
                                v-model="uploadFile"
                                prepend-icon=""
                                variant="solo-filled"
                                :label="uploadFile != null ? '点击修改头像' : '点击上传头像'">
                        <template #prepend v-if="avatarUrl != null">
                            <VAvatar :image="avatarUrl" size="x-large"/>
                        </template>
                    </VFileInput>

                </VCardTitle>

                <VCardText :opacity="1">
                    <VCard title="用户名" prepend-icon="mdi-account">
                        <VCardTitle>
                            <Row main-axis-size="max" cross-axis-alignment="center">
                                <VCheckbox v-model="userFields" value="name"/>
                                <VTextField v-model="request.name" 
                                            clearable
                                            validate-on="eager"
                                            label="用户名" 
                                            :rules="availableRules('name', notEmptyRule, maxLengthRule(64))"
                                            :disabled="!hasField('name')"/>
                            </Row>
                        </VCardTitle>
                    </VCard>

                    <VCard title="昵称" prepend-icon="mdi-account-edit">
                        <VCardTitle>
                            <Row main-axis-size="max" cross-axis-alignment="center">
                                <VCheckbox v-model="userFields" value="nickname"/>
                                <VTextField v-model="request.nickname" 
                                            clearable
                                            validate-on="eager"
                                            :rules="availableRules('nickname', notEmptyRule, maxLengthRule(64))"
                                            :disabled="!hasField('nickname')"/>
                            </Row>
                        </VCardTitle>
                    </VCard>

                    <VCard title="邮箱" prepend-icon="mdi-email-outline">
                        <VCardTitle>
                            <Row main-axis-size="max" cross-axis-alignment="center">
                                <VCheckbox v-model="userFields" value="email"/>
                                <VTextField v-model="request.email" 
                                            type="email" 
                                            clearable
                                            validate-on="eager"
                                            :rules="availableRules('email', notEmptyRule, maxLengthRule(64))"
                                            :disabled="!hasField('email')"/>
                            </Row>
                        </VCardTitle>
                    </VCard>
                    
                    <VCard title="更换新密码" prepend-icon="mdi-lock">
                        <VCardTitle>
                            <Row main-axis-size="max" cross-axis-alignment="center">
                                <VCheckbox v-model="userFields" value="passwordHash"/>
                                <VTextField v-model="request.passwordHash" 
                                            type="password"
                                            validate-on="eager" 
                                            clearable 
                                            :rules="availableRules('passwordHash', notEmptyRule)"
                                            :disabled="!hasField('passwordHash')"/>
                            </Row>
                        </VCardTitle>
                    </VCard>
                </VCardText>

                <VCardActions>
                    <Row main-axis-size="max" main-axis-alignment="end">
                        <VBtn color="grey" @click="cancelUpdate">取消</VBtn>
                        <VBtn color="primary" @click="submitUserEdit" :disabled="!isValid.value">提交</VBtn>
                    </Row>
                </VCardActions>
            </VCard>
        </template>
    </VForm>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import type { PutUserRequest } from '@/types';
import { notEmptyRule, maxLengthRule, type Validator } from '@/utils';
import { findImage, uploadImage } from '@/api';
import { useVBlogStore } from '@/store';
import { useRouter } from 'vue-router';
import { Row } from '@/components/layouts';
const store = useVBlogStore()
const router = useRouter()

const uploadFile = ref<File | null>(null)

const request = ref<PutUserRequest>({id: -1})
    const userFields = ref<string[]>([])

const hasField = (field: string) => {
    return userFields.value.includes(field)
}

const availableRules = (field: string, ...validators: Validator[]) => {
    if (!hasField(field)) {
        return []
    } else {
        return [
            (v: any) => v != null || "input cannot be empty",
            ...validators
        ]
    }
}

const avatarUrl = computed((): string | null => {
    if (uploadFile.value == null) {
        if (request.value.avatarId != null) {
            return findImage(request.value.avatarId)
        } else {
            return null
        }
    } else {
        return URL.createObjectURL(uploadFile.value)
    }
    
})

const cancelUpdate = () => {
    router.go(-1)    
}

const submitUserEdit = async () => {
    if (uploadFile.value != null) {
        let avatar = await uploadImage(uploadFile.value)
        request.value.avatarId = avatar.id
    }

    await store.updateUser(request.value)
}

onMounted(async () => {
    await store.reloadMissingData()

    if (store.currentUser != null) {
        request.value = {
            id: store.currentUser.id,
            name: store.currentUser.name,
            nickname: store.currentUser.nickname,
            email: store.currentUser.email,
            avatarId: store.currentUser.avatar?.id
        }
    }
})

</script>