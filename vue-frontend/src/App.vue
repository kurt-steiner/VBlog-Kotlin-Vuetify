<template>
    <VApp>
        <VAppBar :elevation="10" color="primary">
            <VAppBarNavIcon @click="toggleDrawer"/>
            <VAppBarTitle>
                <VAvatar v-if="store.isLogined">
                    <VImg :src="findImage(store.currentUser?.avatar?.id!)"/>
                </VAvatar>

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
                
                <VBtn @click="logout" icon="mdi-logout"/>
            </template>
        </VAppBar>

        <!-- TODO replace this width with vw -->
        <VNavigationDrawer v-model="drawerActived" 
                           :width="500"
                           location="left" 
                           :elevation="10" 
                           temporary>
            <template v-for="(route, index) in routes" :key="index">
                <VListItem nav :to="route.path" :prepend-icon="routeIcons(route.name! as string)">
                    <VListItemTitle>{{ route.name }} </VListItemTitle>
                </VListItem>
            </template>
        </VNavigationDrawer>

        <VMain>
            <router-view/>
        </VMain>
    </VApp>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import useVBlogStore from './store';
import { findImage } from './api';
import { ArticleSortBy } from './types';

const store = useVBlogStore()
const route = useRoute()
const router = useRouter()
const routes = router.getRoutes()
const drawerActived = ref(false)

const sortBy = ref(ArticleSortBy.ByEditTime)
const routeIcons = (name: string): string => {
    switch (name.toLowerCase()) {
        case "home": return "mdi-home"
        case "login": return "mdi-login"
        case "register": return "mdi-account-plus"
        case "category-manage": return "mdi-format-list-bulleted"
        case "tag-manage": return "mdi-format-list-bulleted"
        default: return "mdi-undefined"
    }
}

const toggleDrawer = () => {
    drawerActived.value = !drawerActived.value
}

const switchSortBy = async (value: ArticleSortBy): Promise<void> => {
    sortBy.value = value
    await store.loadArticles({ "sort-by": value})
}

const logout = () => {
    store.logout()
    router.replace({ name: "Login" })
}
</script>