<template>
    <div class="row" :style="createStyle">
        <slot/>
    </div>
</template>

<script lang="ts" setup>
import type { StyleValue } from 'vue';
import {computed} from "vue";
import { alignmentMap, type Props } from ".";

const props = withDefaults(defineProps<Props>(), {
    mainAxisSize: "min",
    mainAxisAlignment: "start",
    crossAxisAlignment: "start",
    wrap: false
})

const createStyle = computed((): StyleValue => {
    let size = ""

    if (props.mainAxisSize === "max") {
        size = "100%"
    } else {
        size = "fit-content"
    }

    return {
        "display": "flex",
        "flex-direction": "row",
        "width": size,
        "flex-wrap": props.wrap ? "wrap" : "nowrap",
        "justify-content": alignmentMap[props.mainAxisAlignment],
        "align-items": alignmentMap[props.crossAxisAlignment],
    }
})
</script>