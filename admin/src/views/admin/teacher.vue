<template>
    <div>
        <PageHeader title="Teacher Page View">teacher</PageHeader>
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
            <div v-for="teacher in teachers" class="col-md-3">
                <div>
                    <span class="profile-picture">
                        <img v-show="!teacher.image" class="editable img-responsive editable-click editable-empty" src="/static/image/讲师头像/头像1.jpg" v-bind:title="teacher.intro"/>
                        <img v-show="teacher.image" class="editable img-responsive editable-click editable-empty" v-bind:src="teacher.image" v-bind:title="teacher.intro"/>
                    </span>
                    <div class="space-4"></div>
                    <div class="width-85 label label-info label-xlg arrowed-in arrowed-in-right">
                        <div class="inline position-relative">
                            <a href="javascript:;" class="user-title-label dropdown-toggle" data-toggle="dropdown">
                                <i class="ace-icon fa fa-circle light-green"></i>
                                &nbsp;
                                <span class="white">{{teacher.position}}</span>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="space-6"></div>
                <div class="text-center">
                    <a href="javascript:;"
                       class="text-info bigger-110"
                       v-bind:title="teacher.motto"
                    >
                        <i class="ace-icon fa fa-user"></i>
                        {{teacher.name}}【{{teacher.nickname}}】
                    </a>
                </div>
                <div class="space-6"></div>
                <div class="profile-social-links align-center">
                    <button v-on:click="edit(teacher)" class="btn btn-xs btn-info">
                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                    </button>
                    &nbsp;
                    <button v-on:click="del(teacher.id)" class="btn btn-xs btn-danger">
                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                    </button>
                </div>
                <div class="hr hr16 dotted"></div>
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
                                <label class="col-sm-2 control-label">姓名</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">昵称</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.nickname" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">头像</label>
                                <div class="col-sm-10">
                                    <big-file v-bind:input-id="'image-upload'"
                                              v-bind:text="'上传头像'"
                                              v-bind:suffixs="['jpg', 'jpeg', 'png']"
                                              v-bind:use="FILE_USE.TEACHER.key"
                                              v-bind:after-upload="afterUpload"></big-file>
                                    <div v-show="teacher.image" class="row">
                                        <div class="col-md-4">
                                            <img v-bind:src="teacher.image" class="img-responsive">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">职位</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.position" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">座右铭</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.motto" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">简介</label>
                                <div class="col-sm-10">
                                    <textarea v-model="teacher.intro" class="form-control" rows="5"></textarea>
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

    </div>
</template>

<script>
    import Pagination from '../../components/pagination'
    import PageHeader from "../../components/pageHeader";
    export default {
        name: "business-teacher",
        components: {PageHeader, Pagination},
        data: function(){
          return {
              teacher: {},
              teachers: [],
              FILE_USE: FILE_USE
          }
        },
        mounted() {
            let vm = this;
        },
        methods: {
            list(page){

            },
            add(){

            },
            edit(teacher){

            },
            save(){

            },
        }
    }
</script>

<style scoped>

</style>