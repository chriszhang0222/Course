<template>
    <div>
        <pageHeader title="Category View">category</pageHeader>
        <div class="row">
            <div class="col-md-6">
                <p>
                    <button v-on:click="add1()" class="btn btn-white btn-default btn-round">
                        <i class="ace-icon fa fa-edit"></i>
                        Add category Level 1
                    </button>
                    <button v-on:click="all()" class="btn btn-white btn-default btn-round">
                        <i class="ace-icon fa fa-refresh"></i>
                        Refresh
                    </button>
                </p>

                <table id="level1-table" class="table  table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Order</th>
                            <th>Operations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="category in level1" v-on:click="onClickLevel1(category)" v-bind:class="{'active': category.id === active.id}">
                            <td>{{category.id}}</td>
                            <td>{{category.name}}</td>
                            <td>{{category.sort}}</td>
                            <td>
                                <div class="hidden-sm hidden-xs btn-group">
                                    <button v-on:click="edit(category)" class="btn btn-xs btn-info">
                                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                                    </button>
                                    <button v-on:click="del(category.id)" class="btn btn-xs btn-danger">
                                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="col-md-6">
                <p>
                    <button v-on:click="add2()" class="btn btn-white btn-default btn-round">
                        <i class="ace-icon fa fa-edit"></i>
                        Add Category Level 2
                    </button>
                </p>

                <table id="level2-table" class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Order</th>
                            <th>Operation</th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="category in level2">
                            <td>{{category.id}}</td>
                            <td>{{category.name}}</td>
                            <td>{{category.sort}}</td>
                            <td>
                                <div class="hidden-sm hidden-xs btn-group">
                                    <button v-on:click="edit(category)" class="btn btn-xs btn-info">
                                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                                    </button>
                                    <button v-on:click="del(category.id)" class="btn btn-xs btn-danger">
                                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
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
                                <label class="col-sm-2 control-label">Parent Category</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">{{active.name || "None"}}</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Name</label>
                                <div class="col-sm-10">
                                    <input v-model="category.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Order</label>
                                <div class="col-sm-10">
                                    <input v-model="category.sort" class="form-control">
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
    import pagination from '../../components/pagination.vue'
    import pageHeader from "../../components/pageHeader";
    export default {
        name: "business-category",
        components: {pagination, pageHeader},
        data: function(){
            return {
                category: {},
                categories: {},
                level1: [],
                level2: [],
                active: {},
                url: process.env.VUE_APP_SERVER
            }
        },
        mounted() {
            let vm = this;
            vm.all();
        },
        methods: {
            add1(){
              let vm = this;
              vm.active = {};
              vm.level2 = [];
              vm.category = {
                  parent: "00000000"
              };
              $('#form-modal').modal('show');
            },
            add2(){
              let vm = this;
              if(Tool.isEmpty(vm.active)){
                  Toast.warning('Please select category level 1');
                  return;
              }
              vm.category = {
                  parent: vm.active.id
              };
              $('.modal').modal('show');
            },
            onClickLevel1(category){
                let vm = this;
                vm.active = category;
                let childern = category.children;
                this.level2 = childern;
            },
            all(){
                let vm = this;
                Loading.show();
                vm.$ajax.post(vm.url + '/business/admin/category/all')
                .then((res) => {
                    Loading.hide();
                    let resp = res.data
                    vm.categories = resp.content
                    vm.level1 = []
                    for(let i=0;i<vm.categories.length;i++){
                        let c = vm.categories[i];
                        if(c.parent === '00000000'){
                            vm.level1.push(c);
                            for(let j=0;j<vm.categories.length;j++){
                                let child = vm.categories[j];
                                if(child.parent == c.id){
                                    if(Tool.isEmpty(c.children)){
                                        c.children = [];
                                    }
                                    c.children.push(child)
                                }
                            }
                        }
                    }
                    vm.level2 = [];
                    setTimeout(()=>{
                        $("tr.active").trigger("click");
                    }, 100)
                })
            },
            del(id){
                let vm = this;
                Confirm.show('Are you sure you want to delete?', function(){
                   Loading.show();
                   vm.$ajax.delete(vm.url + '/business/admin/category/delete/' + id)
                    .then((res) => {
                        if(res.data.success){
                            vm.all();
                            Toast.success('Delete Successfully');
                        }
                    })
                });
            },
            save(){
                let vm = this;
                if(!Validator.require(vm.category.parent, 'parent id') || !Validator.require(vm.category.name, 'name') ||
                !Validator.require(vm.category.sort, 'order')){
                    return;
                }
                Loading.show();
                vm.$ajax.post(vm.url + '/business/admin/category/save', vm.category)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        $('#form-modal').modal('hide');
                        vm.all();
                        Toast.success('Save successfully!');
                        vm.category = {};
                    }else{
                        Toast.warning(resp.message);
                    }
                })
            },
            edit: function(category){
                let vm = this;
                vm.category = $.extend({}, category);
                $('#form-modal').modal('show');
            }
        }

    }
</script>

<style scoped>
    .active td {
        background-color: #bdecec !important;
    }
</style>