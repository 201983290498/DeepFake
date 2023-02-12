<template>
  <div id="signIn">
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
    <div class="description">
      Sign in to Deepfake Detector
    </div>
<!--    todo 需要写清楚具体的form表单提交的地址-->
    <form action="/" id="form" ref="reluForm">
      <div class="login-container">
        <div class="left-container">
          <div class="title">
            <span>登录</span>
          </div>
          <div class="input-container">
            <input type="text" id="username" name="username" placeholder="用户名" />
            <input type="password" id="password" name="password" placeholder="密码"/>
          </div>
          <div class="message-container">
            <span @click="$router.push({path: '/signUp/forgetPwd'})">忘记密码</span>
          </div>
        </div>
        <div class="right-container">
          <div class="title">
            <span class="regist">注册</span>
          </div>
          <a href="/signUp/register" style="display: none">注册页面</a>
          <div class="photo-container" @click="choosePic">
            <div class="photo" id="photo"></div>
          </div>
          <div class="action-container" id="submit">
            <span>提交</span>
          </div>
          <input type="submit" class="btn" style="display: none">
        </div>
      </div>
    </form>
    <input type="file" name="photo" style="display: none;" v-on:change="readPic"/>
    <div style="height: 150px;"> </div>
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'SignIn',
  data () {
    return {
      userUrl: window.server.COMMONS.userUrl,
      imageUrl: window.server.COMMONS.imageUrl
    }
  },
  methods: {
    getPhoto: function (event) {
      const _this = this
      $.ajax({
        url: _this.userUrl + '/account/isExist?account=' + event.target.value,
        method: 'get',
        dataType: 'json',
        success (resp) {
          if (resp.result && resp.data != null) {
            const url = resp.data.imageUrl
            const context = `<img src='${url}' style="width:100%;height: 100%;margin-top: -11.5px"/>`
            $('#photo').html(context)
          }
        },
        error () {
          alert('用户不存在')
        }
      })
    },
    choosePic: function () {
      $('input[name=photo]').click()
    },
    readPic: function (event) {
      if (event.target.files.length === 0) {
        $('#photo').html('&#xe65b;')
      } else {
        const file = event.target.files[0]
        if (!/^image\/\w*$/.test(file.type)) {
          event.target.value = ''
          alert('上传的并非图片,请重新选择')
        } else {
          const rd = new FileReader()
          rd.readAsDataURL(file) // 记载图片，加载完成之后
          rd.onload = function (e) {
            const url = e.target.result
            const context = `<img src='${url}' style="width:100%;height:100%;margin-top: -11.5px"/>`
            $('#photo').html(context)
          }
        }
      }
    }
  },
  mounted () {
    const _this = this
    $('#photo').html('&#xe65b;')
    $('#photo').addClass('empty-photo')
    $('#photo').addClass('iconfont')
    $('#username').change((event) => {
      if ($('#username').val() !== '') {
        _this.getPhoto(event)
      } else { // 加载空白头像框
        $('#photo').html('&#xe65b;')
      }
    })
    $('.left-container .title span').addClass('font_underline')
    $('#submit').click(() => {
      $.ajax({
        type: 'post',
        url: _this.userUrl + '/login',
        data: new FormData(_this.$refs.reluForm),
        processData: false,
        contentType: false,
        success: function (resp) {
          resp = JSON.parse(resp)
          if (resp.result) {
            window.server.STATUS = true
            _this.$store.commit('saveData', resp.data)
            const token = 'Bearer ' + resp.data.token
            _this.$store.commit('changeLogin', token)
            _this.$store.commit('changeStatus', true)
            _this.$message.success('登入成功!')
            _this.$router.push({ path: '/' })
          }
        },
        error: function () {
          _this.$message.warning('账号密码错误!')
        }
      })
    })
    $('.right-container .title').click(() => {
      $('.right-container a')[0].click()
    })
    $('.right-container .title span').hover(function () {
      $('.right-container .title span').addClass('font_underline')
      $('.left-container .title span').removeClass('font_underline')
    }, function () {
      $('.right-container .title span').removeClass('font_underline')
      $('.left-container .title span').addClass('font_underline')
    })
  }
}
</script>

<style scoped lang="less">
@import '@/assets/font/iconfont1/iconfont.css';
#signIn {
  height: 100%;
  width: 100%;
  background-image: url('https://s2.loli.net/2022/12/16/VkMoJWY9y6daCq2.jpg');
  background-repeat: no-repeat;
  background-size: cover;
  background-position: 50% 50%;
}
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
.description {
  margin-top: 100px;
  font-weight: bold;
  font-size: 30px;
  color: #343A40;
}
#signIn .login-container{
  border-radius: 15px;
  background-color: rgb(95,76,194);
  margin: 30px auto 0;
  display: inline-block;
}
#signIn .left-container{
  display: inline-block;
  width: 480px;
  border-top-left-radius: 15px;
  border-bottom-left-radius: 15px;
  padding: 60px;
  background-image: linear-gradient(to bottom right,rgb(118,76,163),rgb(92,103,211));
}
#signIn .left-container .title{
  color: #fff;
  font-size: 18px;
  font-weight: 200;
  text-align: left;
}
#signIn .left-container .input-container{
  padding: 20px 0;
}
#signIn .left-container .input-container input{
  border:0;
  background: none;
  outline: 0;
  color:#fff;
  margin: 20px 0;
  display: block;
  width: 100%;
  padding: 5px 0;
  transition: .2s;
  border-bottom: 1px solid rgb(199,191,219);
}
#signIn .left-container .input-container input:hover{
  border-bottom-color: #fff;
}
::-webkit-input-placeholder{
  color:rgb(199,191,219);
}
#signIn .left-container .message-container{
  font-size: 14px;
  transition: .2s;
  color: rgb(199,191,219);
  cursor: pointer;
  text-align: left;
}
#signIn .left-container .message-container:hover{
  color: #fff;
}
#signIn .right-container{
  width: 145px;
  vertical-align: top;
  display: inline-block;
  padding: 60px 0 55px;
}
#signIn .right-container .title{
  text-align: center;
  color:#fff;
  font-size: 18px;
  font-weight: 200;
  transition: .2s;
}
.font_underline{
  border-bottom: 3px solid rgb(237,221,22);
  font-weight: 400;
  cursor: pointer;
  transition: .2s;
}
#signIn .right-container .photo-container{
  padding: 20px 0 0;
  width: 100%;
  margin-top: 20px;
}
#signIn .right-container .photo-container .photo{
  width: 70px;
  height: 70px;
  overflow: hidden;
  display: block;
  background-color: rgba(225, 225, 225, 0.137);
  border-radius: 35px;
  margin: 0 auto;
  font-size: 40px;
  font-weight: 600;
}

#signIn  .right-container .action-container{
  font-size: 12px;
  color: #fff;
  position: relative;
}
#signIn .login-container  .right-container .action-container span{
  border: 1px solid rgb(237,221,22);
  padding: 10px;
  display: inline-block;
  margin: 30px auto 0;
  line-height: 25px;
  border-radius: 25px;
  transition: .4s;
  cursor: pointer;
  vert-align: top;
}
#signIn .login-container .right-container .action-container span:hover{
  background-color: rgb(237,221,22);
  color: rgb(95,76,194);
  transition: .4s;
}
.empty-photo{
  text-align: center;
  line-height: 75px;
  color: rgb(199,191,219);
}
</style>
