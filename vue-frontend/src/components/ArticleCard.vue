<template>
    <VCard class="article-card" width="100%" rounded="lg" hover :title="title">
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
            <VBtn icon="mdi-delete" color="error" @click="toggleDelete"/>
        </VCardActions>
    </VCard>
</template>

<script lang="ts" setup>
import { type ArticleShortcut } from '../types';

defineProps<ArticleShortcut>()

const emits = defineEmits<{
    (e: "edit"): void,
    (e: "preview"): void,
    (e: "delete"): void
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
</script>