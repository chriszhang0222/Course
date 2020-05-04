<template>
    <div>
    <p>
        <button v-on:click="add()" class="btn btn-app btn-success">
            <i class="ace-icon fa fa-edit"></i>
            New
        </button>
        &nbsp;
        <button v-on:click="list(1)" class="btn btn-app btn-success">
            <i class="ace-icon fa fa-refresh"></i>
            Reload
        </button>
    </p>
        <pagination ref="pagination" v-bind:list="list"></pagination>
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
        <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Form</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Name</label>
                                <div class="col-sm-10">
                                    <input v-model="chapter.name" class="form-control" placeholder="Name">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Course</label>
                                <div class="col-sm-10">
                                    <input v-model="chapter.courseId" class="form-control" placeholder="Course">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button v-on:click="save()" type="button" class="btn btn-primary">Save</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
    </div>
</template>

<script>
    import Pagination from '../../components/pagination.vue'
    export default {
        components: {Pagination},
        name: "chapter",
        data: function () {
            return {
                chapters:[],
                chapter:{}
            }
        },
        mounted:function(){

            let _this = this;
            _this.list()
        },
        methods: {
            add(){
                let _this = this;
                _this.chapter = {};
                $(".modal").modal("show");
            },
            list(page){
                let _this = this;
                //TODO: refactor url to gateway
                _this.$ajax.post('http://127.0.0.1:9002/business/chapter/list', {
                    page:page,
                    size:_this.$refs.pagination.size
                })
                    .then((res) => {
                        console.log(res);
                        _this.chapters = res.data.content.list;
                        _this.$refs.pagination.render(page, res.data.content.total)
                    })
            },
            save(){
                let _this = this;
                _this.$ajax.post('http://127.0.0.1:9002/business/chapter/save', _this.chapter)
                    .then((res) => {
                        console.log(res);
                        if(res.data.success){
                            $("#form-modal").modal("hide");
                            _this.list(1)
                        }
                    })
            },
            edit(chapter){
                console.log("click edit");
                let _this = this;
                _this.chapter = $.extend({},chapter);
                $("#form-modal").modal("show");
            },
            del(id){
                let _this = this;
                _this.$ajax.delete('http://127.0.0.1:9002/business/chapter/delete/'+id)
                    .then((res) => {
                        console.log(res);
                        let resp = res.data
                        if(resp.success){
                            _this.list(1)
                        }
                    })
            }
        }
    }
</script>

<style scoped>

</style>