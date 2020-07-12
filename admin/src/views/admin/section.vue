<template>
    <div>
        <pageHeader title="Section View">section</pageHeader>
        <h4 class="lighter">
            <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
            <router-link to="/business/course" class="pink"> {{course.name}} </router-link>：
            <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
            <router-link to="/business/chapter" class="pink"> {{chapter.name}} </router-link>
        </h4>
        <hr>
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
                <th>ID</th>
                <th>Title</th>
                <th>VOD</th>
                <th>Duration</th>
                <th>Charge</th>
                <th>Order</th>
                <th>Operations</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="section in sections">
                <td>{{section.id}}</td>
                <td>{{section.title}}</td>
                <td>{{section.vod}}</td>
                <td>{{section.time | formatSecond}}</td>
                <td>{{SECTION_CHARGE | optionKV(section.charge)}}</td>
                <td>{{section.sort}}</td>
                <td>
                    <div class="hidden-sm hidden-xs btn-group">
                        <button v-on:click="play(section)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-video-camera bigger-120"></i>
                        </button>
                        <button v-on:click="edit(section)" class="btn btn-xs btn-info">
                            <i class="ace-icon fa fa-pencil bigger-120"></i>
                        </button>
                        <button v-on:click="del(section.id)" class="btn btn-xs btn-danger">
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
                                <label class="col-sm-2 control-label">Title</label>
                                <div class="col-sm-10">
                                    <input v-model="section.title" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Course</label>
<!--                                <div class="col-sm-10">-->
<!--                                    <p class="form-control-static">{{course.name}}</p>-->
<!--                                </div>-->
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Chapter</label>
                                <div class="col-sm-10">
                                    <p class="form-control-static">{{chapter.name}}</p>
                                </div>
                            </div>
<!--                            <div class="form-group">-->
<!--                                <label class="col-sm-2 control-label">Video</label>-->
<!--                                <div class="col-sm-10">-->
<!--                                    <vod v-bind:input-id="'video-upload'"-->
<!--                                         v-bind:text="'上传VOD'"-->
<!--                                         v-bind:suffixs="['mp4']"-->
<!--                                         v-bind:use="FILE_USE.COURSE.key"-->
<!--                                         v-bind:after-upload="afterUpload"></vod>-->
<!--                                    <div v-show="section.video" class="row">-->
<!--                                        <div class="col-md-9">-->
<!--                                            <player v-bind:player-id="'form-player-div'"-->
<!--                                                    ref="player"></player>-->
<!--                                            <video v-bind:src="section.video" id="video" controls="controls" class="hidden"></video>-->
<!--                                        </div>-->
<!--                                    </div>-->
<!--                                </div>-->
<!--                            </div>-->
                            <div class="form-group">
                                <label class="col-sm-2 control-label">时长</label>
                                <div class="col-sm-10">
                                    <input v-model="section.time" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">视频</label>
<!--                                <div class="col-sm-10">-->
<!--                                    <input v-model="section.video" class="form-control" disabled>-->
<!--                                </div>-->
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">VOD</label>
                                <div class="col-sm-10">
                                    <input v-model="section.vod" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">收费</label>
                                <div class="col-sm-10">
                                    <select v-model="section.charge" class="form-control">
                                        <option v-for="o in SECTION_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">顺序</label>
                                <div class="col-sm-10">
                                    <input v-model="section.sort" class="form-control">
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

<!--        <modal-player ref="modalPlayer"></modal-player>-->
    </div>
</template>

<script>
    import pagination from '../../components/pagination.vue'
    import pageHeader from "../../components/pageHeader";
    export default {
        name: "business-section",
        components: { pagination,pageHeader },
        data: function(){
          return {
              section: {},
              sections: [],
              SECTION_CHARGE: SECTION_CHARGE,
              FILE_USE: FILE_USE,
              course: {},
              chapter: {},
              url: process.env.VUE_APP_SERVER
          }
        },
        mounted() {
            let vm = this;
            let chapter = SessionStorage.get(SESSION_KEY_CHAPTER) || {};
            vm.chapter = chapter;
            vm.list(1);

        },
        methods: {
            add: function(){
              let vm = this;
              vm.section = {};
              $('#form-modal').modal('show');
            },
            list(page){
                let vm = this;
                vm.$ajax.post(vm.url + '/business/admin/section/list', {
                    page: page,
                    size: vm.$refs.pagination.size,
                    chapterId: vm.chapter.id
                }).then((res) => {
                    Loading.hide();
                    vm.sections = res.data.content.list;
                    vm.$refs.pagination.render(page, res.data.content.total)
                })
            },
            save() {
                let vm = this;
                vm.section.video = "";
                let section = vm.section;
                if (!Validator.require(section.title, "Title")
                    || !Validator.length(section.title, "title", 1, 50)
                ) {
                    return;
                }
                section.chapterId = vm.chapter.id;
                Loading.show();
                let url = vm.url;
                console.log(section);
                vm.$ajax.post(url + '/business/admin/section/save', section)
                    .then((res) => {
                        Loading.hide();
                        if (res.data.success) {
                            $("#form-modal").modal("hide");
                            vm.list(1);
                            Toast.success("Save successfully!");
                        } else {
                            Toast.warning(res.data.message)
                        }
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
                        vm.$ajax.delete(vm.url+'/business/admin/section/delete/'+id)
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
            },
            edit(section){
                let vm = this;
                vm.section = $.extend({}, section);
                $("#form-modal").modal("show");
            }
        }
    }
</script>

<style scoped>
    video {
        width: 100%;
        height: auto;
        margin-top: 10px;
    }
</style>