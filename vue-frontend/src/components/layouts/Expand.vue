<template>
    <div class="expand" :style="style">
        <slot/>
    </div>
</template>

<script lang="ts" setup>
import type { StyleValue } from 'vue';

const props = withDefaults(
    defineProps<{
        flex?: number
    }>(),

    {
        flex: 1
    }
)

const style: StyleValue = {
    flexGrow: props.flex,
    flexShrink: 1,
    flexBasis: "auto"
}
</script>