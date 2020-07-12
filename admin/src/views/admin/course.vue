<template>
    <div>
        <pageHeader title="Course Page View">course</pageHeader>
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
        <div class="row">
            <div v-for="course in courses" class="col-md-4">
                <div class="thumbnail search-thumbnail">
                    <img v-show="!course.image" class="media-object" src="/static/image/demo-course.jpg" />
                    <img v-show="course.image" class="media-object" v-bind:src="course.image" />
                    <div class="caption">
                        <div class="clearfix">
                            <span class="pull-right label label-primary info-label">
                                {{COURSE_LEVEL | optionKV(course.level)}}
                            </span>
                            <span class="pull-right label label-primary info-label">
                                {{COURSE_CHARGE | optionKV(course.charge)}}
                            </span>
                            <span class="pull-right label label-primary info-label">
                                {{COURSE_STATUS | optionKV(course.status)}}
                          </span>
                        </div>
                        <h3 class="search-title">
                            <a href="#" class="blue">{{course.name}}</a>
                        </h3>
                        <div v-for="teacher in teachers.filter(t=>{return t.id===course.teacherId})" class="profile-activity clearfix">
                            <div>
                                <img v-show="!teacher.image" class="pull-left" src="/ace/assets/images/avatars/avatar5.png">
                                <img v-show="teacher.image" class="pull-left" v-bind:src="teacher.image">
                                <a class="user" href="#"> {{teacher.name}} </a>
                                <br>
                                {{teacher.position}}
                            </div>
                        </div>
                        <p>
                            <span class="blue bolder bigger-150">{{course.price}}&nbsp;<i class="fa fa-rmb"></i></span>&nbsp;
                        </p>
                        <p>{{course.summary}}</p>
                        <p>
                            <span class="badge badge-info">{{course.id}}</span>
                            <span class="badge badge-info">排序：{{course.sort}}</span>
                            <span class="badge badge-info">{{course.time | formatSecond}}</span>
                        </p>
                        <p>
                            <button v-on:click="toChapter(course)" class="btn btn-white btn-xs btn-info btn-round">
                                大章
                            </button>&nbsp;
                            <button v-on:click="toContent(course)" class="btn btn-white btn-xs btn-info btn-round">
                                内容
                            </button>&nbsp;
                            <button v-on:click="openSortModal(course)" class="btn btn-white btn-xs btn-info btn-round">
                                排序
                            </button>&nbsp;
                            <button v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                                编辑
                            </button>&nbsp;
                            <button v-on:click="del(course.id)" class="btn btn-white btn-xs btn-warning btn-round">
                                删除
                            </button>
                        </p>
                    </div>
                </div>
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
                                <label class="col-sm-2 control-label">
                                    分类
                                </label>
                                <div class="col-sm-10">
                                    <ul id="tree" class="ztree"></ul>
                                </div>
                            </div>
