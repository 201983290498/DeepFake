<template>
  <div id="signIn">
<!--    todo 需要写清楚具体的form表单提交的地址-->
    <form action="/" id="form" method="post">
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
            <!-- TODO modal框实现找回密码 -->
            <span>忘记密码</span>
          </div>
        </div>
        <div class="right-container">
          <div class="regist-container">
            <span class="regist">注册</span>
          </div>
<!--          todo 需要转到register页面-->
          <a href="/signUp" style="display: none">注册页面</a>
          <div class="photo-container">
            <div class="photo" id="photo"></div>
          </div>
          <div class="action-container" id="submit">
            <span>提交</span>
          </div>
          <input type="submit" class="btn" style="display: none">
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'SignIn',
  methods: {
    getPhoto: function (event) {
      $.ajax({
        url: $('#form').attr('action') + '/username?account=' + event.target.value,
        method: 'get',
        dataType: 'json',
        success (resp) {
          if (resp.result && resp.data != null) {
            const url = '/images/' + resp.data.imageId
            const context = `<img src='${url}' style="width:65px;height:65px;"/>`
            $('#photo').html(context)
          }
        },
        error () {
          alert('用户不存在')
        }
      })
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
    $('#submit').click(() => {
      $('#submit + .btn').click()// 代替提交表单的结果
    })
    $('.regist-container').click(() => {
      $('.regist-container + a')[0].click()
    })
  }
}
</script>

<style scoped>
*{
  padding: 0;
  margin: 0;
}
#signIn {
  height: 100%;
  width: 100%;
  background-image:linear-gradient(to bottom right,rgb(114,135,254),rgb(130,88,186));
}
#signIn .login-container{
  width: 650px;
  height: 315px;
  border-radius: 15px;
  box-shadow: 0 10px 50px 0px rgb(59,45,159);
  background-color: rgb(95,76,194);
  position: absolute;
  left: 50%;
  top: 40%;
  transform: translate(-50%,-50%);
}
#signIn .login-container .left-container{
  display: inline-block;
  width: 380px;
  border-top-left-radius: 15px;
  border-bottom-right-radius: 15px;
  padding: 60px;
  background-image: linear-gradient(to bottom right,rgb(118,76,163),rgb(92,103,211));
}
#signIn .login-container .left-container .title{
  color: #fff;
  font-size: 18px;
  font-weight: 200;
}
#signIn .login-container .left-container .title span{
  border-bottom: 3px solid rgb(237,221,22);
}
#signIn .login-container .left-container .input-container{
  padding: 20px 0;
}
#signIn .login-container .left-container .input-container input{
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
#signIn .login-container .left-container .input-container input:hover{
  border-bottom-color: #fff;
}
::-webkit-input-placeholder{
  color:rgb(199,191,219);
}
#signIn .login-container .left-container .message-container{
  font-size: 14px;
  transition: .2s;
  color: rgb(199,191,219);
  cursor: pointer;
}
#signIn .login-container .left-container .message-container:hover{
  color: #fff;
}

#signIn .login-container .right-container{
  width: 145px;
  display: inline-block;
  height: calc(100% - 120px);
  vertical-align: top;
  padding: 60px 0;
}
#signIn .login-container .right-container .regist-container{
  text-align: center;
  color:#fff;
  font-size: 18px;
  font-weight: 200;
  transition: .2s;
}
#signIn .login-container .right-container .regist-container:hover{
  font-weight: 400;
  cursor: pointer;
}
#signIn .login-container .right-container .photo-container{
  padding: 20px 0;
  width: 100%;
  height: 153px;
}
#signIn .login-container .right-container .photo-container .photo{
  width: 65px;
  height: 65px;
  overflow: hidden;
  display: block;
  background-color: rgba(225, 225, 225, 0.137);
  border-radius: 33px;
  margin: 20px auto 0;
  font-size: 30px;
  font-weight: 600;
}

#signIn .login-container .right-container .regist-container span{
  border-bottom: 3px solid rgb(237,221,22);
}

#signIn .login-container  .right-container .action-container{
  font-size: 10px;
  color: #fff;
  height: calc(100% - 190px);
  position: relative;
}
#signIn .login-container  .right-container .action-container span{
  border: 1px solid rgb(237,221,22);
  padding: 10px;
  display: inline;
  line-height: 25px;
  border-radius: 25px;
  position: absolute;
  bottom: 10px;
  left: calc(72px - 20px);
  transition: .2s;
  cursor: pointer;
}
#signIn .login-container .right-container .action-container span:hover{
  background-color: rgb(237,221,22);
  color: rgb(95,76,194);
}

.empty-photo{
  text-align: center;
  line-height: 65px;
  color: rgb(199,191,219);
  transition: .4s;
}
</style>
