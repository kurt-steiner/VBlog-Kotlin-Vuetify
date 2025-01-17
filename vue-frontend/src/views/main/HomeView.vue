<template>
    <VContainer class="home-view">
        <VCard class="search" width="100%">
            <template #title>
                <Row main-axis-size="max" cross-axis-alignment="center">
                    <VTextField v-model="query.title" 
                                clearable
                                width="70%" 
                                label="标题"
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
                <Row main-axis-size="max" main-axis-alignment="end" cross-axis-alignment="center">
                    <VSwitch label="倒序" v-model="query.reverse" hide-details color="primary"/>
                    <VBtn color="primary" icon="mdi-magnify" @click="submitSearch"/>
                    <VBtn color="primary" icon="mdi-restore" @click="cancelSearch"/>
                </Row>
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
                        @delete="handleDelete(article.id)"
                        @restore="handleRestore(article.id)"
                        @delete-forever="handleDeleteForever(article.id)"
                        @publish="handlePublish(article.id)"/>
        </template>

        <VPagination v-model="currentPage" :length="store.totalPages"
                     @first="handleFirst"
                     @last="handleLast"
                     @next="handleNext"
                     @prev="handlePrev"
                     next-icon="mdi-menu-right"
                     prev-icon="mdi-menu-left"/>
    </VContainer>

    <VFab app
          :elevation="3"
          location="bottom right"
          color="primary"
          @click="togglePostArticle"
          icon="mdi-plus"/>

    <VBottomNavigation color="primary" absolute density="compact" v-model="query.status!">
        <template v-for="(status, index) in allStatus" :key="index">
            <VBtn @click="handleSwitchStatus">
                <VIcon :icon="status.icon"/>
                {{ status.text }} 
            </VBtn>
        </template>
    </VBottomNavigation>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import {useVBlogStore} from '@/store';
import ArticleCard from '@/components/ArticleCard.vue';
import { Row } from '@/components/layouts';
import { useRouter } from 'vue-router';
import { type ArticleQuery, ArticleStatus } from '@/types';

const store = useVBlogStore()
const router = useRouter()

// 顺序千万不要搞错
const allStatus = [
    {
        value: ArticleStatus.Draft,
        text: '草稿箱',
        icon: "mdi-inbox"
    },

    {
        value: ArticleStatus.Published,
        text: '已发布',
        icon: "mdi-newspaper-variant-multiple"
    },
    
    {
        value: ArticleStatus.Dustbin,
        text: '垃圾箱',
        icon: "mdi-trash-can-outline"
    }
]

const query = ref<ArticleQuery>({
    status: ArticleStatus.Published
})

const currentPage = computed({
    get: () => store.page,
    set: async (value: number) => {
        store.page = value
        await store.loadArticles(query.value)
    }
})

const handleEdit = async (id: number) => {
    await store.loadArticle(id)
    router.push({path: `/edit-article/${id}`})
}

const handlePreview = async (id: number) => {
    await store.loadArticle(id)
    router.push({path: `/article/${id}`})
}

const handleDelete = async (id: number) => {
    await store.updateArticle({
        id,
        status: ArticleStatus.Dustbin
    })
}

const handleRestore = async (id: number) => {
    await store.updateArticle({
        id,
        status: ArticleStatus.Draft
    })
}

const handleDeleteForever = async (id: number) => {
    await store.deleteArticle(id)
}

const handlePublish = async (id: number) => {
    await store.updateArticle({
        id,
        status: ArticleStatus.Published
    })
}

const submitSearch = async () => {
    await store.loadArticles(query.value)
}

const cancelSearch = async () => {
    query.value = {status: ArticleStatus.Published}
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

const togglePostArticle = () => {
    store.currentArticle = null
    router.push({name: "PostArticle"})
}

const handleSwitchStatus = async () => {
    await store.loadArticles(query.value)
}

onMounted(async () => {
    await store.reloadMissingData(query.value.status)
})
</script>