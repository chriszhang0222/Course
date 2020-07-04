<template>
    <div class="alert alert-block">
        <i class="ace-icon fa fa-check" v-bind:class="color"></i>
        Welcome to Ace.com
        <slot></slot>
    </div>
</template>

<script>
    export default {
        name: "alertBox",
        props: ['color']
    }
</script>
