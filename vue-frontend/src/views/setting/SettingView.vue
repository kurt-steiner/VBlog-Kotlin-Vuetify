<!-- user setting, category manage and tag manage -->
<template>
    <VContainer class="setting-view">
        <VTabs v-model="tab" color="deep-purple-accent-4" class="mb-10">
            <VTab value="user">
                <VIcon icon="mdi-account"/>
                用户设置
            </VTab>

            <VTab value="category">
                <VIcon icon="mdi-format-list-bulleted-type"/>
                分类设置
            </VTab>

            <VTab value="tag">
                <VIcon icon="mdi-tag"/>
                标签设置
            </VTab>
        </VTabs>

        <VTabsWindow v-model="tab">
            <VTabsWindowItem value="user">
                <UserSettingView/>
            </VTabsWindowItem>

            <VTabsWindowItem value="category">
                <CategorySettingView/>
            </VTabsWindowItem>

            <VTabsWindowItem value="tag">
                <TagSettingView/>
            </VTabsWindowItem>
        </VTabsWindow>
    </VContainer>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import UserSettingView from './UserSettingView.vue';
import CategorySettingView from './CategorySettingView.vue';
import TagSettingView from './TagSettingView.vue';

const tab = ref<"user" | "category" | "tag">("user")
</script>