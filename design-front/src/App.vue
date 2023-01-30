<template >
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
// name 属性指向的是当前组件的名称(建议:每个单词的首字母大写)
  name: 'App'
}
</script>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  s-webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
