import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios';
import upperFirst from 'lodash/upperFirst'
import camelCase from 'lodash/camelCase'
import filter from './filter/filter'
Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

axios.defaults.withCredentials = true;

axios.interceptors.request.use(function(config){
    let token = Tool.getLoginUser().token;
    if(Tool.isNotEmpty(token)){
        config.headers.token = token;
    }
    return config;
});

const requireComponent = require.context(
    // 其组件目录的相对路径
    './components',
    // 是否查询其子目录
    false,
    // 匹配基础组件文件名的正则表达式
    /Base[A-Z]\w+\.(vue|js)$/
)

requireComponent.keys().forEach(fileName => {
    // 获取组件配置
    const componentConfig = requireComponent(fileName)

    // 获取组件的 PascalCase 命名
    const componentName = upperFirst(
        camelCase(
            // 获取和目录深度无关的文件名
            fileName
                .split('/')
                .pop()
                .replace(/\.\w+$/, '')
        )
    )

    // 全局注册组件
    Vue.component(
        componentName,
        // 如果这个组件选项是通过 `export default` 导出的，
        // 那么就会优先使用 `.default`，
        // 否则回退到使用模块的根。
        componentConfig.default || componentConfig
    )
})

Object.keys(filter).forEach(key => {
    Vue.filter(key, filter[key])
});

router.beforeEach((to, from, next) =>{
   if(to.matched.some(function(item){
       return item.meta.loginRequire;
   })) {
       let loginUser = Tool.getLoginUser();
       if(Tool.isEmpty(loginUser)){
           next('/login');
       }else{
           next();
       }
   }else{
       next();
   }
});
new Vue({
    el: '#app',
    router,
    template: '<App/>',
    components: { App }
})
