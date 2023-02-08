<template>
  <div>
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
              <input type="text" id="email" name="email" placeholder="注册邮箱" />
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
            <div class="form-group">
              <input class="password" type="password" id="pwd" name="password" placeholder="请输入密码" data-tip="pwdMsg" data-pwd="repeatPwd" disabled="disabled"/>
              <div class="index-item" id="pwdMsg">
                <div class="tip fd"></div>
              </div>
            </div>
            <div class="form-group">
              <input class="password" type="password" name="repeatPwd" id="repeatPwd" placeholder="重复密码" data-tip="repeatPwdMsg" data-pwd="pwd" disabel="disabled"/>
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
      validEmail: true
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
    }
  },
  mounted () {
    let waitProcess = 0
    const _this = this
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
  }
}
</script>

<style scoped lang="less">

</style>
