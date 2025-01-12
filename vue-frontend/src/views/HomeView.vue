<template>
    <VContainer class="home-view">
        <VCard class="search" width="100%" title="搜索">
            <template #subtitle>
                <VTextField v-model="query.title" 
                    clearable 
                    label="标题"
                    hide-details
                    variant="outlined"/>        
            </template>

            <template #text>
                <Row main-axis-size="max" cross-axis-alignment="center">
                    <VSelect v-model="query.status" 
                            :items="statuses"
                            clearable
                            label="状态"
                            item-title="name" 
                            item-value="value"
                            hide-details
                            variant="outlined"/>
                            
                    <VSelect v-model="query['category-id']" 
                            :items="store.categories" 
                            clearable
                            label="分类"
                            item-title="name" 
                            item-value="id"
                            hide-details
                            variant="outlined"/>

                    <VSelect v-model="query['tag-id']" 
                            :items="store.tags" 
                            clearable
                            label="标签"
                            item-title="name" 
                            item-value="id"
                            hide-details
                            variant="outlined"/>
                </Row>
                
            </template>

            <template #actions>
                <VBtn color="primary" icon="mdi-magnify" @click="submitSearch"/>
                <VBtn color="primary" icon="mdi-restore" @click="cancelSearch"/>
            </template>
        </VCard>
        

        <VDivider/>
        
        <template v-for="article in store.articleShortcuts" :key="article.id">
            <ArticleCard :id="article.id"
                         :title="article.title"
                         :author="article.author"
                         :publish-date="article.publishDate"
                         :edit-time="article.editTime"
                         :summary="article.summary"
                         :category="article.category"
                         :status="article.status"
                         :tags="article.tags"
                         @edit="handleEdit(article.id)"
                         @preview="handlePreview(article.id)"
                         @delete="handleDelete(article.id)"/>
        </template>

        <VPagination v-model="currentPage" :length="store.totalPages"
                     @first="handleFirst"
                     @last="handleLast"
                     @next="handleNext"
                     @prev="handlePrev"
                     next-icon="mdi-menu-right"
                     prev-icon="mdi-menu-left"/>
    </VContainer>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import useVBlogStore from '../store';
import ArticleCard from '../components/ArticleCard.vue';
import { Row } from '../components/layouts';
import { useRouter } from 'vue-router';
import { type ArticleQuery, ArticleStatus } from '../types';

const store = useVBlogStore()
const router = useRouter()

const statuses = [
    {
        name: "已发布",
        value: ArticleStatus.Published,
    },

    {
        name: "草稿",
        value: ArticleStatus.Draft,
    },

    {
        name: "回收站",
        value: ArticleStatus.Dustbin,
    },
]

const query = ref<ArticleQuery>({})

const currentPage = computed({
    get: () => store.page,
    set: async (value: number) => {
        store.page = value
        await store.loadArticles(query.value)
    }
})

const handleEdit = (id: number) => {
    router.push({path: `/edit/${id}`})
}

const handlePreview = (id: number) => {
    router.push({path: `/article/${id}`})
}

const handleDelete = async (id: number) => {
    await store.updateArticle({
        id: id,
        status: ArticleStatus.Dustbin
    })
}

const submitSearch = async () => {
    await store.loadArticles(query.value)
}

const cancelSearch = async () => {
    query.value = {}
    await store.loadArticles({})
}

const handleFirst = () => {
    store.page = 0
}

const handleLast = () => {
    store.page = store.totalPages - 1
}

const handleNext = () => {
    store.page += 1
}

const handlePrev = () => {
    store.page -= 1
}

onMounted(async () => {
    await store.reload()
})
</script>