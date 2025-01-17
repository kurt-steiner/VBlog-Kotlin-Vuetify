<template>
    <VContainer class="category-setting-view">
        <VTable fixed-header>
            <thead>
                <tr>
                    <th :colspan="3">
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
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
            </thead>

            <tbody>
                <template v-for="category in categories" :key="category.id">
                    <tr style="height: 100px">
                        <td width="10%"> {{ category.id }} </td>
                        <td width="30%"> {{ category.name }} </td>
                        <td> {{ category.createTime }}</td>
                        <td>
                            <VBtn color="primary" @click="toggleUpdate(category.id, category.name)">修改</VBtn>
                            <VBtn color="error" @click="toggleDelete(category.id)">删除</VBtn>
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
import type { PostCategoryRequest, PutCategoryRequest, Category } from '@/types';
import { notEmptyRule, maxLengthRule } from '@/utils';
import { onMounted, ref } from 'vue';
import { Row } from '@/components/layouts';

const store = useVBlogStore()

/* 
    由于 store 从后端获取 categories 没有进行分页，而是获得所有结果，这个时候数据就好操作了
    在搜索 category 时，不需要重新加载数据，不需要修改后端接口，直接从现有的 categories 中进行搜索即可
*/

const dialogActived = ref(false)
const dialogType = ref<"insert" | "update">("insert")
const searchContent = ref("")
const categories = ref<Category[]>([])

const putRequest = ref<PutCategoryRequest>({
    id: -1,
    name: ""
})

const postRequest = ref<PostCategoryRequest>({
    name: "",
    userId: -1
})

const toggleSearch = () => {
    categories.value = store.categories.filter(category => category.name.includes(searchContent.value))
}

const resetSearch = () => {
    searchContent.value = ""
    categories.value = store.categories
}

const toggleUpdate = (id: number, name: string) => {
    dialogActived.value = true
    dialogType.value = "update"
    putRequest.value = {
        id,
        name
    }
}

const toggleInsert = () => {
    dialogActived.value = true
    dialogType.value = "insert"
}

const toggleDelete = async (id: number) => {
    await store.deleteCategory(id)
}

const submitUpdate = async () => {
    await store.updateCategory(putRequest.value)
    dialogActived.value = false

    const index = categories.value.findIndex((element) => element.id == putRequest.value.id)
    categories.value[index] = store.categories.find(category => category.id == putRequest.value.id)!
}

const cancelOperation = () => {
    dialogActived.value = false
}

const submitInsert = async () => {
    await store.insertCategory(postRequest.value)
    categories.value = store.categories
    dialogActived.value = false
    postRequest.value.name = ""
}

onMounted(async () => {
    await store.reloadMissingData()
    categories.value = store.categories
    postRequest.value = {
        name: "",
        userId: store.currentUser!.id
    }
})
</script>

<style lang="scss" scoped>
td {
    text-overflow: ellipsis;
}
</style>