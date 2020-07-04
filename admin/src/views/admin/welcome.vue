<template>
    <div>
        <h1>Hello</h1>
        <alertBox class="alert-success" color="green"></alertBox>
    </div>
</template>

<script>
    import alertBox from "../../components/alertBox";
    export default {
        name: "welcome",
        mounted:function(){

        },
        components:{
            alertBox
        }
    }
</script>

<style scoped>

</style>