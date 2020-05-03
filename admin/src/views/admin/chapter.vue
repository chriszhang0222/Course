<template>
    <div>
        <p>
    <button v-on:click="list()" href="#" class="btn btn-app btn-success">
        <i class="ace-icon fa fa-refresh"></i>
        Reload
    </button></p>

    <table id="simple-table" class="table  table-bordered table-hover">
                <thead>
                <tr>
                    <th class="detail-col">ID</th>
                    <th>Name</th>
                    <th>Operation</th>
                </tr>
                </thead>

                <tbody>

                <tr v-for="chapter in chapters">
                    <td>{{chapter.id}}</td>
                    <td>{{chapter.name}}</td>
                    <td>
                        <div class="hidden-sm hidden-xs btn-group">
                            <button v-on:click="toSection(chapter)" class="btn btn-white btn-xs btn-info btn-round">
                                小节
                            </button>&nbsp;
                            <button v-on:click="edit(chapter)" class="btn btn-white btn-xs btn-info btn-round">
                                Edit
                            </button>&nbsp;
                            <button v-on:click="del(chapter.id)" class="btn btn-white btn-xs btn-warning btn-round">
                                Delete
                            </button>
                        </div>
                    </td>
                </tr>

                </tbody>
            </table>
    </div>
</template>

<script>
    export default {
        name: "chapter",
        data: function () {
            return {
                chapters:[]
            }
        },
        mounted:function(){

            let _this = this;
            _this.list()
        },
        methods: {
            list(){
                let _this = this;
                //TODO: refactor url to gateway
                _this.$ajax.post('http://127.0.0.1:9002/business/chapter/list', {
                    page:1,
                    size:1
                })
                    .then((res) => {
                        console.log(res);
                        _this.chapters = res.data.list;
                    })
            }
        }
    }
</script>

<style scoped>

</style>