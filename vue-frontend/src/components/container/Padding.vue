<template>
    <div class="padding" :style="style">
        <slot/>
    </div>
</template>

<script lang="ts" setup>
import { computed, type StyleValue } from 'vue';

const props = withDefaults(
    defineProps<{
        left?: number,
        right?: number,
        top?: number,
        bottom?: number,
    }>(),

    {
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
    }
)

const style = computed((): StyleValue => {
    return {
        paddingLeft: `${props.left}px`,
        paddingRight: `${props.right}px`,
        paddingTop: `${props.top}px`,
        paddingBottom: `${props.bottom}px`
    }
})
</script>