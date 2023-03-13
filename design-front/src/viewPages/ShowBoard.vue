<template>
  <div class="lyear-layout-web">
    <!--左侧导航-->
    <aside class="lyear-layout-sidebar">
      <!-- logo -->
      <div id="logo" class="sidebar-header">
        <router-link to="/"><img src="@/../static/imgs/logo.png" title="DeepfakeDetector" alt="DeepfakeDetector"/>
          <span class="log-title">
          DeepFake Detector
          </span>
        </router-link>
      </div>
      <div class="lyear-layout-sidebar-scroll">
        <nav class="sidebar-main">
          <ul class="nav nav-drawer">
            <li class="nav-item active"> <router-link to="/showBoard"><i class="mdi mdi-home"></i>控制台</router-link> </li>

            <li class="nav-item nav-item-has-subnav">
              <a href="javascript:void(0)"><i class="mdi mdi-format-align-justify"></i>项目管理</a>
              <ul class="nav nav-subnav">
                <li> <router-link to="/showBoard/addProject">创建项目</router-link> </li>
                <li> <router-link to="/showBoard/historyProjects">项目记录</router-link> </li>
                <li> <router-link to="/showBoard/picBoard">历史图库</router-link></li>
              </ul>
            </li>
            <li class="nav-item nav-item-has-subnav">
              <a href="javascript:void(0)"><i class="mdi mdi-account"></i>个人信息</a>
              <ul class="nav nav-subnav">
                <li> <router-link to="/showBoard/personalInfo">账号信息</router-link> </li>
                <li> <router-link to="/showBoard/forgetPwd">密码修改</router-link></li>
              </ul>
            </li>
          </ul>
        </nav>
        <div class="sidebar-footer">
          <p class="copyright">Copyright &copy; 2023. <router-link  to="/">Deepfake Detector Team</router-link> All rights reserved.</p>
        </div>
      </div>
    </aside>
    <!--End 左侧导航-->
    <!--头部信息-->
    <header class="lyear-layout-header">
      <nav class="navbar navbar-default">
        <div class="topbar">
          <div class="topbar-left">
            <div class="lyear-aside-toggler">
              <span class="lyear-toggler-bar"></span>
              <span class="lyear-toggler-bar"></span>
              <span class="lyear-toggler-bar"></span>
            </div>
            <span class="navbar-page-title">{{activePage}}</span>
          </div>
          <ul class="topbar-right">
            <li class="dropdown dropdown-profile">
              <a data-toggle="dropdown">
                <img class="img-avatar img-avatar-48 m-r-10" :src="user.imageUrl" alt="DF" />
                <span>{{user.username}}<span class="caret"></span></span>
              </a>
              <ul class="dropdown-menu dropdown-menu-right">
                <li> <router-link to="/showBoard"><i class="mdi mdi-home"></i> 控制台</router-link> </li>
                <li> <router-link to="/showBoard/historyProjects"><i class="mdi mdi-format-align-justify"></i> 历史项目</router-link></li>
                <li><router-link to="/showBoard/personalInfo"><i class="mdi mdi-account"></i>个人信息</router-link></li>
                <li class="divider"></li>
                <li> <a @click="loginOut"><i class="mdi mdi-logout-variant"></i> 退出登录</a> </li>
              </ul>
            </li>
            <!--切换主题配色-->
            <li class="dropdown dropdown-skin">
              <span data-toggle="dropdown" class="icon-palette"><i class="mdi mdi-palette"></i></span>
              <ul class="dropdown-menu dropdown-menu-right" data-stopPropagation="true">
                <li class="drop-title"><p>主题</p></li>
                <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="site_theme" value="default" id="site_theme_1" checked>
                    <label for="site_theme_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="site_theme" value="dark" id="site_theme_2">
                    <label for="site_theme_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="site_theme" value="translucent" id="site_theme_3">
                    <label for="site_theme_3"></label>
                  </span>
                </li>
                <li class="drop-title"><p>LOGO</p></li>
                <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="logo_bg" value="default" id="logo_bg_1" checked>
                    <label for="logo_bg_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_2" id="logo_bg_2">
                    <label for="logo_bg_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_3" id="logo_bg_3">
                    <label for="logo_bg_3"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_4" id="logo_bg_4">
                    <label for="logo_bg_4"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_5" id="logo_bg_5">
                    <label for="logo_bg_5"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_6" id="logo_bg_6">
                    <label for="logo_bg_6"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_7" id="logo_bg_7">
                    <label for="logo_bg_7"></label>
                  </span>
                  <span>
                    <input type="radio" name="logo_bg" value="color_8" id="logo_bg_8">
                    <label for="logo_bg_8"></label>
                  </span>
                </li>
                <li class="drop-title"><p>头部</p></li>
                <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="header_bg" value="default" id="header_bg_1" checked>
                    <label for="header_bg_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_2" id="header_bg_2">
                    <label for="header_bg_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_3" id="header_bg_3">
                    <label for="header_bg_3"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_4" id="header_bg_4">
                    <label for="header_bg_4"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_5" id="header_bg_5">
                    <label for="header_bg_5"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_6" id="header_bg_6">
                    <label for="header_bg_6"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_7" id="header_bg_7">
                    <label for="header_bg_7"></label>
                  </span>
                  <span>
                    <input type="radio" name="header_bg" value="color_8" id="header_bg_8">
                    <label for="header_bg_8"></label>
                  </span>
                </li>
                <li class="drop-title"><p>侧边栏</p></li>
                <li class="drop-skin-li clearfix">
                  <span class="inverse">
                    <input type="radio" name="sidebar_bg" value="default" id="sidebar_bg_1" checked>
                    <label for="sidebar_bg_1"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_2" id="sidebar_bg_2">
                    <label for="sidebar_bg_2"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_3" id="sidebar_bg_3">
                    <label for="sidebar_bg_3"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_4" id="sidebar_bg_4">
                    <label for="sidebar_bg_4"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_5" id="sidebar_bg_5">
                    <label for="sidebar_bg_5"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_6" id="sidebar_bg_6">
                    <label for="sidebar_bg_6"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_7" id="sidebar_bg_7">
                    <label for="sidebar_bg_7"></label>
                  </span>
                  <span>
                    <input type="radio" name="sidebar_bg" value="color_8" id="sidebar_bg_8">
                    <label for="sidebar_bg_8"></label>
                  </span>
                </li>
              </ul>
            </li>
            <!--切换主题配色-->
          </ul>
        </div>
      </nav>
    </header>
    <!--End 头部信息-->
    <!--页面主要内容-->
    <main class="lyear-layout-content">
      <router-view @changeActivePage="changeSubPage" @changeImage="changeImage" @oneImageShow="oneImageShow"></router-view>
    </main>
    <!--End 页面主要内容-->
    <OnePictureShow v-if="showImage" :picture="exampleImage" @closeDialog="closeShow" @downLoad="downloadImage"> </OnePictureShow>
  </div>
