<template>
    <VContainer class="post-article-view">
        <VForm>
            <template #default="{ isValid }">
                <VCard width="100%">
                    <VCardTitle>
                        <VTextField label="标题"
                                    prepend-inner-icon="mdi-format-title" 
                                    v-model="request.title" 
                                    variant="outlined" 
                                    :rules="[notEmptyRule, maxLengthRule(256)]"/>
                    </VCardTitle>
                
                    <VCardSubtitle>
                        <Row main-axis-size="max" cross-axis-alignment="center">
                            <VSelect v-model="request.categoryId" 
                                    width="30%"
                                    prepend-inner-icon="mdi-format-list-bulleted-type"
                                    label="分类"
                                    hide-details 
                                    density="compact" 
                                    variant="outlined"
                                    :items="store.categories"
                                    item-title="name"
                                    item-value="id"/>

                            
                            <VSelect v-model="request.tags!"
                                    width="70%"
                                    prepend-inner-icon="mdi-tag-outline"
                                    label="标签" 
                                    multiple 
                                    hide-details 
                                    chips 
                                    density="compact"
                                    variant="outlined"
                                    :items="store.tags"
                                    return-object
                                    item-title="name">
                                <template #prepend-item>
                                    <VListItem>
                                        <VTextField v-model="postTagRequest.name"
                                                variant="outlined"
                                                label="输入标签名"
                                                density="compact"
                                                @keyup.enter="handlePostTag"
                                                @keydown.enter.stop=""
                                                :rules="[notEmptyRule, maxLengthRule(64)]"/>
                                    </VListItem>
                                    
                                </template>
                            </VSelect>
                        </Row>
                    </VCardSubtitle> 

                    <VCardText>
                        <MdEditor v-model="request.markdownContent"
                                @on-html-changed="handleHtmlChange"
                                @on-upload-img="handleUploadImage"/>
                    </VCardText>
                    
                    <VCardActions> 
                        <Row main-axis-size="max" cross-axis-alignment="center" main-axis-alignment="end">
                            <VBtn color="grey" variant="elevated" 
                                  @click="cancelOperation" 
                                  text="取消"/>

                            <Expand/>

                            <VBtn class="mr-4" color="success" 
                                  variant="elevated" 
                                  @click="submitArticle(ArticleStatus.Draft)" 
                                  text="草稿"
                                  :disabled="!isValid.value"/>

                            <VBtn color="blue" variant="elevated" 
                                  @click="submitArticle(ArticleStatus.Published)" 
                                  text="发布"
                                  :disabled="!isValid.value || isContentEmpty"/>
                        </Row>
                    </VCardActions>
                </VCard>
            </template>
        </VForm>
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
import { MdEditor } from 'md-editor-v3';
import "md-editor-v3/lib/style.css"
import { computed, onMounted, ref } from 'vue';
import { ArticleStatus, type PostArticleRequest, type PostTagRequest, type Response } from '@/types';
import {useVBlogStore} from '@/store';
import { findImage, uploadImage } from '@/api';
import { Row, Expand } from '@/components/layouts';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { notEmptyRule, maxLengthRule } from '@/utils';


const store = useVBlogStore()
const router = useRouter()

const alertActived = ref(false)
const errorMessage = ref("")

const request = ref<PostArticleRequest>({
    title: "",
    markdownContent: "# Hello World",
    htmlContent: "<h1> Hello World </h1>",
    status: ArticleStatus.Draft,
    authorId: store.currentUser!.id
})

const postTagRequest = ref<PostTagRequest>({
    name: "",
    userId: store.currentUser!.id
})



const handleHtmlChange = (html: string) => {
    request.value.htmlContent = html
}

const isContentEmpty = computed(() => request.value.markdownContent.length == 0)
const handleUploadImage = async (files: File[], callback: (urls: string[] | { url: string; alt: string; title: string }[]) => void) => {
    let result = files.map((file) => {
        return uploadImage(file)
    })

    let imageitems = await Promise.all(result)
    callback(imageitems.map((item) => findImage(item.id)))
}

const handlePostTag = async () => {
    if (postTagRequest.value.name.length == 0) {
        return
    }

    await store.insertTag(postTagRequest.value)
    postTagRequest.value.name = ""
}

const cancelOperation = () => {
    router.go(-1)
}

const submitArticle = async (status: ArticleStatus) => {
    request.value.status = status

    try {
        await store.insertArticle(request.value)
        router.replace({name: "Home"})
    } catch (error: any) {
        alertActived.value = true

        if (axios.isAxiosError(error)) {
            errorMessage.value = (error.response?.data as Response<string>).message
        } else {
            errorMessage.value = `${error}`
        }
    }
}

onMounted(async () => {
    await store.reloadMissingData()
})
</script>