<template>
    <div id="player-modal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <player v-bind:player-id="'modal-player-div'"
                            ref="player"></player>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import player from "./player";
    export default {
        components: {player},
        name: "modal-player",
        data: function(){
            return {
                aliPlayer: {}
            }
        },
        methods:{
            playUrl(url){
                this.$refs.player.playUrl(url);
            },
            playVod(vod){
                this.$refs.player.playVod(vod);
                $('#player-modal').modal('show');
            }
        }
    }
</script>

<style scoped>
    #player-modal .modal-body {
        padding: 0;
    }

</style>
