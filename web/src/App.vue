<template>
    <div>
        <Header></Header>
        <Footer></Footer>
    </div>
</template>

<script>

import Header from "./components/header";
import Footer from "./components/footer";
export default {
    name: 'App',
    components: {Header, Footer}


}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
