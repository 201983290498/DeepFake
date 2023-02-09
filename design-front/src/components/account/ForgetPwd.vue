<template>
  <div id="forgetPwd">
    <div class="description">
      Forget Password
    </div>
    <form id="form" action="/" enctype="multipart/form-data" ref="reluForm">
      <div class="login-container">
        <div class="left-container">
          <div class="title" style="text-align:left;">
            <span>找回密码</span>
          </div>
          <div class="input-container">
            <div class="form-group">
              <input type="text" id="email" name="email" placeholder="请先输入注册邮箱" />
              <div class="index-item" id="emailMsg">
                <div>
                  <div class="tip fd"></div>
                </div>
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
            <div class="form-group" >
              <input class="password" type="password" id="pwd" name="password" placeholder="请输入密码" data-tip="pwdMsg" data-pwd="repeatPwd" disabled="disabled"/>
              <div class="index-item" id="pwdMsg">
                <div class="tip fd"></div>
              </div>
            </div>
            <div class="form-group">
              <input class="password" type="password" name="repeatPwd" id="repeatPwd" placeholder="重复密码" data-tip="repeatPwdMsg" data-pwd="pwd" disabled="disabled"/>
              <div class="index-item" id="repeatPwdMsg">
                <div class="tip fd"></div>
              </div>
            </div>
          </div>
          <div class="message-container">
            <span @click="$router.push({path: '/signIn'})">返回登入</span>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import $ from 'jquery'

