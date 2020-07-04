<template>
    <div>
        <pageHeader title="Dashboard" content="overview"> Overview</pageHeader>
        <alertBox class="alert-success" color="green">
            Thank you for your visiting!
        </alertBox>
    </div>
</template>

<script>
    import alertBox from "../../components/alertBox";
    import pageHeader from "../../components/pageHeader";
    export default {
        name: "welcome",
        mounted:function(){

        },
        components:{
            alertBox,
            pageHeader
        }
    }
</script>

<style scoped>

</style>