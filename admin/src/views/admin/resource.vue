<template>
    <div>
        <p>
            <button v-on:click="list()" class="btn btn-white btn-default btn-round">
                <i class="ace-icon fa fa-refresh"></i>
                Refresh
            </button>
        </p>
        <div class="row">
            <div class="col-md-6">
                <textarea id="resource-textarea" class="form-control" v-model="resourceStr" name="resource" rows="10"></textarea>

                <br>
                <button id="save-btn" type="button" class="btn btn-primary" v-on:click="save()">
                    Save
                </button>
            </div>
            <div class="col-md-6">
                <ul id="tree" class="ztree"></ul>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: "system-resource",
        data: function(){
            return {
                resource: {},
                resources: [],
                resourceStr: "",
                tree: {},
                url: process.env.VUE_APP_SERVER
            }
        },
        mounted() {
            this.list();
        },
        methods: {
            list(){
                let vm = this;
                Loading.show();
                vm.$ajax.get(vm.url + '/system/admin/resource/load-tree')
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    vm.resources = resp.content;
                    vm.initTree();
                })
            },
            del(id){
              let vm = this;
              Confirm.show('Are you sure?', ()=>{
                  Loading.show();
                  vm.$ajax.delete(vm.url + '/system/admin/resource/delete/' + id)
                  .then((res) => {
                      Loading.hide();
                      if(res.data.success){
                          vm.list();
                          Toast.success('Success!');
                      }
                  })
              })
            },
            save(){
              let vm = this;
              if(Tool.isEmpty(vm.resourceStr)){
                  Toast.warning('Resource can not be empty');
                      return;
              }
              let json = JSON.parse(vm.resourceStr);
              Loading.show();
              vm.$ajax.post(vm.url + '/system/admin/resource/save', json)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        vm.list();
                        Toast.success("Success!");
                    }else{
                        Toast.warning(resp.message);
                    }
                })
            },
            initTree(){
                let vm = this;
                let setting = {
                    data: {
                        simpleData: {
                            idKey: "id",
                            pIdKey: "parent",
                            rootPId: ""
                        }
                    }
                };
                vm.zTree = $.fn.zTree.init($("#tree"), setting, vm.resources);
                vm.zTree.expandAll(true);
            }
        }
    }
</script>

<style scoped>

</style>
