<template>
  <div id="registerCard">
    <div class="description">
      Sign up to Deepfake Detector
    </div>
    <form id="form" action="/" enctype="multipart/form-data" ref="reluForm">
      <div class="login-container">
        <div class="left-container">
          <div class="title" style="text-align:left;">
            <span>注册</span>
          </div>
          <div class="input-container">
            <div class="form-group">
              <input type="text" id="username" name="username" placeholder="用户名" />
              <div class="index-item" id="accountMsg">
                <div>
                  <div class="tip fd"></div>
                </div>
              </div>
            </div>
            <div class="form-group">
              <!--              todo 使用data-tip存储数据-->
              <input class="password" type="password" id="pwd" name="password" placeholder="密码" data-tip="pwdMsg" data-pwd="repeatPwd"/>
              <div class="index-item" id="pwdMsg">
                <label class="control-label"></label>
                <div class="tip fd"></div>
              </div>
            </div>
            <div class="form-group">
              <input class="password" type="password" name="repeatPwd" id="repeatPwd" placeholder="重复密码" data-tip="repeatPwdMsg" data-pwd="pwd"/>
              <div class="index-item" id="repeatPwdMsg">
                <div class="tip fd"></div>
              </div>
            </div>
            <div class="form-group">
              <input type="text" name="email" placeholder="邮箱" id="email"/>
              <div class="index-item" id="emailMsg">
                <label class="control-label"></label>
                <div class="tip fd"></div>
              </div>
            </div>
            <div class="form-group">
              <div class="validateMsgBox">
                <input type="text" name="validateData" placeholder="验证码" style="display: inline-block;width: 61.8%;border-bottom-right-radius: 0;border-top-right-radius: 0;"  id="validateData"/>
                <input type="button" value="发送验证信息" style="display: inline-block;width: 38.2%; border-bottom-left-radius: 0;border-top-left-radius: 0;" @/>
              </div>
              <div class="index-item" id="validateMsg">
                <label class="control-label"></label>
                <div class="tip fd"></div>
              </div>
            </div>
          </div>
          <div class="message-container">
            <span @click="$router.push({path: '/signIn'})">返回登录</span>
          </div>
        </div>
        <div class="right-container">
          <div class="title">
            <span>上传头像</span>
          </div>
          <div class="photo-container" id="photo-container" v-on:click="chooseFile">
            <div class="photo" id="photo"></div>
          </div>
          <input type="file" name="photo" style="display: none;" v-on:change="readPic"/>
          <div class="action-container" id="submit">
            <span>注册</span>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import $ from 'jquery'
