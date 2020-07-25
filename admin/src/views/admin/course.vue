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
                            <span class="pull-right label label-success arrowed-in arrowed-in-right">
                                {{COURSE_LEVEL | optionKV(course.level)}}
                            </span>
                            <span class="pull-right label label-success arrowed-in arrowed-in-right">
                                {{COURSE_CHARGE | optionKV(course.charge)}}
                            </span>
                            <span class="pull-right label label-success arrowed-in arrowed-in-right">
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
                            <span class="badge badge-info">Order：{{course.sort}}</span>
                            <span class="badge badge-info">{{course.time | formatSecond}}</span>
                        </p>
                        <p>
                            <button v-on:click="toChapter(course)" class="btn btn-white btn-xs btn-info btn-round">
                                chapter
                            </button>&nbsp;
                            <button v-on:click="toContent(course)" class="btn btn-white btn-xs btn-info btn-round">
                                content
                            </button>&nbsp;
                            <button v-on:click="openSortModal(course)" class="btn btn-white btn-xs btn-info btn-round">
                                sort
                            </button>&nbsp;
                            <button v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                                edit
                            </button>&nbsp;
                            <button v-on:click="del(course.id)" class="btn btn-white btn-xs btn-warning btn-round">
                                delete
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
                        <h4 class="modal-title">Form</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">
                                    Categories
                                </label>
                                <div class="col-sm-10">
                                    <ul id="tree" class="ztree"></ul>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Front Image</label>
                                <div class="col-sm-10">
                                    <file v-bind:input-id="'image-upload'"
                                          v-bind:url="'http://127.0.0.1:9003/file/admin/upload'"
                                          v-bind:text="'Upload Image'"
                                          v-bind:suffixs="['jpg', 'jpeg', 'png']"
                                          v-bind:use="FILE_USE.COURSE.key"
                                          v-bind:after-upload="afterUpload"></file>
                                    <div v-show="course.image" class="row">
                                        <div class="col-md-6">
                                            <img v-bind:src="course.image" class="img-responsive">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Course Name</label>
                                <div class="col-sm-10">
                                    <input v-model="course.name" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Teacher</label>
                                <div class="col-sm-10">
                                    <select v-model="course.teacherId" class="form-control">
                                        <option v-for="o in teachers" v-bind:value="o.id">{{o.name}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Description</label>
                                <div class="col-sm-10">
                                    <textarea v-model="course.summary" class="form-control"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Duration</label>
                                <div class="col-sm-10">
                                    <input v-model="course.time" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Price</label>
                                <div class="col-sm-10">
                                    <input v-model="course.price" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Level</label>
                                <div class="col-sm-10">
                                    <select v-model="course.level" class="form-control">
                                        <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Charge</label>
                                <div class="col-sm-10">
                                    <select v-model="course.charge" class="form-control">
                                        <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Status</label>
                                <div class="col-sm-10">
                                    <select v-model="course.status" class="form-control">
                                        <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{o.value}}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Number enrolled</label>
                                <div class="col-sm-10">
                                    <input v-model="course.enroll" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Order</label>
                                <div class="col-sm-10">
                                    <input v-model="course.sort" class="form-control" disabled>
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

        <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Order</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-lg-3">
                                    Current Order
                                </label>
                                <div class="col-lg-9">
                                    <input class="form-control" v-model="sort.oldSort" name="oldSort" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-lg-3">
                                    New Order
                                </label>
                                <div class="col-lg-9">
                                    <input class="form-control" v-model="sort.newSort" name="newSort">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
                            <i class="ace-icon fa fa-times"></i>
                            Cancel
                        </button>
                        <button type="button" class="btn btn-white btn-info btn-round" v-on:click="updateSort()">
                            <i class="ace-icon fa fa-plus blue"></i>
                            Update Order
                        </button>
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
        name: "business-course",
        components: {pageHeader, pagination, file},
        data: function(){
            return {
                course: {},
                courses: [],
                COURSE_LEVEL: COURSE_LEVEL,
                COURSE_CHARGE: COURSE_CHARGE,
                COURSE_STATUS: COURSE_STATUS,
                FILE_USE: FILE_USE,
                categories: [],
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
            vm.allCategory();
            vm.allTeachers()
            vm.list(1);
        },
        methods:{
            afterUpload(resp){
                this.course.image = resp.content.path;
            },
            updateSort(){
                let vm = this;
                if(vm.sort.newSort === vm.sort.oldSort){
                    Toast.warning("Order didn't change");
                    return;
                }
                Loading.show();
                vm.$ajax.post(vm.url + '/business/admin/course/sort', vm.sort)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        Toast.success('Success');
                        $("#course-sort-modal").modal("hide");
                        vm.list(1);
                    }else{
                        Toast.warning(resp.message);
                    }
                })
            },
            openSortModal(course){
                let vm = this;
                vm.sort = {
                    id: course.id,
                    oldSort: course.sort,
                    newSort: course.sort
                }
                $("#course-sort-modal").modal("show");
            },
            toContent(course){
              let vm = this;
              SessionStorage.set(SESSION_KEY_COURSE, course);
              vm.$router.push('/business/content')
            },
            toChapter(course){
              let vm = this;
              SessionStorage.set(SESSION_KEY_COURSE, course);
              vm.$router.push('/business/chapter');
            },
            allCategory(){
              let vm = this;
              Loading.show();
              vm.$ajax.post(vm.url + '/business/admin/category/all')
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    vm.categories = resp.content;
                    vm.initTree();
                })
            },
            initTree(){
                let vm = this;
                let setting = {
                    check:{
                        enable: true
                    },
                    data:{
                        simpleData: {
                            idKey: "id",
                            pIdKey: "parent",
                            rootPId: "00000000",
                            enable: true
                        }
                    }
                };
                let zNodes = vm.categories;
                vm.tree = $.fn.zTree.init($("#tree"), setting, zNodes);
            },
            listCategory(courseId){
              let vm = this;
              Loading.show();
              vm.$ajax.post(vm.url + '/business/admin/course/list-category/'+courseId)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    let categories = resp.content;
                    vm.tree.checkAllNodes(false);
                    for(let i=0;i<categories.length;i++){
                        let node = vm.tree.getNodeByParam("id", categories[i].categoryId);
                        vm.tree.checkNode(node, true);
                    }
                })
            },
            add(){
                let vm = this;
                vm.tree.checkAllNodes(false);
                vm.course = {
                    sort: vm.$refs.pagination.total + 1
                };
                $('#form-modal').modal('show');
            },
            save(){
                let vm = this;
                let course = vm.course
                if(!Validator.require(course.name, 'name') ||
                !Validator.length(course.name, 'name', 1, 50) ||
                !Validator.require(course.price, 'price')){
                    return;
                }
                let categories = vm.tree.getCheckedNodes();
                if(Tool.isEmpty(categories)){
                    Toast.warning('Please check categories');
                    return;
                }
                vm.course.categories = categories;
                Loading.show();
                vm.$ajax.post(vm.url + '/business/admin/course/save', course)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        $('#form-modal').modal('hide');
                        vm.list(1);
                        Toast.success('Saved Successfully');
                    }else{
                        Toast.warning(resp.message);
                    }
                })
            },
            edit(course){
                let vm = this;
                vm.course = $.extend({}, course);
                vm.listCategory(course.id);
                $('#form-modal').modal('show');
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
            allTeachers(){
                let vm = this;
                vm.$ajax.post(vm.url + '/business/admin/teacher/all/')
                .then((res) => {
                    let resp = res.data;
                    if(resp.success){
                        vm.teachers = resp.content;
                    }else{
                        console.log(resp.message);
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
    .caption h3 {
        font-size: 20px;
    }

    @media (max-width: 1199px) {
        .caption h3 {
            font-size: 16px;
        }
    }
</style>
