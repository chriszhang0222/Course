<template>
    <div>
        <pageHeader title="Teacher Page View">teacher</pageHeader>
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
                       v-bind:title="teacher.name"
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
                        <h4 class="modal-title">Form</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Name</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Nickname</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.nickname" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Photo</label>
                                <div class="col-sm-10">
                                    <file v-bind:suffixs="suffixs"
                                          v-bind:url="'http://127.0.0.1:9003/file/admin/upload'"
                                          v-bind:after-upload="afterUpload"
                                          v-bind:text="'Photo Upload'"
                                          v-bind:input-id="'image-upload'"
                                          v-bind:use="FILE_USE.TEACHER.key"
                                    ></file>
<!--                                    <input type="file" v-on:change="uploadImage()" ref="file" id="file-upload-input">-->
                                    <div v-show="teacher.image" class="row">
                                        <div class="col-md-4">
                                            <img v-bind:src="teacher.image" class="img-responsive"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
<!--                            <div class="form-group">-->
<!--                                <label class="col-sm-2 control-label">头像</label>-->
<!--                                <div class="col-sm-10">-->
<!--                                    <big-file v-bind:input-id="'image-upload'"-->
<!--                                              v-bind:text="'上传头像'"-->
<!--                                              v-bind:suffixs="['jpg', 'jpeg', 'png']"-->
<!--                                              v-bind:use="FILE_USE.TEACHER.key"-->
<!--                                              v-bind:after-upload="afterUpload"></big-file>-->
<!--                                    <div v-show="teacher.image" class="row">-->
<!--                                        <div class="col-md-4">-->
<!--                                            <img v-bind:src="teacher.image" class="img-responsive">-->
<!--                                        </div>-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </div>-->
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Position</label>
                                <div class="col-sm-10">
                                    <input v-model="teacher.position" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Intro</label>
                                <div class="col-sm-10">
                                    <textarea v-model="teacher.intro" class="form-control" rows="5"></textarea>
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

    </div>
</template>

<script>
    import pagination from '../../components/pagination'
    import pageHeader from "../../components/pageHeader";
    import file from '../../components/file.vue'
    export default {
        name: "business-teacher",
        components: {pageHeader, pagination, file},
        data: function(){
          return {
              teacher: {},
              teachers: [],
              FILE_USE: FILE_USE,
              url: process.env.VUE_APP_SERVER,
              suffixs: ['jpg', 'png', 'jpeg', 'bmp']
          }
        },
        mounted() {
            let vm = this;
            vm.$refs.pagination.size = 5;
            vm.list(1);
        },
        methods: {
            list(page){
                let vm = this;
                Loading.show();
                vm.$ajax.post(vm.url+ '/business/admin/teacher/list', {
                    page: page,
                    size: vm.$refs.size
                }).then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    vm.teachers = resp.content.list;
                    vm.$refs.pagination.render(page, resp.content.total);
                })
            },
            add(){
                this.teacher = {}
                $('#form-modal').modal('show');
            },
            edit(teacher){
                let vm = this;
                vm.teacher = $.extend({}, teacher);
                $('#form-modal').modal('show');
            },
            save(){
                let vm = this;
                if(!Validator.require(vm.teacher.name, 'name')){
                    return;
                }
                Loading.show();
                vm.$ajax.post(vm.url + '/business/admin/teacher/save', vm.teacher)
                .then((res) => {
                    Loading.hide();
                    if(res.data.success){
                        $('#form-modal').modal('hide');
                        vm.list(1);
                        Toast.success('Saved Successfully');
                    }else{
                        Toast.warning(res.data.message);
                    }
                })
            },
            del(id){
                let vm = this;
                Confirm.show('Are you sure?', ()=>{
                    Loading.show();
                    vm.$ajax.delete(vm.url + '/business/admin/teacher/delete/'+id)
                    .then((res) => {
                        Loading.hide();
                        let resp = res.data;
                        if(resp.success){
                            vm.list(1);
                            Toast.success('Delete successfully!');
                        }else{
                            Toast.warning(resp.message);
                        }
                    })
                })
            },
            validfile(file){
                let suffixs = ['jpg', 'png', 'jpeg', 'bmp'];
                let fileName = file.name;
                let suffix = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length).toLowerCase();
                for(let i=0;i<suffixs.length;i++){
                    if(suffix === suffixs[i].toLowerCase()){
                        return true;
                    }
                }
                return false;
            },
            uploadImage(){
                let vm = this;
                let formData = new window.FormData();
                let file = vm.$refs.file.files[0];
                if(!vm.validfile(file)){
                    Toast.warning('File format is not correct!!');
                    return;
                }
                formData.append('file', file);

                Loading.show();
                vm.$ajax.post('http://127.0.0.1:9003' + '/file/admin/upload', formData)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    let image = resp.content;
                    console.log(image);
                    vm.teacher.image = image;
                })
            },
            afterUpload(resp){
                let vm = this;
                let image = resp.content.path;
                vm.teacher.image = image;
            },
        }
    }
</script>

<style scoped>

</style>
