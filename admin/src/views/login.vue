
<template>
    <div class="main-container">
        <div class="main-content">
            <div class="row">
                <div class="col-sm-10 col-sm-offset-1">
                    <div class="login-container">
                        <div class="center">
                            <h1>
                                <i class="ace-icon fa fa-leaf green"></i>
                                <span class="">Login</span>
                            </h1>
                        </div>

                        <div class="space-6"></div>

                        <div class="position-relative">
                            <div id="login-box" class="login-box visible widget-box no-border">
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <h4 class="header blue lighter bigger">
                                            <i class="ace-icon fa fa-coffee green"></i>
                                            ACE ADMIN
                                        </h4>

                                        <div class="space-6"></div>

                                        <form>
                                            <fieldset>
                                                <label class="block clearfix">
                      <span class="block input-icon input-icon-right">
                        <input type="text" v-model="user.loginName" class="form-control" placeholder="用户名"/>
                        <i class="ace-icon fa fa-user"></i>
                      </span>
                                                </label>

                                                <label class="block clearfix">
                      <span class="block input-icon input-icon-right">
                        <input v-model="user.password" type="password" class="form-control" placeholder="密码"/>
                        <i class="ace-icon fa fa-lock"></i>
                      </span>
                                                </label>

                                                <label class="block clearfix">
<!--                      <span class="block input-icon input-icon-right">-->
<!--                        <div class="input-group">-->
<!--                          <input  type="text" class="form-control" placeholder="验证码">-->
<!--                          <span class="input-group-addon" id="basic-addon2">-->
<!--                            <img v-on:click="loadImageCode()" id="image-code" alt="验证码"/>-->
<!--                          </span>-->
<!--                        </div>-->
<!--                      </span>-->
                                                </label>

                                                <div class="space"></div>

                                                <div class="clearfix">
                                                    <label class="inline">
                                                        <input v-model="remember" type="checkbox" class="ace"/>
                                                        <span class="lbl">Remember Me</span>
                                                    </label>

                                                    <button type="button"
                                                            class="width-35 pull-right btn btn-sm btn-primary"
                                                            v-on:click="login()">
                                                        <i class="ace-icon fa fa-key"></i>
                                                        <span class="bigger-110">Login</span>
                                                    </button>
                                                </div>

                                                <div class="space-4"></div>
                                            </fieldset>
                                        </form>

                                    </div><!-- /.widget-main -->

                                </div><!-- /.widget-body -->
                            </div><!-- /.login-box -->
                        </div><!-- /.position-relative -->

                    </div>
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.main-content -->
    </div><!-- /.main-container -->
</template>

<script>
    export default {
        name: "login",
        data: function(){
          return {
              user: {},
              url: process.env.VUE_APP_SERVER,
              error: '',
              remember: null
          }
        },
        mounted:function(){
            $('body').removeClass('no-skin');
            $('body').attr('class', 'login-layout blur-login');
        },
        methods:{
            login(){
                if(!Validator.require(this.user.loginName, 'loginName')
                    || !Validator.require(this.user.password, 'password')){
                    Toast.warning('You Must Submit username and password!!');
                    this.user = {};
                    return;
                }

                let url = this.url;
                let password = hex_md5(this.user.password + KEY);
                this.user.password = password;
                this.$ajax.post(url + '/system/admin/user/login', this.user)
                .then((res) => {
                    let resp = res.data;
                    if(resp.success){
                        this.$router.push("/welcome");
                    }else{
                        Toast.warning(resp.message);
                        this.user = {};
                    }
                })

            }
        }
    }

</script>

<style scoped>

</style>
