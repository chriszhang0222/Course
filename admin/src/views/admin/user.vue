<template>
    <div>
        <p>
            <button v-on:click="add()" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-edit"></i>
                New User
            </button>

            <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-refresh"></i>
                Refresh
            </button>
        </p>
        <pagination ref="pagination" v-bind:list="list"
                    v-bind:item-count="8">

        </pagination>
        <table id="simple-table" class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>LoginName</th>
                    <th>NickName</th>
                    <th>Password</th>
                    <th>Operation</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users">
                    <td>{{user.id}}</td>
                    <td>{{user.loginName}}</td>
                    <td>{{user.name}}</td>
                    <td>{{user.password}}</td>
                    <td>
                        <div class="hidden-sm hidden-xs btn-group">
                            <button v-on:click="editPassword(user)" class="btn btn-xs btn-info">
                                <i class="ace-icon fa fa-key bigger-120"></i>
                            </button>
                            <button v-on:click="edit(user)" class="btn btn-xs btn-info">
                                <i class="ace-icon fa fa-pencil bigger-120"></i>
                            </button>
                            <button v-on:click="del(user.id)" class="btn btn-xs btn-danger">
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
                        <h4 class="modal-title">form</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">LoginName</label>
                                <div class="col-sm-10">
                                    <input v-model="user.loginName" v-bind:disabled="user.id" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">NickName</label>
                                <div class="col-sm-10">
                                    <input v-model="user.name" class="form-control">
                                </div>
                            </div>
                            <div v-show="!user.id" class="form-group">
                                <label class="col-sm-2 control-label">Password</label>
                                <div class="col-sm-10">
                                    <input v-model="user.password" type="password" class="form-control">
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
        </div><!-- /.modal -->

        <div id="edit-password-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Change Password</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-2">Password</label>
                                <div class="col-sm-10">
                                    <input class="form-control" type="password" v-model="user.password" name="password">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
                            <i class="ace-icon fa fa-times"></i>
                            Cancel
                        </button>
                        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="savePassword()">
                            <i class="ace-icon fa fa-plus blue"></i>
                            Save
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </div>
</template>

<script>
    import Pagination from '../../components/pagination';
    export default {
        name: "user",
        components: {Pagination},
        data: function(){
          return {
              user: {},
              users: [],
              url: process.env.VUE_APP_SERVER
          }
        },
        mounted() {
            this.$refs.pagination.size = 5;
            this.list(1);
        },
        methods: {
            add(){
              this.user = {};
              $('#form-modal').modal('show');
            },
            edit(user){
              this.user = $.extend({}, user);
              $('#form-modal').modal('show');
            },
            list(page){
                let vm = this;
                Loading.show();
                vm.$ajax.post(vm.url + '/system/admin/user/list', {
                    page: page,
                    size: vm.$refs.pagination.size
                }).then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    vm.users = resp.content.list;
                    vm.$refs.pagination.render(page, resp.content.total);
                })
            },
            save(){
              let vm = this;
              if(!Validator.require(vm.user.loginName, 'LoginName')
              || !Validator.length(vm.user.loginName, 'LoginName', 1, 50)
              || !Validator.length(vm.user.name, 'nickname', 1, 50)
              || !Validator.require(vm.user.password, 'password')){
                  return;
              }
              vm.user.password = hex_md5(vm.user.password + KEY);
              Loading.show();
              vm.$ajax.post(vm.url + '/system/admin/user/save', vm.user)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        $('#form-modal').modal('hide');
                        vm.list(1);
                        Toast.success('Saved Successfully!');
                    }else{
                        Toast.warning(resp.message);
                    }
                })

            },
            del(id){
                let vm = this;
                Confirm.show('Are you sure?', function(){
                    Loading.show();
                    vm.$ajax.delete(vm.url + '/system/admin/user/delete/' + id)
                    .then((res) => {
                        Loading.hide();
                        let resp = res.data;
                        if(resp.success){
                            vm.list(1);
                            Toast.success('Delete Successfully!');
                        }
                    })
                })
            },
            editPassword(user){
              this.user = $.extend({}, user);
              this.user.password = null;
              $('#edit-password-modal').modal('show');
            },
            savePassword(){
              let vm = this;
              vm.user.password = hex_md5(vm.user.password + KEY);
              Loading.show();
              vm.$ajax.post(vm.url + '/system/admin/user/save-password', vm.user)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        $('#edit-password-modal').modal('hide');
                        vm.list(1);
                        Toast.success('Saved successfully!');
                    }else{
                        Toast.warning(resp.message);
                    }
                })
            },
        }
    }
</script>

<style scoped>

</style>
