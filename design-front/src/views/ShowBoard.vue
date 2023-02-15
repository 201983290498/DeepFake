<template>
  <div class="lyear-layout-web">
    <!--左侧导航-->
    <aside class="lyear-layout-sidebar">
      <!-- logo -->
      <div id="logo" class="sidebar-header">
        <a href="/"><img src="@/../static/imgs/logo.png" title="DeepfakeDetector" alt="DeepfakeDetector"/>
          <span class="log-title">
          DeepFake Detector
          </span>
        </a>
      </div>
      <div class="lyear-layout-sidebar-scroll">
        <nav class="sidebar-main">
          <ul class="nav nav-drawer">
            <li class="nav-item active"> <a href="/showBoard"><i class="mdi mdi-home"></i>控制台</a> </li>
            <li class="nav-item nav-item-has-subnav">
              <a href="/showBoard/runningProject"><i class="mdi mdi-palette"></i>运行项目</a>
            </li>
            <li class="nav-item nav-item-has-subnav">
              <a href="/showBoard/historyProject"><i class="mdi mdi-format-align-justify"></i>历史项目</a>
            </li>
            <li class="nav-item nav-item-has-subnav">
              <a href="/showBoard/historyProject"><i class="mdi mdi-account"></i>个人信息</a>
            </li>
          </ul>
        </nav>
        <div class="sidebar-footer">
          <p class="copyright">Copyright &copy; 2023. <a target="_blank" href="/">Deepfake Detector Team</a> All rights reserved.</p>
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
                <img class="img-avatar img-avatar-48 m-r-10" :src="user.imageUrl" alt="伪造检测" />
                <span>{{user.username}}<span class="caret"></span></span>
              </a>
              <ul class="dropdown-menu dropdown-menu-right">
                <li> <a href="/showBoard"><i class="mdi mdi-home"></i> 控制台</a> </li>
                <li> <a href="/showBoard/runningProject"><i class="mdi mdi-palette"></i> 运行项目</a> </li>
                <li> <a href="/showBoard/historyProject"><i class="mdi mdi-format-align-justify"></i> 历史项目</a></li>
                <li><a href="/showBoard/historyProject"><i class="mdi mdi-account"></i>个人信息</a></li>
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

      <div class="container-fluid">

        <div class="row">
          <div class="col-sm-6 col-lg-3">
            <div class="card bg-primary">
              <div class="card-body clearfix">
                <div class="pull-right">
                  <p class="h6 text-white m-t-0">今日收入</p>
                  <p class="h3 text-white m-b-0 fa-1-5x">102,125.00</p>
                </div>
                <div class="pull-left"> <span class="img-avatar img-avatar-48 bg-translucent"><i class="mdi mdi-currency-cny fa-1-5x"></i></span> </div>
              </div>
            </div>
          </div>

          <div class="col-sm-6 col-lg-3">
            <div class="card bg-danger">
              <div class="card-body clearfix">
                <div class="pull-right">
                  <p class="h6 text-white m-t-0">用户总数</p>
                  <p class="h3 text-white m-b-0 fa-1-5x">920,000</p>
                </div>
                <div class="pull-left"> <span class="img-avatar img-avatar-48 bg-translucent"><i class="mdi mdi-account fa-1-5x"></i></span> </div>
              </div>
            </div>
          </div>

          <div class="col-sm-6 col-lg-3">
            <div class="card bg-success">
              <div class="card-body clearfix">
                <div class="pull-right">
                  <p class="h6 text-white m-t-0">下载总量</p>
                  <p class="h3 text-white m-b-0 fa-1-5x">34,005,000</p>
                </div>
                <div class="pull-left"> <span class="img-avatar img-avatar-48 bg-translucent"><i class="mdi mdi-arrow-down-bold fa-1-5x"></i></span> </div>
              </div>
            </div>
          </div>

          <div class="col-sm-6 col-lg-3">
            <div class="card bg-purple">
              <div class="card-body clearfix">
                <div class="pull-right">
                  <p class="h6 text-white m-t-0">新增留言</p>
                  <p class="h3 text-white m-b-0 fa-1-5x">153 条</p>
                </div>
                <div class="pull-left"> <span class="img-avatar img-avatar-48 bg-translucent"><i class="mdi mdi-comment-outline fa-1-5x"></i></span> </div>
              </div>
            </div>
          </div>
        </div>

        <div class="row">

          <div class="col-lg-6">
            <div class="card">
              <div class="card-header">
                <h4>每周用户</h4>
              </div>
              <div class="card-body">
                <canvas class="js-chartjs-bars"></canvas>
              </div>
            </div>
          </div>

          <div class="col-lg-6">
            <div class="card">
              <div class="card-header">
                <h4>交易历史记录</h4>
              </div>
              <div class="card-body">
                <canvas class="js-chartjs-lines"></canvas>
              </div>
            </div>
          </div>

        </div>

        <div class="row">

          <div class="col-lg-12">
            <div class="card">
              <div class="card-header">
                <h4>项目信息</h4>
              </div>
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                    <tr>
                      <th>#</th>
                      <th>项目名称</th>
                      <th>开始日期</th>
                      <th>截止日期</th>
                      <th>状态</th>
                      <th>进度</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                      <td>1</td>
                      <td>设计新主题</td>
                      <td>10/02/2019</td>
                      <td>12/05/2019</td>
                      <td><span class="label label-warning">待定</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-warning" style="width: 45%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>2</td>
                      <td>网站重新设计</td>
                      <td>01/03/2019</td>
                      <td>12/04/2019</td>
                      <td><span class="label label-success">进行中</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-success" style="width: 30%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>3</td>
                      <td>模型设计</td>
                      <td>10/10/2019</td>
                      <td>12/11/2019</td>
                      <td><span class="label label-warning">待定</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-warning" style="width: 25%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>4</td>
                      <td>后台管理系统模板设计</td>
                      <td>25/01/2019</td>
                      <td>09/05/2019</td>
                      <td><span class="label label-success">进行中</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-success" style="width: 55%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>5</td>
                      <td>前端设计</td>
                      <td>10/10/2019</td>
                      <td>12/12/2019</td>
                      <td><span class="label label-danger">未开始</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-danger" style="width: 0%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>6</td>
                      <td>桌面软件测试</td>
                      <td>10/01/2019</td>
                      <td>29/03/2019</td>
                      <td><span class="label label-success">进行中</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-success" style="width: 75%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>7</td>
                      <td>APP改版开发</td>
                      <td>25/02/2019</td>
                      <td>12/05/2019</td>
                      <td><span class="label label-danger">暂停</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-danger" style="width: 15%;"></div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td>8</td>
                      <td>Logo设计</td>
                      <td>10/02/2019</td>
                      <td>01/03/2019</td>
                      <td><span class="label label-warning">完成</span></td>
                      <td>
                        <div class="progress progress-striped progress-sm">
                          <div class="progress-bar progress-bar-success" style="width: 100%;"></div>
                        </div>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

        </div>

      </div>

    </main>
    <!--End 页面主要内容-->
  </div>
</template>

<script>
export default {
  name: 'ShowBoard',
  data () {
    return {
      activePage: '控制台',
      user: JSON.parse(this.$store.state.data)
    }
  },
  methods: {
    loginOut: function () {
      localStorage.clear()
      window.location.reload()
    }
  },
  created () {
    const token = this.$store.state.Authorization
    if (token !== '') {
      const data = new FormData()
      data.append('token', token)
      this.axios({
        url: '/api/authorize',
        method: 'post',
        data: data
      }).then(resp => {
        if (!resp.data.result) {
          localStorage.removeItem('data')
        }
      }).catch(() => {
        localStorage.removeItem('data')
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
    script.src = '/static/js/main.min.js'
    document.body.appendChild(script)
    script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = '/static/js/Chart.js'
    document.body.appendChild(script)
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
