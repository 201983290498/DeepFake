<template>
  <div class="service-module">
    <div class="module">
      <div class="module-bottom" :class="{'btn-active':isActive1}" @click="accuracyMode">
        <span class="module-text">精准模式</span>
      </div>
      <div class="module-bottom" :class="{'btn-active':isActive2}" v-on:click="speedMode">
        <span class="module-text">极速模式</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ServiceModule',
  data () {
    return {
      isActive1: true,
      isActive2: false
    }
  },
  methods: {
    speedMode: function () {
      this.isActive2 = true
      this.isActive1 = false
      if (!this.$store.state.status) {
        this.$router.push({ path: '/signIn' })
      }
    },
    accuracyMode: function () {
      this.isActive1 = true
      this.isActive2 = false
    }
  }
}
</script>

<style scoped>
.service-module{
  padding-bottom: 25px;
}
.module-bottom{
  display: inline-block;
  margin-left: 20px;
  height: 40px;
  border: 1px solid #B5BBCA;
  width: 150px;
  border-radius:20px;
  color: #4E88BA;
  font-weight: 700;
  text-align: center;
  font-size: 18px;
}
.module-text{
  display: block;
  margin-top: 7px;
}
.module-bottom:hover{
  cursor: pointer;
  background-color: #007FF5;
  color: #fff;
}
.btn-active{
  background-color: #007FF5;
  color: #fff;
}
</style>
