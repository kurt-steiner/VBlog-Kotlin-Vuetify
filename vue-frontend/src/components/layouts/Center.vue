<template>
    <div class="center">
        <slot/>
    </div>
</template>

<style lang="scss" scoped>
div.center {
    display: flex;
    justify-content: center;
    align-items: center;

    width: 100%;
    height: 100%;
}
</style>