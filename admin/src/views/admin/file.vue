<template>
    <div>
        <pageHeader title="File View">File</pageHeader>
        <p>
            <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-refresh"></i>
                Refresh
            </button>
        </p>

        <pagination ref="pagination"
                    v-bind:list="list"
                    v-bind:item-count="8"
        ></pagination>
        <table id="simple-table" class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Path</th>
                    <th>File Name</th>
                    <th>Suffix</th>
                    <th>Size</th>
                    <th>Usage</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="file in files">
                    <td>{{file.id}}</td>
                    <td>{{file.path}}</td>
                    <td>{{file.name}}</td>
                    <td>{{file.suffix}}</td>
                    <td>{{file.size | formatFileSize}}</td>
                    <td>{{FILE_USE | optionKV(file.use)}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import pagination from '../../components/pagination.vue'
    import pageHeader from "../../components/pageHeader";
    export default {
        name: "file-file",
        components: {pagination, pageHeader},
        data: function(){
            return {
                file: {},
                files: [],
                FILE_USE: FILE_USE,
                url: 'http://127.0.0.1:9003'
            }
        },
        mounted() {
            let vm = this;
            vm.$refs.pagination.size = 5;
            vm.list(1);
        },
        methods:{
            list(page){
                let vm = this;
                Loading.show();
                vm.$ajax.post(vm.url + '/file/admin/list',{
                    page: page,
                    size: vm.$refs.pagination.size,
                }).then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    vm.files = resp.content.list;
                    vm.$refs.pagination.render(page, resp.content.total)
                })
            }
        }
    }
</script>

<style scoped>

</style>