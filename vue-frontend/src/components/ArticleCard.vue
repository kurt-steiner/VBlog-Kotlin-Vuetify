<template>
    <VCard class="article-card mb-1" width="100%" rounded="lg" hover :title="title">
        <template #append v-if="category !== undefined">
            <VChip border variant="elevated"> {{ category.name }} </VChip>
        </template>

        <VCardSubtitle>
            <template v-for="tag in tags" :key="tag.id">
                <VChip> {{ tag.name }}</VChip>
            </template>
        </VCardSubtitle>

        <VCardText>
            {{ summary }}
        </VCardText>

        <VCardActions>
            <VBtn icon="mdi-pencil" @click="toggleEdit"/>
            <VBtn icon="mdi-eye-arrow-left-outline" @click="togglePreview"/>

            <template v-if="status == ArticleStatus.Published">
                <VBtn icon="mdi-delete" color="error" @click="toggleDelete"/>
            </template>

            <template v-if="status == ArticleStatus.Dustbin">
                <VBtn icon="mdi-restore" color="success" @click="toggleRestore"/>
                <VBtn icon="mdi-delete-forever-outline" color="error" @click="toggleDeleteForever"/>
            </template>

            <template v-if="status == ArticleStatus.Draft">
                <VBtn icon="mdi-delete" color="error" @click="toggleDelete"/>
                <VBtn icon="mdi-newspaper" color="success" @click="togglePublish"/>
            </template>
        </VCardActions>
    </VCard>
</template>

<script lang="ts" setup>
import { ArticleStatus, type ArticleShortcut } from '@/types';

defineProps<ArticleShortcut>()

const emits = defineEmits<{
    (e: "edit"): void,
    (e: "preview"): void,
    (e: "delete"): void,
    (e: "restore"): void,
    (e: "delete-forever"): void,
    (e: "publish"): void
}>()

const toggleEdit = () => {
    emits("edit")
}

const togglePreview = () => {
    emits("preview")
}

const toggleDelete = () => {
    emits("delete")
}

const togglePublish = () => {
    emits("publish")
}

const toggleRestore = () => {
    emits("restore")
}

const toggleDeleteForever = () => {
    emits("delete-forever")
}
</script>