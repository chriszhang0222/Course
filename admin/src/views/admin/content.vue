<template>
    <div>
        <h4 class="lighter">
            <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
            <router-link to="/business/course" class="pink">{{ course.name }}</router-link>
        </h4>
        <hr>
        <table id="file-table" class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Url</th>
                    <th>Size</th>
                    <th>Operation</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        <form class="form-horizontal">
            <div class="form-group">
                <div class="col-lg-12">
                    {{ saveContentLabel }}
                </div>
            </div>
            <div class="form-group">
                <div class="col-lg-12">
                    <div id="content"></div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-lg-12">
                    {{ saveContentLabel }}
                </div>
            </div>
        </form>
        <p>
            <button type="button" class="btn btn-success btn-info btn-round" v-on:click="saveContent">
                <i class="ace-icon fa fa-plus blue"></i>
                Save
            </button>&nbsp;
            <router-link to="/business/course" type="button" class="btn btn-info btn-default btn-round"
                         data-dismiss="modal">
                <i class="ace-icon fa fa-times"></i>
                Return
            </router-link>
        </p>
    </div>
</template>

<script>
    export default {
        name: "course-content",
        data: function(){
            return {
                course: {},
                saveContentLabel: {},
                url: process.env.VUE_APP_SERVER,
                saveContentInterval: {},
            }
        },
        mounted() {
            let vm = this;
            let course = SessionStorage.get(SESSION_KEY_COURSE) || {}
            if(Tool.isEmpty(course)){
                vm.$router.push('/welcome');
            }
            vm.course = course;
            vm.init()
        },
        destroyed() {
          clearInterval(this.saveContentInterval);
        },
        methods:{
            init: function(){
                let vm = this;
                let course = vm.course;
                let id = course.id;
                $('#content').summernote({
                    focus: true,
                    height: 300
                });

                $('#content').summernote('code', '');
                vm.saveContentLabel = "";
                Loading.show();
                vm.$ajax.get(vm.url + '/business/admin/course/find-content/'+id)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        if(resp.content){
                            $('#content').summernote('code', resp.content.content);
                        }
                        // vm.saveContentInterval = setInterval(()=>{
                        //     vm.saveContent();
                        // }, 5000);
                    }else{
                        Toast.warning(resp.message);
                    }
                })

            },
            saveContent(){
                let vm = this;
                let content = $('#content').summernote('code');
                vm.$ajax.post(vm.url + '/business/admin/course/save-content',
                    {
                        id: vm.course.id,
                        content: content
                    }).then((res) => {
                   Loading.hide();
                   let resp = res.data;
                   if(resp.success){
                       let now = Tool.dateFormat("hh:mm:ss");
                       vm.saveContentLabel = "Last save time: " + now;
                   }else{
                       Toast.warning(resp.message);
                   }
                });
            }
        },

    }
</script>

<style scoped>

</style>