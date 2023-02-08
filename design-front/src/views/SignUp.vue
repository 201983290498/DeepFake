<template>
  <div id="signUp">
    <div>&nbsp;</div>
    <nav class="navbar navbar-fixed-top content-pd">
      <div class="container-fluid">
        <!--Logo加上商标名-->
        <div class="navbar-header  font-weight-bolder">
          <div class="navbar-brand">
            <img alt="logo" src="@/../static/imgs/logo(1).png" class="logo-pic">
            DeepFake Detector
          </div>
        </div>
        <!--功能栏-->
        <div class="collapse navbar-collapse ">
          <!--主要功能-->
          <ul class="nav navbar-nav mg-auto">
            <li>
              <router-link to="/" active-class="func-active">主站</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <RegisterCard v-show="showCard=='register'" style="margin-top: 80px;"></RegisterCard>
    <ForgetPwd v-show="showCard==='forgetPwd'"></ForgetPwd>
    <div style="height: 100px;"></div>
  </div>
</template>

<script>
import RegisterCard from '@/components/account/RegisterCard.vue'
import ForgetPwd from '@/components/account/ForgetPwd.vue'
export default {
  name: 'SignUp',
  components: {
    ForgetPwd,
    RegisterCard
  },
  data () {
    return {
      showCard: 'register'
    }
  },
  mounted () {
    if (this.$route.params.page) {
      this.showCard = this.$route.params.page
    }
  }
}
</script>

<style scoped lang="less">
@import '@/assets/font/iconfont1/iconfont.css';
// 注册页面的样式
#signUp {
  height: 100%;
  width: 100%;
  //noinspection CssUnknownTarget
  background-image: url('https://s2.loli.net/2022/12/16/VkMoJWY9y6daCq2.jpg');
  background-repeat: no-repeat;
  background-size: cover;
  background-position: 50% 50%;
}
// navbar的样式
.logo-pic{
  float: left;
  margin-top: -6%;
  margin-right: 1rem;
}
.navbar{
  padding: 0.5rem 2rem;
  align-items: center;
  box-shadow: 0 0 0.5rem 0 rgba(230,230,230,50%);
  a {
    font-weight: bold;
    color: #2c3e50;
    &.router-link-exact-active {
      color: #42b983;
      background-color: #eee;
    }
  }
}
.mg-auto{
  position: relative;
  left: auto !important;
}
.navbar-brand{
  font-weight: bold;
  color: #2c3e50;
}
.navbar-header:hover{
  cursor: pointer;
}

</style>
