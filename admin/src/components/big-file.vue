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
        name: "big-file",
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
                default: "upload"
            },
            shardSize: {
                default: 1024 * 1024 * 10
            }
        },
        data:function(){
            return {

            }
        },
        methods:{
            uploadFile(){
                let vm = this;
                let file = vm.$refs.file.files[0];

                if(!vm.validfile(file)){
                    Toast.warning('File format is not supported!!Please choose:' + vm.suffixs.join(","));
                    $('#'+vm.inputId + '-input').val("");
                    return;
                }
                let suffix = file.name.substring(file.name.lastIndexOf(".")+1, file.name.length).toLowerCase();
                let shardSize = vm.shardSize;
                let shardIndex = 1;
                let size = file.size;
                let shardTotal = Math.ceil(size / shardSize);
                let key62 = vm.getKey(file);

                let param = {
                    'shardIndex': shardIndex,
                    'shardSize': shardSize,
                    'shardTotal': shardTotal,
                    'use': vm.use,
                    'name': file.name,
                    'suffix': suffix,
                    'size': file.size,
                    'key': key62
                }
                vm.check(param);

            },
            check(param){
                let url = process.env.VUE_APP_SERVER;
                let vm = this;
                vm.$ajax.get(url + '/file/admin/check/' + param.key)
                .then((res) => {
                    let resp = res.data;
                    if(resp.success){
                        let obj = resp.content;
                        if(!obj){
                            param.shardIndex = 1;
                            console.log('没有找到文件记录，从分片1开始上传');
                            vm.upload(param);
                        }else if(obj.shardIndex === obj.shardTotal){
                            Toast.success('Upload Successfully!!');
                            vm.afterUpload(resp);
                            $("#" + vm.inputId + "-input").val("");
                        }else{
                            param.shardIndex = obj.shardIndex + 1;
                            console.log("找到文件记录，从分片" + param.shardIndex + "开始上传");
                            vm.upload(param);
                        }
                    }else{
                        Toast.warning("Upload Failed!!");
                        $('#' + this.inputId + '-input').val("");
                    }
                })
            },
            upload(param){
                let vm = this;
                let url = process.env.VUE_APP_SERVER;
                let shardIndex = param.shardIndex;
                let shardTotal = param.shardTotal;
                let shardSize = param.shardSize;
                let fileShard = vm.getFileShard(shardIndex, shardSize);

                let fileReader = new FileReader();
                Progress.show(parseInt((shardIndex-1)*100/shardTotal));
                fileReader.onload = (e) => {
                    let base64 = e.target.result;
                    param.shard = base64;
                    vm.$ajax.post(url + "/file/admin/" + vm.url, param)
                    .then((res) => {
                        let resp = res.data;
                        Progress.show(parseInt(shardIndex*100/shardTotal));
                        if(shardIndex < shardTotal){
                            param.shardIndex = param.shardIndex + 1;
                            vm.upload(param);
                        }else{
                            Progress.hide();
                            vm.afterUpload(resp);
                            $("#" + vm.inputId + "-input").val("");
                        }
                    })
                }
                fileReader.readAsDataURL(fileShard);
            },
            getFileShard(shardIndex, shardSize){
                let file = this.$refs.file.files[0];
                let start = (shardIndex - 1) * shardSize;
                let end = Math.min(start + shardSize, file.size);
                let fileShard = file.slice(start, end);
                return fileShard;
            },
            getKey(file){
                let key = hex_md5(file.name + file.size + file.type);
                let key10 = parseInt(key, 16);
                let key62 = Tool._10to62(key10);
                return key62;
            },
            constructForm(formData, file){
                let key = Tool._10to62(parseInt(hex_md5(file),16));
                let shardSize = 10 * 1024 * 1024;
                let shardIndex = 1;
                let start = (shardSize - 1) * shardIndex;
                let end = Math.min(file.size, start + shardSize);
                let fileShard = file.slice(start, end);

                let size = file.size;
                let shardTotal = Math.ceil(size / shardSize);
                let suffix = file.name.substring(file.name.lastIndexOf('.')+1, file.name.length).toLowerCase();

                formData.append('shard', fileShard);
                formData.append('shardIndex', shardIndex);
                formData.append('shardSize', shardSize);
                formData.append('shardTotal', shardTotal);
                formData.append('use', this.use);
                formData.append('name', file.name);
                formData.append('suffix', suffix);
                formData.append('size', size);
                formData.append('key', key);
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
