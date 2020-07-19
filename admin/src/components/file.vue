<template>
    <div>
        <button type="button" v-on:click="selectFile()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-upload">
            </i>
            {{ text }}
        </button>
        <input class="hidden" type="file" ref="file" v-on:change="uploadFile()"
        v-bind:id="inputId + '-input'"/>
    </div>
</template>

<script>
    export default {
        name: "file",
        props:{
            text: {
                default: 'File Upload'
            },
            inputId: {
                default: 'file-upload'
            },
            suffixs: {
                required: true,
                default: []
            },
            afterUpload: {
                required: false,
                type: Function,
                default: null
            },
            use: {
                default: ""
            },
            url: {
                default: process.env.VUE_APP_SERVER
            }
        },
        data:function(){
            return {

            }
        },
        methods:{
            uploadFile(){
                let vm = this;
                let formData = new window.FormData();
                let file = vm.$refs.file.files[0];

                if(!vm.validfile(file)){
                    Toast.warning('File format is not supported!!Please choose:' + vm.suffixs.join(","));
                    $('#'+vm.inputId + '-input').val("");
                    return;
                }
                formData.append('file', file);
                formData.append('use', vm.use);
                Loading.show();
                vm.$ajax.post(vm.url, formData).then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    console.log('upload successfully!!!');
                    if(vm.afterUpload !== null){
                        vm.afterUpload(resp);
                    }
                    $('#' + vm.inputId + "-input").val("");
                })

            },
            validfile(file){
                let vm = this;
                let suffixs = vm.suffixs;
                let fileName = file.name;
                let suffix = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length).toLowerCase();
                for(let i=0;i<suffixs.length;i++){
                    if(suffix === suffixs[i].toLowerCase()){
                        return true;
                    }
                }
                return false;
            },
            selectFile(){
                $('#' + this.inputId + '-input').trigger('click');
            }
        }
    }
</script>

<style scoped>

</style>