<!--                            <div class="form-group">-->
<!--                                <label class="col-sm-2 control-label">封面</label>-->
<!--                                <div class="col-sm-10">-->
<!--                                    <big-file v-bind:input-id="'image-upload'"-->
<!--                                              v-bind:text="'上传封面'"-->
<!--                                              v-bind:suffixs="['jpg', 'jpeg', 'png']"-->
<!--                                              v-bind:use="FILE_USE.COURSE.key"-->
<!--                                              v-bind:after-upload="afterUpload"></big-file>-->
<!--                                    <div v-show="course.image" class="row">-->
<!--                                        <div class="col-md-6">-->
<!--                                            <img v-bind:src="course.image" class="img-responsive">-->
<!--                                        </div>-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </div>-->
                            <div class="form-group">
                                <label class="col-sm-2 control-label">名称</label>
                                <div class="col-sm-10">
                                    <input v-model="course.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">讲师</label>
                                <div class="col-sm-10">
                                    <select v-model="course.teacherId" class="form-control">
                                        <option v-for="o in teachers" v-bind:value="o.id">{{o.name}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">概述</label>
                                <div class="col-sm-10">
                                    <textarea v-model="course.summary" class="form-control"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">时长</label>
                                <div class="col-sm-10">
                                    <input v-model="course.time" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">价格（元）</label>
                                <div class="col-sm-10">
                                    <input v-model="course.price" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">级别</label>
                                <div class="col-sm-10">
                                    <select v-model="course.level" class="form-control">
                                        <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">收费</label>
                                <div class="col-sm-10">
                                    <select v-model="course.charge" class="form-control">
                                        <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">状态</label>
                                <div class="col-sm-10">
                                    <select v-model="course.status" class="form-control">
                                        <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">报名数</label>
                                <div class="col-sm-10">
                                    <input v-model="course.enroll" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">顺序</label>
                                <div class="col-sm-10">
                                    <input v-model="course.sort" class="form-control" disabled>
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
        </div><!-- /.modal -->

<!--        <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog">-->
<!--            <div class="modal-dialog" role="document">-->
<!--                <div class="modal-content">-->
<!--                    <div class="modal-header">-->
<!--                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
<!--                        <h4 class="modal-title">排序</h4>-->
<!--                    </div>-->
<!--                    <div class="modal-body">-->
<!--                        <form class="form-horizontal">-->
<!--                            <div class="form-group">-->
<!--                                <label class="control-label col-lg-3">-->
<!--                                    当前排序-->
<!--                                </label>-->
<!--                                <div class="col-lg-9">-->
<!--                                    <input class="form-control" v-model="sort.oldSort" name="oldSort" disabled>-->
<!--                                </div>-->
<!--                            </div>-->
<!--                            <div class="form-group">-->
<!--                                <label class="control-label col-lg-3">-->
<!--                                    新排序-->
<!--                                </label>-->
<!--                                <div class="col-lg-9">-->
<!--                                    <input class="form-control" v-model="sort.newSort" name="newSort">-->
<!--                                </div>-->
<!--                            </div>-->
<!--                        </form>-->
<!--                    </div>-->
<!--                    <div class="modal-footer">-->
<!--                        <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">-->
<!--                            <i class="ace-icon fa fa-times"></i>-->
<!--                            取消-->
<!--                        </button>-->
<!--                        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="updateSort()">-->
<!--                            <i class="ace-icon fa fa-plus blue"></i>-->
<!--                            更新排序-->
<!--                        </button>-->
<!--                    </div>-->
<!--                </div>&lt;!&ndash; /.modal-content &ndash;&gt;-->
<!--            </div>&lt;!&ndash; /.modal-dialog &ndash;&gt;-->
<!--        </div>&lt;!&ndash; /.modal &ndash;&gt;-->

    </div>
</template>

<script>
    import pagination from '../../components/pagination'
    import pageHeader from "../../components/pageHeader";
    export default {
        name: "business-course",
        components: {pageHeader, pagination},
        data: function(){
            return {
                course: {},
                courses: [],
                COURSE_LEVEL: COURSE_LEVEL,
                COURSE_CHARGE: COURSE_CHARGE,
                COURSE_STATUS: COURSE_STATUS,
                FILE_USE: FILE_USE,
                categorys: [],
                tree: {},
                saveContentLabel: "",
                sort: {
                    id: "",
                    oldSort: 0,
                    newSort: 0
                },
                teachers: [],
                url: process.env.VUE_APP_SERVER
            }
        },
        mounted() {
            let vm = this;
            vm.$refs.pagination.size = 5;
            vm.list(1);
        },
        methods:{
            add(){

            },
            edit(course){

            },
            list(page){
              let vm = this;
              Loading.show();
              vm.$ajax.post(vm.url + '/business/admin/course/list', {
                  page: page,
                  size: vm.$refs.pagination.size
              }).then((res) => {
                  Loading.hide();
                  vm.courses = res.data.content.list;
                  vm.$refs.pagination.render(page, res.data.content.total);
              })
            },
            del(id){
                let vm = this;
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
                        vm.$ajax.delete(process.env.VUE_APP_SERVER+'/business/admin/course/delete/'+id)
                            .then((res) => {
                                console.log(res);
                                let resp = res.data
                                if(resp.success){
                                    Toast.success("Delete successfully");
                                    vm.list(1)
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