import common from '@/assets/js/common'
export default {
  name: 'RegisterCard',
  data () {
    return {
      userUrl: window.server.COMMONS.userUrl,
      validEmail: false
    }
  },
  methods: {
    checkPwd: function (value) {
      const pattern = /^[A-Z]/
      const pattern2 = /[a-z]/
      return pattern.test(value) && pattern2.test(value) && /[0-9]/.test(value) && /\w{6,16}/.test(value)
    },
    checkAccount: function (value) {
      return /^[a-zA-Z]\w{5,17}$/.test(value)
    },
    checkEmail: function (value) {
      return /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)
    },
    chooseFile: function () {
      $('input[name=photo]').click()
    },
    readPic: function (event) {
      const _this = this
      if (event.target.files.length === 0) {
        $('#photo').html('&#xe65b;')
        return
      }
      const file = event.target.files[0]
      if (!/^image\/\w*$/.test(file.type)) {
        event.target.value = ''
        _this.$message.warning('上传的并非图片,请重新选择')
        return
      }
      const rd = new FileReader()
      rd.readAsDataURL(file) // 记载图片，加载完成之后
      rd.onload = function (e) {
        const url = e.target.result
        const context = `<img src='${url}' style="width:100%;height:100%;margin-top: -11.5px" alt=""/>`
        $('#photo').html(context)
      }
    }
  },
  mounted () {
    const _this = this
    $('input[type=button]').addClass('avoid')
    $('#photo').html('&#xe65b;')
    $('#photo').addClass('empty-photo')
    $('#photo').addClass('iconfont')
    // 用户名获取焦点事件
    $('#username').focus(function () {
      const msg = '6～18个字符，可使用字母、数字、下划线，需要以字母开头'
      $('#accountMsg .tip').empty().text(msg)
      $('#accountMsg').removeClass('success error')
      $('#accountMsg').addClass('focus')
    })
    // 用户名失去焦点事件
    $('#username').blur(function () {
      const account = this.value
      // 正则检验 输入的账号是否合法
      $('#accountMsg').removeClass('focus')
      if (account) {
        let msg
        let style
        if (_this.checkAccount(account)) { // 查看样式是否符号规范
          $.ajax({
            url: _this.userUrl.checkAccount + '?account=' + account,
            method: 'get',
            dataType: 'json',
            success: (rs) => {
              msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + ((!rs.result) ? '恭喜，该用户名可以注册' : '用户名重复')
              style = (!rs.result) ? 'success' : 'error'
              if (style === 'success') {
                $('#username').removeClass('error')
              } else {
                $('#username').addClass('error')
              }
              $('#accountMsg').addClass(style)
              $('#accountMsg .tip').empty().html(msg)
            }
          })
        } else {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长度应为6~18个字符'
          style = 'error'
          $('#username').addClass('error')
          $('#accountMsg').addClass(style)
          $('#accountMsg .tip').empty().html(msg)
        }
      }
    })
    $('.password').focus(function () {
      const msg = '6～16个字符，首字母必须大写,并包含数字和字母'
      const tip = this.dataset.tip
      $(`#${tip} .tip`).empty().text(msg)// 将内容清空
      $(`#${tip}`).removeClass('success error')
      $(`#${tip}`).addClass('focus')
    })
    // 密码框失去焦点
    $('.password').blur(function () {
      const tip = this.dataset.tip // 显示标签
      const pwd = this.dataset.pwd // 另一个框的id
      const tip2 = $(`#${pwd}`).data('tip')
      $(`#${tip}`).removeClass('focus success error')
      $(`#${tip2}`).removeClass('focus success error')
      const val1 = this.value
      const val2 = $(`#${pwd}`).val() // 另外一个输入框的密码
      if (val1 !== '') { // 输入的内容需要验证
        let msg
        let style
        if (_this.checkPwd(val1) && val1 === val2) {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;恭喜，该密码可用!'
          style = 'success'
          $('.password').removeClass('error')
        } else if (_this.checkPwd(val1)) {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;两次输入的密码不同!'
          style = 'error'
          $('.password').addClass('error')
        } else {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;首写字母必须大写，并包含数字和字母!'
          style = 'error'
          $('.password').addClass('error')
        }
        $(`#${tip}`).addClass(style)
        $(`#${tip} .tip`).empty().html(msg)
        $(`#${tip2}`).addClass(style)
        $(`#${tip2} .tip`).empty().html(msg)
      }
    })
    // 邮箱的聚焦和失去焦点
    $('input[name=email]').focus(() => {
      const msg = '邮箱地址: xx@xx.xx'
      $('#emailMsg').removeClass('success')
      $('#emailMsg').removeClass('error')
      $('#emailMsg').addClass('focus')
      $('#emailMsg .tip').empty().text(msg)
    })
    $('input[name=email]').blur(function () {
      $('#emailMsg').removeClass('focus')
      const value = this.value
      let style
      let msg
      if (value && _this.checkEmail(value)) {
        msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该邮箱格式有效!'
        style = 'success'
        $('input[name=email]').removeClass('error')
      } else if (value !== '') {
        msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;邮箱格式错误!'
        style = 'error'
        $('input[name=email]').addClass('error')
      }
      if (style === 'success') {
        $.ajax({
          url: _this.userUrl.checkEmail + '?email=' + value,
          type: 'get',
          dataType: 'json',
          success: function (resp) {
            style = resp.result ? 'error' : 'success'
            if (style === 'success') {
              $('input[name=email]').removeClass('error')
            } else {
              $('input[name=email]').addClass('error')
            }
            if (resp.result) {
              // 用户已经存在
              msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + resp.data
              _this.validEmail = false
            } else {
              _this.validEmail = true
              $('input[type=button]').removeClass('avoid')
            }
            $('#emailMsg').addClass(style)
            $('#emailMsg .tip').empty().html(msg)
          }
        })
      } else {
        $('#emailMsg').addClass(style)
        $('#emailMsg .tip').empty().html(msg)
      }
    })
    // 验证码
    $('input[name=validateData]').focus(() => {
      const msg = '6位验证码'
      $('#validateMsg').removeClass('success error')
      $('#validateMsg').addClass('focus')
      $('#validateMsg .tip').empty().text(msg)
    })
    $('input[name=validateData]').blur(function () {
      const message = this.value
      const email = $('input[name=email]').val()
      $('#validateMsg').removeClass('focus')
      if (message && message.length === 6 && _this.checkEmail(email)) {
        common.checkValidationMsg(email, message, window.CONSTANT.EMAIL.register).then(function (rq) {
          const style = rq.data.result ? 'success' : 'error'
          if (rq.data.result) {
            $('input[name=validateData]').removeClass('error')
          } else {
            $('input[name=validateData]').addClass('error')
          }
          const msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + (rq.data.result ? '验证码通过!' : '验证码错误!')
          $('#validateMsg').addClass(style)
          $('#validateMsg .tip').empty().html(msg)
        })
      }
    })
    $('#submit').on('click', function () {
      let flag = 0
      flag = flag + (($('#username').hasClass('error') || $('#username').val() === '') ? 1 : 0)
      flag = flag + (($('#pwd').hasClass('error') || $('#pwd').val() === '') ? 1 : 0)
      flag = flag + (($('#repeatPwd').hasClass('error') || $('#repeatPwd').val() === '') ? 1 : 0)
      flag = flag + (($('#email').hasClass('error') || $('#email').val() === '') ? 1 : 0)
      flag = flag + (($('#validateData').hasClass('error') || $('#validateData').val() === '') ? 1 : 0)
      if (!_this.validEmail) {
        _this.$message.warning('邮箱还未校验成功, 请稍后尝试!')
        return
      }
      if (flag === 0) {
        $.ajax({
          url: _this.userUrl.register,
          type: 'post',
          data: new FormData(_this.$refs.reluForm),
          processData: false,
          contentType: false,
          success: function (resp) {
            resp = JSON.parse(resp)
            if (resp.result) {
              _this.$message.success('注册成功。')
              _this.$router.push({ path: '/signIn' })
            } else {
              _this.$message.warning(resp.msg)
            }
          },
          error: function () {
            _this.$message.warning('注册失败, 请稍后重试.')
          }
        })
      } else {
        _this.$message.warning('注册信息错误!')
      }
    })
    $('input[type=button]').click(function () {
      if (!$('input[type=button]').hasClass('avoid')) {
        common.sendValidationMsg($('#email').val(), window.CONSTANT.EMAIL.register, $('input[type=button]'))
      }
    })
  }
}
</script>

