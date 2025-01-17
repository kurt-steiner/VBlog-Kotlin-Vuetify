<template>
    <VApp>
        <VAppBar :elevation="10" color="primary">
            <VAppBarNavIcon @click="toggleDrawer"/>
            <VAppBarTitle>
                <template v-if="store.isLogined">
                    <VAvatar :image="findImage(store.currentUser?.avatar?.id!)"
                             :size="50"/>
                </template>

                VBlog 管理系统 (Kotlin + Vue3 + Vuetify)
            </VAppBarTitle>
            
            <template #append>
                <template v-if="route.name == 'Home'">
                    <VMenu>
                        <template #activator="{ props }">
                            <VBtn v-bind="props" icon="mdi-filter"/>
                        </template>

                        <VList>
                            <VListItem @click="switchSortBy(ArticleSortBy.ByEditTime)" title="编辑时间"/>
                            <VListItem @click="switchSortBy(ArticleSortBy.ByPublishDate)" title="发布时间"/>
                            <VListItem @click="switchSortBy(ArticleSortBy.ByTitle)" title="标题"/>
                        </VList>
                    </VMenu>
                </template>
                
                <template v-if="route.name == 'Article'">
                    <VBtn @click="toggleEdit" icon="mdi-pencil"/>
                </template>

                <VBtn @click="logout" icon="mdi-logout"/>
            </template>
        </VAppBar>

        <!-- TODO replace this width with vw -->
        <VNavigationDrawer v-model="drawerActived" 
                           :width="500"
                           location="left" 
                           :elevation="10" 
                           temporary>
            <template v-for="route in routes" :key="route.path">
                <VListItem nav :to="route.path">
                    <template #prepend>
                        <VIcon :icon="route.icon" size="large"/>
                    </template>

                    <VListItemTitle>{{ route.name }} </VListItemTitle>
                </VListItem>
            </template>
        </VNavigationDrawer>

        <VMain>
            <router-view/>
        </VMain>

        <VSnackbar v-model="snackbar.actived" 
                   color="error"
                   width="50%"
                   :height="60"
                   position="fixed"
                   location="top"
                   :text="snackbar.message"/>
    </VApp>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {useVBlogStore, useSnackbarStore} from './store';
import { findImage } from './api';
import { ArticleSortBy } from './types';

const store = useVBlogStore()
const snackbar = useSnackbarStore()
const route = useRoute()
const router = useRouter()
const routes = [
    { path: "/", name: "主页", icon: "mdi-home" },
    { path: "/post-article", name: "发表文章", icon: "mdi-application-edit-outline"},
    { path: "/setting", name: "设置", icon: "mdi-cog-outline"}
]

const drawerActived = ref(false)

const sortBy = ref(ArticleSortBy.ByEditTime)
const toggleDrawer = () => {
    drawerActived.value = !drawerActived.value
}

const switchSortBy = async (value: ArticleSortBy): Promise<void> => {
    sortBy.value = value
    await store.loadArticles({ "sort-by": value})
}

const toggleEdit = () => {
    router.replace({ path: `/edit-article/${store.currentArticle!.id}`})
}

const logout = () => {
    store.logout()
    router.replace({ name: "Login" })
}

onMounted(async () => {
    await store.reloadUser()
})
</script>