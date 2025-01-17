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
</template>

<script lang="ts" setup>
import { computed, onMounted} from 'vue';
import {useVBlogStore} from "@/store";
import { useRoute } from 'vue-router';
const store = useVBlogStore()

const article = computed(() => store.currentArticle)
const route = useRoute()

onMounted(async () => {
    const id = parseInt(route.params.id as string)
    if (store.currentArticle == null || store.currentArticle?.id != id) {
        await store.loadArticle(id)
    }
})
</script>