<style scoped lang="less">
#registerCard {
  margin-bottom: 50px;
}
// 输入框的样式
.description {
  font-weight: bold;
  font-size: 30px;
  color: #343A40;
}
#signUp .login-container{
  border-radius: 15px;
  background-color: #fff;
  margin: 10px auto 0;
  display: inline-block;
}
#signUp .left-container{
  display: inline-block;
  width: 480px;
  border-top-left-radius: 15px;
  border-bottom-left-radius: 15px;
  padding: 60px 60px 40px 60px;
}
#signUp .title span{
  font-size: 20px;
  color: #343A40;
  font-family: Inter,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,Noto Sans,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol,Noto Color Emoji;
  font-weight: 700 !important;
  border-bottom: 3px solid rgba(52,58,64,0.6);
  cursor: pointer;
  text-align: center;
}
#signUp .left-container .input-container{
  padding: 30px 0 20px;
}
#signUp input{
  background-color: #F5F5F5;
  width: 100%;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  -webkit-appearance: none;
  height: 40px;
  line-height: 40px;
  outline: 0;
  padding: 0 15px;
  color: #495057;
  transition: border-color .2s cubic-bezier(.645,.045,.355,1);
  font-size: 16px;
  font-weight: 550;
  font-family: Inter,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,Noto Sans,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol,Noto Color Emoji;
  margin-bottom: 4px;
}
#signUp input:focus{
  border: 1.5px solid #1AA9FF;
  background-color: #E5E5E5;
  color: #000;
}
#signUp input.error{
  border: 1.5px solid rgb(255, 0, 0);
}
#signUp .left-container .message-container{
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
  text-align: center;
}
#signUp .left-container .message-container:hover{
  color: #2C96C4;
  transition: .2s;
}
#signUp .right-container{
  width: 145px;
  vertical-align: top;
  display: inline-block;
  padding: 60px 0 60px;
  position: relative;
}
#signUp .right-container .photo-container{
  padding: 20px 0 0;
  width: 100%;
  margin-top: 20px;
  cursor: pointer;
}
#signUp .right-container .photo-container .photo{
  width: 90px;
  height: 90px;
  overflow: hidden;
  display: block;
  background-color: #F5F5F5;
  border-radius: 45px;
  margin: 0 auto;
  font-size: 54px;
  font-weight: 600;
}
#signUp .right-container .photo-container .photo:hover{
  background-color: rgba(225, 225, 225, 0.137);
  cursor: pointer;
}
#signUp  .right-container .action-container{
  font-size: 18px;
  font-weight: 600;
  margin: 197px auto -70px;
}
#signUp .login-container  .right-container .action-container span{
  padding: 10px;
  display: inline-block;
  line-height: 35px;
  border-radius: 30px;
  transition: .4s;
  cursor: pointer;
  vert-align: top;
}
#signUp .login-container .right-container .action-container span:hover{
  background-color: rgba(17, 126, 248, 0.89);
  color: rgba(255, 255, 255);
  transition: .4s;
  border: 1px solid rgb(52, 58, 64);
}
.empty-photo{
  text-align: center;
  color: rgb(199,191,219);
  line-height: 94px;
}
#signUp .left-container .form-group input[type=button]  {
  cursor: pointer;
}
#signUp .form-group{
  position: relative;
}
// 设置提示语的特点
#signUp .index-item{
  position: absolute;
  top: 40px;
  left: 10px;
  height: 17px;
  line-height: 17px;
  width: 100%;
  font-size: 13px;
  text-align: left;
}
#pwdMsg, #emailMsg, #validateMsg{
  top: 23px !important;
}
#signUp .form-group .index-item.focus .tip {
  display: block;
  color: #9e9e9e;
}
#signUp .form-group .index-item.success .tip{
  display: block;
  color: #33a853;
  background-size: 15px 15px;
}
#signUp .form-group .index-item.error .tip{
  display: block;
  color: #ff5b5b;
  background-size: 15px 15px;
}
#signUp .form-group .index-item .fd{
  display: none;
}
#signUp .left-container .form-group input[type=button].avoid{
  cursor: default;
  border: 1px solid;
  border-radius: 10px;
  background: fixed rgb(61, 60, 60);
}
.form-group .success .tip{
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAMAAAAM7l6QAAAAXVBMVEVHcEw3rVc0qVQ1qlU0qVM9slkzqVQzqVM0q1YzqFM0qlROsWIzqVQzqFQ0qVMzqFQzqVQzqVQ1qVQzqVNVqlUzqFP///+HzJqx3b08rFpPtGpuwYXp9eyPz6DR69g9FZdJAAAAFXRSTlMAG21FtA+a7jHiVwaAxPnKd9J0/gMIIjVXAAAA8ElEQVQoz33TyRKDIBAE0AZUlrgl2MQlyf9/Zg4aSxDTJ6hXTIEzAr8IWdx1a5tK3ZBmMA1J0pOkrstYy4JRrDuqsUxTH1SvRaNUe+XzWZJeblxkzpJszVqa+fgCANBk6DmSpAIgchrCvN1OZvVJknoAikslDXC/VjpAXysl0JLkMs4ZZQ9YkhxDeJ/Vu+3Z0xjCZ06UVEC1rpZXCO9EvQDUtp7GEGJlB+C2X315xcoeAOp9O82R6vK63SQfa79dXrvfwNa5bluxD1t1ZmsOoyrbtLKIBt1EfdWP04+iqu0D+K4vkclgnOydEviTId5+AY/mM/wSjlEoAAAAAElFTkSuQmCC) no-repeat 0 1px;
}
.form-group .error .tip{
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAMAAAAM7l6QAAAAY1BMVEVHcEz/Z2f/XFz/W1v/XFz/XV3/XFz/W1v/cnL/X1//XFz/W1v/W1v/XFz/XV3/XFz/W1v/XFz/XV3/XFz/W1v/////fHz/2dn/7e3/s7P/xsb/8vL/mpr/iYn/bGz/p6f/Y2NsZYSPAAAAFHRSTlMAELjPcS3F8ggd6t2XgTmsVEhCoJXLaSEAAADrSURBVCjPjZPrFoIgEIQXARHSSl1v2e39nzI0pV3MTvPL4TvALjsCrLIqkc4c0rzMYKNjikGmEByKEzK5M6X6gLEKQs2y1vV9f1u+83By2Ns1TbNiVAv+3Msw6nfNyHAbXDJj0tGFYaym18BdXHusInwn3Xuc7GO0AJLYweMn8b529wMfAUyEkWP63I8Ia9b2hEfkpeXEXiPsx14Se29b8iqY+r4zg3uaZ1Z8fDsMD5KpOVLC0bv7aDPA+TuWa2DD8ddx7MI8bAhbvi3LaRJVFVNpWdA1nSsatflRqnrtIFUCvkkIa63I4H+9AItiMgck4VeDAAAAAElFTkSuQmCC) no-repeat 0 1px;
}
</style>