</template>

<script>
import dashBoard from '@/assets/js/dashBoard'
import common from '@/assets/js/common'
import OnePictureShow from '@/components/dashboard/common/OnePictureShow.vue'
export default {
  name: 'ShowBoard',
  components: { OnePictureShow },
  data () {
    return {
      activePage: '控制台',
      user: JSON.parse(this.$store.state.data),
      showImage: false,
      exampleImage: null
    }
  },
  methods: {
    loginOut: function () {
      localStorage.clear()
      window.location.reload()
    },
    changeSubPage: function (data) {
      this.activePage = data
    },
    changeImage: function (url) {
      this.user.imageUrl = url
    },
    oneImageShow: function (image) {
      this.showImage = true
      this.exampleImage = image
    },
    closeShow: function (resp) {
      this.showImage = resp
    },
    downloadImage: function (resp) {
      common.downLoadPic(resp)
    }
  },
  created () {
    const token = this.$store.state.Authorization
    if (token !== '') {
      const data = new FormData()
      data.append('token', token)
      common.checkToken().then(resp => {
        if (!resp.data.result) {
          localStorage.clear()
          window.location.reload()
        }
      }).catch(() => {
        console.log('服务器异常, 请等待')
      })
    }
    let script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = '/static/js/jquery.min.js'
    document.body.appendChild(script)
    script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = '/static/js/perfect-scrollbar.min.js'
    document.body.appendChild(script)
    script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = '/static/js/Chart.js'
    document.body.appendChild(script)
  },
  mounted () {
    dashBoard.startMain()
  }
}
</script>
<style>
@import '@/assets/css/materialdesignicons.min.css';
@import '@/assets/css/style.min.css';
</style>
<style scoped>
.log-title {
  display: inline-block;
  position: relative;
  top: 4px;
  left: 2px;
  font-weight: bold;
  color: #2c3e50;
  font-size: 18px;
  line-height: 18px;
}
.lyear-layout-web {
  text-align: left;
}
</style>
