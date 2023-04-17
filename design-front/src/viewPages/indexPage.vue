<template>
  <div id="detector">
    <nav class="navbar navbar-fixed-top content-mg">
      <div class="container-fluid">
        <!--Logo加上商标名-->
        <div class="navbar-header  font-weight-bolder">
          <div class="navbar-brand">
            <router-link to="/" style="color: #4d5259; text-decoration: none; outline: none">
              <img alt="logo" src="@/../static/imgs/logo.png" class="logo-pic" >
              DeepFake Detector
            </router-link>
          </div>
        </div>
        <!--功能栏-->
        <div class="collapse navbar-collapse">
          <!--主要功能-->
          <ul class="nav navbar-nav mg-auto">
            <li class="active">
              <router-link to="/detectorPage/deepfakeDetector" active-class="func-active">deepfake篡改检测</router-link>
            </li>
<!--            <li class="active">-->
<!--              <router-link to="/detectorPage/normalDetector" active-class="func-active">普通篡改检测</router-link>-->
<!--            </li>-->
          </ul>
          <!--用户信息栏-->
          <ul class="nav navbar-nav ml-auto">
            <li class="active" v-show="!status">
              <router-link to="/signIn"><img class="rounded-circle mr-2" src="@/../static/imgs/av.png" style="width:30px;height: 30px" alt=""><span class="align-middle">{{userStatus}}</span></router-link>
            </li>
            <li class="active dropdown" v-show="status">
              <a class="dropdown-toggle" data-toggle="dropdown"><img id="headPortrait" class="rounded-circle mr-2" src="@/../static/imgs/av.png" style="width:30px;height: 30px" alt=""><span class="align-middle">{{userStatus}}<span class="caret"></span></span></a>
              <ul class="dropdown-menu dropdown-menu-right">
                <li> <router-link to="/showBoard"><i class="mdi mdi-home"></i> 控制台</router-link> </li>
<!--                <li> <router-link to="/showBoard/runningProject"><i class="mdi mdi-palette"></i> 运行项目</router-link> </li>-->
                <li> <router-link to="/showBoard/historyProjects"><i class="mdi mdi-format-align-justify"></i> 历史项目</router-link></li>
                <li class="divider"></li>
                <li> <a @click="loginOut"><i class="mdi mdi-logout-variant"></i> 退出登录</a> </li>
              </ul>
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
import common from '@/assets/js/common'
export default {
  name: 'DetectorPage',
  data () {
    return {
      userStatus: 'Sign in',
      user: {},
      status: JSON.parse(this.$store.state.status)
    }
  },
  created () {
    common.checkToken().then(resp => {
      if (!resp.data.result) {
        localStorage.clear()
      }
    }).catch(() => {
      console.log('服务器异常请稍后重试')
    })
  },
  methods: {
    loginOut: function () {
      localStorage.clear()
      window.location.reload()
    }
  },
  mounted () {
    const _this = this
    if (_this.status) {
      _this.user = JSON.parse(_this.$store.state.data)
      _this.userStatus = _this.user.username
      $('#headPortrait').attr('src', _this.user.imageUrl)
    }
  }
}
</script>

<style lang="less" scoped>
@import '@/assets/css/materialdesignicons.min.css';
@import '@/assets/css/style.min.css';
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
.dropdown-menu li{
  margin-bottom: 5px;
  font-size: 16px;
}
</style>
