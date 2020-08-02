<template>
    <div v-bind:id="playerId">
    </div>
</template>

<script>
    export default {
        name: "player",
        props:{
            playerId: {
                default: "player-div"
            }
        },
        data: function(){
            return {
                aliPlayer: {},
                cover: 'http://liveroom-img.oss-cn-qingdao.aliyuncs.com/logo.png'
            }
        },
        methods:{
            playUrl(url){
                let vm = this;
                if(vm.aliPlayer){
                    vm.aliPlayer = null;
                    $('#' + vm.playerId + '-player').remove();
                }
                $('#'+vm.playerId).append("<div class=\"prism-player\" id=\"" + vm.playerId + "-player\"></div>");
                vm.aliPlayer = new Aliplayer({
                   id: vm.playerId + '-player',
                   width: '100%',
                   autoplay: false,
                   source: url,
                   cover: vm.cover
                }, function(player){
                    console.log('Player is created')
                });
            },

            playVod(vod){
                let vm = this;
                Loading.show();
                let url = process.env.VUE_APP_SERVER;
                vm.$ajax.get(url + '/file/admin/get-auth' + vod)
                .then((res) => {
                    Loading.hide();
                    let resp = res.data;
                    if(resp.success){
                        if(vm.aliPlayer){
                            vm.aliPlayer = null;
                            $("#" + vm.playerId + '-player').remove();
                        }
                        $("#" + vm.playerId).append("<div class=\"prism-player\" id=\"" + vm.playerId + "-player\"></div>");
                        vm.aliPlayer = new Aliplayer({
                            id: vm.playerId + '-player',
                            width: '100%',
                            autoplay: false,
                            vid: vod,
                            playauth: resp.content,
                            cover: vm.cover,
                            encryptType: 1
                        }, (player) => {
                            console.log('Player is created')
                        })

                    }else{
                        Toast.warning('Error when loading the player');
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
