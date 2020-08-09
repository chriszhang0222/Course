<template>
    <div>
        <p>
            <button v-on:click="add()" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-edit"></i>
                New
            </button>
            &nbsp;
            <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-refresh"></i>
                Refresh
            </button>
        </p>

        <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>

        <table id="simple-table" class="table  table-bordered table-hover">
            <thead>
            <tr>
                <th>id</th>
                <th>Role</th>
                <th>Description</th>
                <th>Operation</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="role in roles">
                <td>{{role.id}}</td>
                <td>{{role.name}}</td>
                <td>{{role.desc}}</td>
                <td>
                    <div class="hidden-sm hidden-xs btn-group">
                        <button v-on:click="editUser(role)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-user bigger-120"></i>
                        </button>
                        <button v-on:click="editResource(role)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-list bigger-120"></i>
                        </button>
                        <button v-on:click="edit(role)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-pencil bigger-120"></i>
                        </button>
                        <button v-on:click="del(role.id)" class="btn btn-xs btn-danger">
                            <i class="ace-icon fa fa-trash-o bigger-120"></i>
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
                                <label class="col-sm-2 control-label">Role</label>
                                <div class="col-sm-10">
                                    <input v-model="role.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Desc</label>
                                <div class="col-sm-10">
                                    <input v-model="role.desc" class="form-control">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                        <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>
    </div>
</template>

<script>
    import Pagination from "../../components/pagination";
    export default {
        components: {Pagination},
        name: "system-role",
        data: function(){
            return {
                role: {},
                roles: [],
                resources: [],
                zTree: {},
                users: [],
                roleUsers: [],
                url: process.env.VUE_APP_SERVER
            }
        },
        mounted() {
        },
        methods: {
            add(){
                this.role = {};
                $('#form-modal').modal('show');
            },
            edit(role){
                this.role = $.extend({}, role);
                $("#form-modal").modal("show");
            },
            list(page){
                let vm = this;
                Loading.show();
                vm.$ajax.post(vm.url + '/system/admin/role/list', {
                    page: page,
                    size: vm.$refs.pagination.size,
                }).then((res) => {
                    let resp = res.data;
                    Loading.hide();
                    this.roles = resp.content.list;
                    vm.$refs.pagination.render(page, resp.content.total);
                })
            },
            save(){
                let _this = this;

                // 保存校验
                if (1 != 1
                    || !Validator.require(_this.role.name, "角色")
                    || !Validator.length(_this.role.name, "角色", 1, 50)
                    || !Validator.require(_this.role.desc, "描述")
                    || !Validator.length(_this.role.desc, "描述", 1, 100)
                ) {
                    return;
                }
                Loading.show();
                _this.$ajax.post(_this.url + '/system/admin/role/save', _this.role)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        $('#form-modal').modal("hide");
                        _this.list(1);
                        Toast.success("Saved Successfully!");
                    }else{
                        Toast.warning(resp.message);
                    }
                })
            },
            del(id) {
                let _this = this;
                Confirm.show("Are you sure？", function () {
                    Loading.show();
                    _this.$ajax.delete(process.env.VUE_APP_SERVER + '/system/admin/role/delete/' + id).then((response)=>{
                        Loading.hide();
                        let resp = response.data;
                        if (resp.success) {
                            _this.list(1);
                            Toast.success("Success");
                        }
                    })
                });
            },
        }
    }
</script>

<style scoped>

</style>
