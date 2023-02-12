<template>
  <div id="detector">
    <nav class="navbar navbar-fixed-top content-mg">
      <div class="container-fluid">
        <!--Logo加上商标名-->
        <div class="navbar-header  font-weight-bolder">
          <div class="navbar-brand">
            <img alt="logo" src="@/../static/imgs/logo.png" class="logo-pic" >
            DeepFake Detector
          </div>
        </div>
        <!--功能栏-->
        <div class="collapse navbar-collapse ">
          <!--主要功能-->
          <ul class="nav navbar-nav mg-auto">
            <li>
              <router-link to="/detectorPage/deepfakeDetector" active-class="func-active">deepfake篡改检测</router-link>
            </li>
            <li class="active">
              <router-link to="/detectorPage/normalDetector" active-class="func-active">普通篡改检测</router-link>
            </li>
          </ul>
          <!--用户信息栏-->
          <ul class="nav navbar-nav ml-auto">
            <li class="active">
              <a href="/signIn"><img id="headPortrait" class="rounded-circle mr-2" src="@/../static/imgs/av.png" style="width:30px;height: 30px" alt=""><span class="align-middle">{{userStatus}}</span></a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view style="margin-top: 70px; text-align: center" class="content-mg content-pd"></router-view>
    <div class="page-bottom"></div>
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'DetectorPage',
  data () {
    return {
      userStatus: 'Sign in',
      user: {}
    }
  },
  mounted () {
    const _this = this
    if (_this.$store.state.status) {
      _this.user = _this.$store.state.data
      _this.userStatus = _this.user.username
      $('#headPortrait').attr('src', _this.user.imageUrl)
    }
  }
}
</script>

<style lang="less" scoped>
#detector {
  background-color:#F8F8F9 !important
}
.content-mg{
  margin-left: 10%;
  margin-right: 10%;
}
.content-pd{
  padding-left: 10%;
  padding-right: 10%;
  text-align: left;
  padding-top: 20px;
}
.logo-pic{
  float: left;
  margin-top: -6%;
  margin-right: 1rem;
}
.navbar{
  padding: 0.5rem 2rem;
  align-items: center;
  background-color:#fff !important;
  border: 1px solid #f1f1f1 !important;
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
.ml-auto{
  float: right !important;
}
.rounded-circle{
  border-radius: 50%;
}
.mr-2{
  margin-right: 0.66rem;
}
.page-bottom{
  width: 100%;
  height: 50px;
}
</style>
