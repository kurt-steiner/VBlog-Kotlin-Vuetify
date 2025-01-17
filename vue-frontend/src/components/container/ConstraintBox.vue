<template>
    <div class="constraint-box" :style="style">
        <slot/>
    </div>
</template>

<script lang="ts" setup>
import { computed, type StyleValue } from 'vue';

const props = defineProps<{
       minWidth?: number,
       minHeight?: number,
       maxWidth?: number,
       maxHeight?: number
}>()

const style = computed((): StyleValue => {
    let result: StyleValue = {}

    if (props.minWidth !== undefined) {
        result["min-width"] = `${props.minWidth}px`
    }

    if (props.minHeight !== undefined) {
        result["min-height"] = `${props.minHeight}px`
    }

    if (props.maxWidth !== undefined) {
        result["max-width"] = `${props.maxWidth}px`
    }

    if (props.maxHeight !== undefined) {
        result["max-height"] = `${props.maxHeight}px`
    }

    return result
})


</script>