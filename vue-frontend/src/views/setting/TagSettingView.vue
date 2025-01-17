<template>
    <VContainer class="category-setting-view">
        <VTable fixed-header>
            <thead>
                <tr>
                    <th :colspan="2">
                        <VTextField v-model="searchContent" 
                                clearable
                                label="搜索"
                                hide-details
                                variant="outlined"/>
                    </th>

                    <th>
                        <VBtn color="primary" @click="toggleSearch">搜索</VBtn>
                        <VBtn @click="resetSearch">重置</VBtn>
                    </th>
                </tr>
                
                <tr>
                    <th>ID</th>
                    <th>名称</th>
                    <th>操作</th>
                </tr>
            </thead>

            <tbody>
                <template v-for="tag in tags" :key="tag.id">
                    <tr style="height: 100px">
                        <td width="10%"> {{ tag.id }} </td>
                        <td width="30%"> {{ tag.name }} </td>
                        <td>
                            <VBtn color="primary" @click="toggleUpdate(tag.id, tag.name)">修改</VBtn>
                            <VBtn color="error" @click="toggleDelete(tag.id)">删除</VBtn>
                        </td>
                    </tr>
                </template>
            </tbody>
        </VTable>
    </VContainer>

    <VDialog v-model="dialogActived" width="60%" height="50%">
        <VForm>
            <template #default="{ isValid }">
                <VCard>
                    <VCardTitle>
                        <template v-if="dialogType == 'insert'">
                            新增分类
                        </template>
                        <template v-else>
                            修改分类
                        </template>
                    </VCardTitle>

                    <VCardText>
                        <template v-if="dialogType == 'insert'">
                            <VTextField v-model="postRequest.name"
                                        clearable
                                        label="输入分类名称"
                                        :rules="[notEmptyRule, maxLengthRule(64)]"
                                        variant="outlined"/>
                        </template>

                        <template v-else>
                            <VTextField v-model="putRequest.name"
                                    clearable
                                    label="输入分类名称"
                                    :rules="[notEmptyRule, maxLengthRule(64)]"
                                    variant="outlined"/>
                        </template>
                    </VCardText>

                    <VCardActions>
                        <Row main-axis-size="max" main-axis-alignment="end" cross-axis-alignment="center">
                            <VBtn color="grey" @click="cancelOperation">取消</VBtn>
                            <VBtn color="primary"
                                  :disabled="!isValid.value" 
                                  @click="dialogType == 'insert' ? submitInsert() : submitUpdate()">确定</VBtn>
                        </Row>
                    </VCardActions>
                </VCard> 
            </template>
        </VForm>
        
    </VDialog>

    <VFab app
          :elevation="3"
          color="primary"
          icon="mdi-plus"
          @click="toggleInsert"/>

</template>

<script lang="ts" setup>
import { useVBlogStore } from '@/store';
import type { PostTagRequest, PutTagRequest, Tag } from '@/types';
import { onMounted, ref } from 'vue';
import { notEmptyRule, maxLengthRule } from '@/utils';
import { Row } from '@/components/layouts';

const store = useVBlogStore()
const tags = ref<Tag[]>([])

const dialogActived = ref(false)
const dialogType = ref<"insert" | "update">("insert")
const searchContent = ref("")
const postRequest = ref<PostTagRequest>({
    name: "",
    userId: -1
})
const putRequest = ref<PutTagRequest>({
    id: -1,
    name: ""
})


const toggleSearch = () => {
    if (searchContent.value.trim() == "") {
        tags.value = store.tags
    } else {
        tags.value = store.tags.filter(tag => tag.name.includes(searchContent.value))
    }
}

const resetSearch = () => {
    searchContent.value = ""
    tags.value = store.tags
}

const toggleUpdate = (id: number, name: string) => {
    putRequest.value = {
        id,
        name
    }

    dialogActived.value = true
    dialogType.value = "update"
}

const toggleDelete = async (id: number) => {
    await store.deleteTag(id)
    const index = tags.value?.findIndex((tag) => tag.id == id)
    tags.value?.splice(index, 1)
}

const cancelOperation = () => {
    dialogActived.value = false
}

const submitUpdate = async () => {
    await store.updateTag(putRequest.value)
    const index = tags.value.findIndex((tag) => tag.id == putRequest.value.id)
    tags.value[index] = store.tags.find(tag => tag.id == putRequest.value.id)!

    dialogActived.value = false
}

const submitInsert = async () => {
    await store.insertTag(postRequest.value)
    tags.value = store.tags
    dialogActived.value = false
    postRequest.value.name = ""
}

const toggleInsert = () => {
    dialogActived.value = true
    dialogType.value = "insert"
}

onMounted(async () => {
    await store.reloadMissingData()
    tags.value = store.tags
    postRequest.value.userId = store.currentUser!.id
})
</script>