export default {
  name: 'ForgetPwd',
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
    checkEmail: function (value) {
      return /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)
    },
    validCheck: function () {
      console.log(1)
      if (!this.validEmail) {
        this.$message.info('请先输入邮箱')
      }
    }
  },
  mounted () {
    let waitProcess = 0
    const _this = this
    $('input[type=button]').addClass('avoid')
    $('.password').focus(function () {
      const msg = '6～16个字符，首字母必须大写,并包含数字和字母'
      const tip = this.dataset.tip
      $(`#${tip} .tip`).empty().text(msg)// 将内容清空
      $(`#${tip}`).removeClass('success error')
      $(`#${tip}`).addClass('focus')
    })
    $('.password').blur(function () {
      const tip = this.dataset.tip // 显示标签
      const pwd = this.dataset.pwd // 另一个框的id
      const tip2 = $(`#${pwd}`).data('tip')
      $(`#${tip}`).removeClass('focus success error')
      $(`#${tip2}`).removeClass('focus success error')
      const val1 = this.value
      const val2 = $(`#${pwd}`).val() // 另外一个输入框的密码
      if (val1 !== '') { // 输入的内容需要验证
        let msg = ''
        let style = ''
        if (_this.checkPwd(val1) && val1 === val2) {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;恭喜，该密码可用!'
          style = 'success'
        } else if (_this.checkPwd(val1)) {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;两次输入的密码不同!'
          style = 'error'
        } else {
          msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;首写字母必须大写，并包含数字和字母!'
          style = 'error'
        }
        $(`#${tip}`).addClass(style)
        $(`#${tip} .tip`).empty().html(msg)
        $(`#${tip2}`).addClass(style)
        $(`#${tip2} .tip`).empty().html(msg)
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
        let style = ''
        let msg = ''
        $.ajax({
          url: _this.userUrl + '/register/checkMsg',
          type: 'post',
          dataType: 'json',
          data: {
            email,
            message
          },
          success: function (rq) {
            style = rq.result ? 'success' : 'error'
            msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + (rq.result ? '验证码通过!' : '验证码错误!')
            $('#validateMsg').addClass(style)
            $('#validateMsg .tip').empty().html(msg)
          }
        })
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
      let style = ''
      let msg = ''
      if (value && _this.checkEmail(value)) {
        msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该邮箱格式有效!'
        style = 'success'
      } else if (value !== '') {
        msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;邮箱格式错误!'
        style = 'error'
      }
      if (style === 'success') {
        $.ajax({
          url: _this.userUrl + '/account/emailExist?email=' + value,
          type: 'get',
          dataType: 'json',
          success: function (resp) {
            style = resp.result ? 'success' : 'error'
            if (!resp.result) {
              // 需要已经注册好了
              msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该邮箱没有注册任何账号'
            } else {
              // todo 需要二次处理
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
    $('input[type=button]').click(function () {
      if (!$('input[type=button]').hasClass('avoid')) {
        $.ajax({
          url: _this.userUrl + '/register/genMsg',
          type: 'post',
          dataType: 'json',
          data: {
            email: $('#email').val()
          },
          success: function (resp) {
            if (resp.result) {
              _this.$message.info('验证信息已经发送到邮箱!')
            }
          }
        })
        $('input[type=button]').addClass('avoid')
        $('input[type=button]').val('重新发送(60s)')
        waitProcess = setInterval(() => {
          const content = $('input[type=button]').val()
          let time = parseInt(content.substr(5, 2))
          time = time - 1
          if (time < 10) {
            $('input[type=button]').val('重新发送(0' + time + 's)')
          } else {
            $('input[type=button]').val('重新发送(' + time + 's)')
          }
        }, 1000)// 每隔一段时间修改时间
        setTimeout(() => {
          clearInterval(waitProcess)
          $('input[type=button]').removeClass('avoid')
          $('input[type=button]').val('发送验证信息')
        }, 60000)
      }
    })
  }
}
</script>

<style scoped lang="less">
#forgetPwd {
  margin-bottom: 50px;
}
.description {
  font-weight: bold;
  font-size: 30px;
  color: #343A40;
}
#forgetPwd .login-container{
  border-radius: 15px;
  background-color: #fff;
  margin: 10px auto 0;
  display: inline-block;
}
#forgetPwd .left-container{
  display: inline-block;
  width: 480px;
  border-top-left-radius: 15px;
  border-bottom-left-radius: 15px;
  padding: 60px 60px 40px 60px;
}
#forgetPwd .title span{
  font-size: 20px;
  color: #343A40;
  font-family: Inter,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,Noto Sans,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol,Noto Color Emoji;
  font-weight: 700 !important;
  border-bottom: 3px solid rgba(52,58,64,0.6);
  cursor: pointer;
  text-align: center;
}
#forgetPwd .left-container .input-container{
  padding: 30px 0 20px;
}
#forgetPwd input{
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
#forgetPwd input:focus{
  border: 1px solid #C0C4CC;
  background-color: #E5E5E5;
  color: #000;
}
#forgetPwd .left-container .message-container{
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
  text-align: center;
}
#forgetPwd .left-container .message-container:hover{
  color: #2C96C4;
  transition: .2s;
}
// 设置提示语的特点
#forgetPwd .index-item{
  position: absolute;
  top: 39px;
  left: 10px;
  height: 17px;
  line-height: 17px;
  width: 100%;
  font-size: 13px;
  text-align: left;
}
#forgetPwd .form-group .index-item.focus .tip {
  display: block;
  color: #9e9e9e;
}
#forgetPwd .form-group .index-item.success .tip{
  display: block;
  color: #33a853;
  background-size: 15px 15px;
}
#forgetPwd .form-group .index-item.error .tip{
  display: block;
  color: #ff5b5b;
  background-size: 15px 15px;
}
#forgetPwd .form-group .index-item .fd{
  display: none;
}
.form-group .success .tip{
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAMAAAAM7l6QAAAAXVBMVEVHcEw3rVc0qVQ1qlU0qVM9slkzqVQzqVM0q1YzqFM0qlROsWIzqVQzqFQ0qVMzqFQzqVQzqVQ1qVQzqVNVqlUzqFP///+HzJqx3b08rFpPtGpuwYXp9eyPz6DR69g9FZdJAAAAFXRSTlMAG21FtA+a7jHiVwaAxPnKd9J0/gMIIjVXAAAA8ElEQVQoz33TyRKDIBAE0AZUlrgl2MQlyf9/Zg4aSxDTJ6hXTIEzAr8IWdx1a5tK3ZBmMA1J0pOkrstYy4JRrDuqsUxTH1SvRaNUe+XzWZJeblxkzpJszVqa+fgCANBk6DmSpAIgchrCvN1OZvVJknoAikslDXC/VjpAXysl0JLkMs4ZZQ9YkhxDeJ/Vu+3Z0xjCZ06UVEC1rpZXCO9EvQDUtp7GEGJlB+C2X315xcoeAOp9O82R6vK63SQfa79dXrvfwNa5bluxD1t1ZmsOoyrbtLKIBt1EfdWP04+iqu0D+K4vkclgnOydEviTId5+AY/mM/wSjlEoAAAAAElFTkSuQmCC) no-repeat 0 1px;
}
.form-group .error .tip{
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAMAAAAM7l6QAAAAY1BMVEVHcEz/Z2f/XFz/W1v/XFz/XV3/XFz/W1v/cnL/X1//XFz/W1v/W1v/XFz/XV3/XFz/W1v/XFz/XV3/XFz/W1v/////fHz/2dn/7e3/s7P/xsb/8vL/mpr/iYn/bGz/p6f/Y2NsZYSPAAAAFHRSTlMAELjPcS3F8ggd6t2XgTmsVEhCoJXLaSEAAADrSURBVCjPjZPrFoIgEIQXARHSSl1v2e39nzI0pV3MTvPL4TvALjsCrLIqkc4c0rzMYKNjikGmEByKEzK5M6X6gLEKQs2y1vV9f1u+83By2Ns1TbNiVAv+3Msw6nfNyHAbXDJj0tGFYaym18BdXHusInwn3Xuc7GO0AJLYweMn8b529wMfAUyEkWP63I8Ia9b2hEfkpeXEXiPsx14Se29b8iqY+r4zg3uaZ1Z8fDsMD5KpOVLC0bv7aDPA+TuWa2DD8ddx7MI8bAhbvi3LaRJVFVNpWdA1nSsatflRqnrtIFUCvkkIa63I4H+9AItiMgck4VeDAAAAAElFTkSuQmCC) no-repeat 0 1px;
}
#pwdMsg, #emailMsg, #validateMsg{
  top: 23px !important;
}
#forgetPwd .left-container .form-group input[type=button]  {
  cursor: pointer;
}
#forgetPwd .form-group{
  position: relative;
}
</style>
