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

        <div id="resource-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Role-Resource link settings</h4>
                    </div>
                    <div class="modal-body">
                        <ul id="tree" class="ztree"></ul>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
                            <i class="ace-icon fa fa-times"></i>
                            Close
                        </button>
                        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="saveResource()">
                            <i class="ace-icon fa fa-plus blue"></i>
                            Save
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>

        <div id="user-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Role-User link settings</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <table id="user-table" class="table table-hover">
                                    <tbody>
                                    <tr v-for="user in users">
                                        <td>{{user.loginName}}</td>
                                        <td class="text-right">
                                            <a v-on:click="addUser(user)" href="javascript:;" class="">
                                                <i class="ace-icon fa fa-arrow-circle-right blue"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col-md-6">
                                <table id="role-user-table" class="table table-hover">
                                    <tbody>
                                    <tr v-for="user in roleUsers">
                                        <td>{{user.loginName}}</td>
                                        <td class="text-right">
                                            <a v-on:click="deleteUser(user)" href="javascript:;" class="">
                                                <i class="ace-icon fa fa-trash blue"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
                            <i class="ace-icon fa fa-times"></i>
                            close
                        </button>
                        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="saveUser()">
                            <i class="ace-icon fa fa-plus blue"></i>
                            save
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
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
            this.$refs.pagination.size = 5;
            this.list(1);
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
            editResource(role){
              this.role = $.extend({}, role);
              this.loadResource();
              $('#resource-modal').modal('show');
            },
            loadResource(){
              let vm = this;
              Loading.show();
              vm.$ajax.get(vm.url + '/system/admin/resource/load-tree')
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    vm.resources = resp.content;
                    vm.initTree();
                    vm.listRoleResource();
                })
            },
            initTree(){
              let setting = {
                  check: {
                      enable: true
                  },
                  data: {
                      simpleData: {
                          idKey: "id",
                          pIdKey: "parent",
                          rootPId: "",
                          enable: true
                      }
                  }
              };
              this.zTree = $.fn.zTree.init($('#tree'), setting, this.resources);
              this.zTree.expandAll(true);
            },
            listRoleResource(){
                let vm = this;
                vm.$ajax.get(vm.url + '/system/admin/role/list-resource/' + vm.role.id)
                .then((res) => {
                   let resp = res.data;
                   let resources = resp.content;
                   vm.zTree.checkAllNodes(false);
                   for(let i=0;i<resources.length;i++){
                       let node = vm.zTree.getNodeByParam("id", resources[i]);
                       vm.zTree.checkNode(node, true);
                   }
                });
            },
            saveResource(){
                let vm = this;
                let resources = vm.zTree.getCheckedNodes();

                let resourceIds = [];
                for(let i=0;i<resources.length;i++){
                    resourceIds.push(resources[i].id);
                }
                vm.$ajax.post(vm.url + '/system/admin/role/save-resource',{
                    id: vm.role.id,
                    resourceIds: resourceIds
                }).then((res) => {
                    let resp = res.data;
                    if(resp.success){
                        Toast.success("Saved Successfully!");
                        $('#resource-modal').modal('hide');
                    }else{
                        Toast.warning(resp.message);
                    }
                });
            },
            editUser(role){
                this.role = $.extend({}, role);
                this.listUser();
                $('#user-modal').modal('show');
            },
            listUser(){
              let vm = this;
              vm.$ajax.post(vm.url + '/system/admin/user/list', {
                  page: 1,
                  size: 9999
              }).then((res) => {
                  let resp = res.data;
                  if(resp.success){
                      vm.users = resp.content.list;
                      vm.listRoleUser();
                  }else{
                      Toast.warning(resp.message);
                  }
              })
            },
            listRoleUser(){
              let vm = this;
              vm.roleUsers = [];
              vm.$ajax.get(vm.url + '/system/admin/role/list-user/' + vm.role.id)
                .then((res) => {
                    let resp = res.data;
                    let userIds = resp.content;

                    for(let i=0;i<userIds.length;i++){
                        for(let j=0;j<vm.users.length;j++){
                            if(vm.users[j].id === userIds[i]){
                                vm.roleUsers.push(vm.users[j]);
                            }
                        }
                    }
                })
            },
            addUser(user){
                for(let i=0;i<this.roleUsers.length;i++){
                    if(user === this.roleUsers[i]){
                        return;
                    }
                }
                this.roleUsers.push(user);
            },
            deleteUser(user){
                Tool.removeObj(this.roleUsers, user);
            },
            saveUser(){
                let users = this.roleUsers;
                let vm = this;
                let userIds = [];
                for(let i=0;i<users.length;i++){
                    userIds.push(users[i].id);
                }
                vm.$ajax.post(vm.url + '/system/admin/role/save-user', {
                    id: vm.role.id,
                    userIds: userIds
                }).then((res) => {
                    let resp = res.data;
                    if(resp.success){
                        Toast.success('Success!');
                    }else{
                        Toast.warning(resp.message);
                    }
                    $('#user-modal').modal('hide');
                })
            }
        }
    }
</script>

<style scoped>

</style>
