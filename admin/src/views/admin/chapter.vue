<template>
    <div>
        <pageHeader title="Chapter View">chapter</pageHeader>
        <h4 class="lighter">
            <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
            <router-link to="/business/course" class="pink"> {{course.name}} </router-link>
        </h4>
        <p>
            <button v-on:click="add()" class="btn btn-app btn-pink btn-xs radius-4">
                <i class="ace-icon fa fa-edit"></i>
                New
            </button>
            &nbsp;
            <button v-on:click="list(1)" class="btn btn-app btn-pink btn-xs radius-4">
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
                        <th>Course ID</th>
                        <th>Operation</th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="chapter in chapters">
                        <td>{{chapter.id}}</td>
                        <td>{{chapter.name}}</td>
                        <td>{{chapter.courseId}}</td>
                        <td>
                            <div class="hidden-sm hidden-xs btn-group">
                                <button v-on:click="toSection(chapter)" class="btn btn-white btn-xs btn-info btn-round">
                                    Section
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
    import pagination from '../../components/pagination.vue'
    import pageHeader from "../../components/pageHeader";
    export default {
        components: {pagination, pageHeader},
        name: "chapter",
        data: function () {
            return {
                chapters:[],
                chapter:{},
                course: {}
            }
        },
        mounted:function(){

            let vm = this;
            let course = SessionStorage.get(SESSION_KEY_COURSE) || {}
            if(Tool.isEmpty(course)){
                vm.$router.push("/welcome");
            }
            vm.course = course;
            vm.list(1);
        },
        methods: {
            toSection(chapter){
              let vm = this;
              SessionStorage.set(SESSION_KEY_CHAPTER, chapter);
              vm.$router.push('/business/section');
            },
            add(){
                let _this = this;
                _this.chapter = {};
                $(".modal").modal("show");
            },
            list(page){
                let _this = this;
                //TODO: refactor url to gateway
                Loading.show()
                _this.$ajax.post(process.env.VUE_APP_SERVER+'/business/admin/chapter/list', {
                    page:page,
                    size:_this.$refs.pagination.size,
                    courseId: _this.course.id
                })
                    .then((res) => {
                        Loading.hide()
                        _this.chapters = res.data.content.list;
                        _this.$refs.pagination.render(page, res.data.content.total)
                    })
            },
            save(){
                let _this = this;
                if(!Validator.require(_this.chapter.name, "Name")
                || !Validator.require(_this.chapter.courseId, "CourseId")
                || !Validator.require(_this.chapter.vourseId, "CourseId")){
                    return;
                }
                _this.$ajax.post(process.env.VUE_APP_SERVER+'/business/admin/chapter/save', _this.chapter)
                    .then((res) => {
                        console.log(res);
                        if(res.data.success){
                            $("#form-modal").modal("hide");
                            _this.list(1)
                            Toast.success("Save successfully");
                        }else{
                            Toast.warning(res.data.message)
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
                Swal.fire({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.value) {
                        _this.$ajax.delete(process.env.VUE_APP_SERVER+'/business/admin/chapter/delete/'+id)
                            .then((res) => {
                                console.log(res);
                                let resp = res.data
                                if(resp.success){
                                    Toast.success("Delete successfully");
                                    _this.list(1)
                                }
                            })
                        Swal.fire(
                            'Deleted!',
                            'Your file has been deleted.',
                            'success'
                        )
                    }
                });

            }
        }
    }
</script>

<style scoped>

</style>