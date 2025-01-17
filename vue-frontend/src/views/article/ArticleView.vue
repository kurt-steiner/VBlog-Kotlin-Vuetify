<template>
    <VFadeTransition>
        <VCard v-if="article !== null" :title="article?.title">

            <template #append v-if="article?.category !== null">
                <VChip border variant="elevated"> {{ article.category.name }} </VChip>
            </template>

            <VCardSubtitle>
                <template v-for="tag in article?.tags ?? []" :key="tag.id">
                    <VChip> {{ tag.name }}</VChip>
                </template>
            </VCardSubtitle>
            
            <VCardText v-html="article?.htmlContent"/>
        </VCard>
    </VFadeTransition>

    <VFab app
          :elevation="3"
          color="primary"
          icon="mdi-keyboard-backspace"
          @click="goBack"/>
</template>

<script lang="ts" setup>
import { computed, onMounted} from 'vue';
import {useVBlogStore} from "@/store";
import { useRoute, useRouter } from 'vue-router';
const store = useVBlogStore()

const article = computed(() => store.currentArticle)
const route = useRoute()
const router = useRouter()

const goBack = () => {
    router.go(-1)
}

onMounted(async () => {
    const id = parseInt(route.params.id as string)
    if (store.currentArticle == null || store.currentArticle?.id != id) {
        await store.loadArticle(id)
    }
})
</script>