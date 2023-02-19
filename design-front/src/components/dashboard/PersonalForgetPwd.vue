<template>
  <div class="container-fluid" id="forgetCard">
    <div class="row">
      <div class="col-lg-12">
        <div class="card">
          <div class="card-header"><h4>{{pageTitle}}</h4></div>
          <div class="card-body">
            <form method="post" class="guide-box" data-navigateable="true" ref="forgetForm">
              <ul class="nav-step step-dots">
                <li class="nav-step-item active">
                  <span>步骤一</span>
                  <a class="active" data-toggle="tab" href="#step-1"></a>
                </li>
                <li class="nav-step-item">
                  <span>步骤二</span>
                  <a data-toggle="tab" href="#step-2"></a>
                </li>

                <li class="nav-step-item">
                  <span>步骤三</span>
                  <a data-toggle="tab" href="#step-3"></a>
                </li>

                <li class="nav-step-item">
                  <span>步骤四</span>
                  <a data-toggle="tab" href="#step-4"></a>
                </li>
              </ul>
              <!--对应内容-->
              <div class="nav-step-content">
                <div class="nav-step-pane hidden active" id="step-1">
                  <div class="form-group">
                    <label>邮箱地址</label>
                    <input class="form-control" type="text" name="email" id="email">
                  </div>
                </div>

                <div class="nav-step-pane hidden" id="step-2">
                  <div class="form-group">
                    <label>验证码</label>
                    <input class="form-control" type="text" name="validateData" id="validateMsg">
                  </div>
                </div>

                <div class="nav-step-pane hidden" id="step-3">
                  <div class="form-group">
                    <label>设置密码</label>
                    <input class="form-control" type="password" name="password" id="password">
                  </div>
                </div>

                <div class="nav-step-pane hidden" id="step-4">
                  <div class="form-group">
                    <label>重复密码</label>
                    <input class="form-control" name="repeatPwd" id="repeatPwd" type="password">
                  </div>
                </div>
              </div>
              <!--End 对应内容-->
              <hr>
              <div class="nav-step-button">
                <button class="btn btn-secondary disabled" id="prev" type="button" @click="activeStep = Math.max(activeStep - 1, 1)">上一步</button>
                <button class="btn btn-secondary" id="next" type="button" @click="activeStep = activeStep + 1" v-show="activeStep !== 4">下一步</button>
                <button class="btn btn-primary" id="finish" type="button" v-show="activeStep === 4" @click="submitInfo">完成</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'PersonalForgetPwd',
  data () {
    return {
      pageTitle: '密码修改',
      activeStep: 1,
      userUrl: window.server.COMMONS.userUrl
    }
  },
  watch: {
    activeStep: function (newValue, oldValue) {
      const _this = this
      $('.nav-step-item').removeClass('complete')
      $('.nav-step-item').removeClass('active')
      for (let i = 0; i < newValue - 1; i++) {
        $($('.nav-step-item')[i]).addClass('complete')
      }
      $($('.nav-step-item')[newValue - 1]).addClass('active')
      $($('.nav-step-pane')[oldValue - 1]).removeClass('active')
      $($('.nav-step-pane')[newValue - 1]).addClass('active')
      if (newValue === 1) {
        $('#prev').addClass('disabled')
      } else {
        $('#prev').removeClass('disabled')
      }
      if (newValue === 2 && oldValue === 1) {
        if (!this.checkEmail($('#email').val())) {
          this.$message.warning('输入的邮箱格式错误')
          return
        }
        $.ajax({
          url: _this.userUrl + '/register/genMsg',
          type: 'post',
          dataType: 'json',
          data: {
            email: $('#email').val(),
            type: 'forgetPwd'
          },
          success: function (resp) {
            if (resp.result) {
              _this.$message.success('验证信息已经发送到邮箱!')
            } else {
              _this.$message.warning(resp.data)
            }
          },
          error: () => {
            _this.$message.warning('验证码发送失败, 请稍后尝试。')
          }
        })
      } else if (newValue === 2) {
        this.$message.info('验证码只有从step1进入step2时会自动发送。')
      }
    }
  },
  methods: {
    checkEmail: function (value) {
      return /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)
    },
    submitInfo: function (event) {
      const _this = this
      event.preventDefault()
      let flag = 0
      flag = flag + ($('#password').val() !== $('#repeatPwd').val())
      flag = flag + ($('#email').val() === '') + ($('#validateMsg').val() === '') + ($('#repeatPwd').val() === '')
      if (flag === 0) {
        $.ajax({
          url: _this.userUrl + '/forgetPwd',
          type: 'post',
          data: new FormData(_this.$refs.forgetForm),
          processData: false,
          contentType: false,
          success: function (resp) {
            resp = JSON.parse(resp)
            if (resp.result) {
              _this.$message.success('密码修改成功!')
              _this.$router.push({ path: '/showBoard/personalInfo' })
            } else {
              _this.$message.warning(resp.data)
            }
          },
          error: function () {
            _this.$message.warning('修改密码失败, 请稍后尝试.')
          }
        })
      } else {
        _this.$message.warning('输入的信息有误, 请检查后重新提交.')
      }
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
  },
  mounted () {
    const _this = this
    $('.nav-step-item').each(function (index, item) {
      $(item).click(() => {
        _this.activeStep = index + 1
      })
    })
  }
}
</script>

<style scoped>

</style>
