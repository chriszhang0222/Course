<template>
    <div class="alert alert-block">
        <button class="close" type="button" data-dismiss="alert">
            <i class="ace-icon fa fa-times"></i>
        </button>
        <i class="ace-icon fa fa-check" v-bind:class="color"></i>
        Welcome to Ace.com!
        <slot></slot>
    </div>
</template>

<script>
    export default {
        name: "alertBox",
        props: ['color']
    }
</script>
