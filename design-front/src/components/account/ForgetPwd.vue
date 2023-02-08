<template>
  <div>
    <div class="description">
      Forget Password
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
            <span @click="$router.push({path: '/signIn'})">登录</span>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: 'ForgetPwd'
}
</script>

<style scoped lang="less">

</style>
