<template>
    <VFadeTransition>
        <VContainer class="edit-article-view" v-if="request != undefined">
            <VForm>
                <template #default="{ isValid }">
                    <VCard width="100%">
                        <VCardTitle>
                            <VTextField label="标题"
                                        prepend-inner-icon="mdi-format-title" 
                                        v-model="request!.title" 
                                        variant="outlined" 
                                        :rules="[notEmptyRule, maxLengthRule(256)]"/>
                        </VCardTitle>
                    
                        <VCardSubtitle>
                            <Row main-axis-size="max" cross-axis-alignment="center">
                                <VSelect v-model="request!.categoryId" 
                                        width="30%"
                                        prepend-inner-icon="mdi-format-list-bulleted-type"
                                        label="分类"
                                        hide-details 
                                        density="compact" 
                                        variant="outlined"
                                        :items="store.categories"
                                        item-title="name"
                                        item-value="id"/>

                                
                                <VSelect v-model="request!.tags"
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
                            <MdEditor v-model="request!.markdownContent"
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
    </VFadeTransition>
</template>

<script lang="ts" setup>
import { MdEditor } from 'md-editor-v3';
import "md-editor-v3/lib/style.css"
import { onMounted, ref, computed } from 'vue';
import { ArticleStatus, type PostTagRequest, type PutArticleRequest } from '@/types';
import {useVBlogStore} from '@/store';
import { useRoute, useRouter } from 'vue-router';
import { findImage, uploadImage } from '@/api';
import { Expand, Row } from '@/components/layouts';
import { notEmptyRule, maxLengthRule } from '@/utils';

const store = useVBlogStore()
const route = useRoute()
const router = useRouter()

const request = ref<PutArticleRequest>()

const postTagRequest = ref<PostTagRequest>({
    name: "",
    userId: store.currentUser!.id
})

const handleHtmlChange = (html: string) => {
    request.value!.htmlContent = html
}

const handlePostTag = async () => {
    if (postTagRequest.value.name.length == 0) {
        return
    }

    await store.insertTag(postTagRequest.value)
    postTagRequest.value.name = ""
}

const isContentEmpty = computed(
    () => request.value?.markdownContent?.length == 0
)
const handleUploadImage = async (files: File[], callback: (urls: string[] | { url: string; alt: string; title: string }[]) => void) => {
    let result = files.map((file) => {
        return uploadImage(file)
    })

    let imageitems = await Promise.all(result)
    callback(imageitems.map((item) => findImage(item.id)))
}

const cancelOperation = () => {
    router.go(-1)
}

const submitArticle = async (status: ArticleStatus) => {
    request.value!.status = status
    await store.updateArticle(request.value!)
    router.replace({name: "Home"})
}

onMounted(async () => {
    const id = parseInt(route.params.id as string)

    if (store.currentArticle == null || store.currentArticle?.id != id) {
        await store.loadArticle(id)
    }
    
    await store.reloadMissingData()

    const currentArticle = store.currentArticle!
    request.value = {
        id: currentArticle.id,
        title: currentArticle.title,
        markdownContent: currentArticle.markdownContent,
        htmlContent: currentArticle.htmlContent,
        categoryId: currentArticle.category?.id,
        tags: currentArticle.tags,
        status: currentArticle.status
    }
})
</script>