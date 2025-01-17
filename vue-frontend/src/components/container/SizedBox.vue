<template>
    <div class="sizedbox" :style="style">
        <slot/>
    </div>
</template>

<script lang="ts" setup>
import { computed, type StyleValue } from 'vue';

const props = defineProps<{
    width?: number,
    height?: number
}>()

const style = computed((): StyleValue => {
    let result: {width?: string, height?: string} = {}
    if (props.width !== undefined) {
        result["width"] = `${props.width}px`
    }

    if (props.height !== undefined) {
        result["height"] = `${props.height}px`
    }

    return result
})
</script>