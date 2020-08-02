<template>
    <BigFile v-bind:input-id="inputId"
    v-bind:text="text"
    v-bind:suffixs="suffixs"
    v-bind:use="use"
    v-bind:url="'vod'"
    v-bind:shard-size="1024 * 1024 * 1024 * 10"
    v-bind:after-upload="afterUpload"
    >

    </BigFile>

</template>

<script>
    import BigFile from './big-file.vue';
    export default {
        components:{ BigFile },
        name: "vod",
        props:{
            text: {
                default: "Upload VOD"
            },
            inputId: {
                default: "vod-upload"
            },
            suffixs: {
                default: []
            },
            use: {
                default: ""
            },
            afterUpload: {
                type: Function,
                default: null
            },
        }
    }
</script>

<style scoped>

</style>
