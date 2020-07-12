<template>
    <div>
        <pageHeader title="Category View">category</pageHeader>
        <div class="row">
            <div class="col-md-6">
                <p>
                    <button v-on:click="add()" class="btn btn-white btn-default btn-round">
                        <i class="ace-icon fa fa-edit"></i>
                        新增一级
                    </button>
                    <button v-on:click="all()" class="btn btn-white btn-default btn-round">
                        <i class="ace-icon fa fa-refresh"></i>
                        刷新
                    </button>
                </p>

                <table id="level1-table" class="table  table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>名称</th>
                            <th>顺序</th>
                            <th>操作</th>
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
                    <button v-on:click="add_level2()" class="btn btn-white btn-default btn-round">
                        <i class="ace-icon fa fa-edit"></i>
                        新增二级
                    </button>
                </p>

                <table id="level2-table" class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>名称</th>
                            <th>顺序</th>
                            <th>操作</th>
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
                        <h4 class="modal-title">表单</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">父分类</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">{{active.name || "无"}}</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">名称</label>
                                <div class="col-sm-10">
                                    <input v-model="category.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">顺序</label>
                                <div class="col-sm-10">
                                    <input v-model="category.sort" class="form-control">
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
    import Pagination from '../../components/pagination.vue'
    import pageHeader from "../../components/pageHeader";
    export default {
        name: "business-category",
        components: {Pagination, pageHeader},
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

        },
        methods: {
            all(){
                let vm = this;
                Loading.show();
                vm.$ajax.post(vm.url + '/business/admin/category/all')
                .then((res) => {
                    Loading.hide();
                    let resp = res.data.content;
                    vm.categories = resp.list
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
            }
        }

    }
</script>

<style scoped>
    .active td {
        background-color: #d6e9c6 !important;
    }
</style>