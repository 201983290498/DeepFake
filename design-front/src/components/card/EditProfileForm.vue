<template>
  <BaseCard class="card" title="User Profile">
    <div>
      <form @submit.prevent="updateProfile" ref="updateForm" id="editForm">
        <div class="row">
          <div class="col-md-5">
            <FgInput type="text" label="用户唯一标识" :disabled="true" placeholder="Use Unique Id" v-model="user.userId" name="userId"></FgInput>
          </div>
          <div class="col-md-3">
            <FgInput type="text" label="用户名" placeholder="Username" v-model="user.username" name="username"></FgInput>
          </div>
          <div class="col-md-4">
            <FgInput type="email" label="邮箱" placeholder="Email" v-model="user.email" name="email"></FgInput>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <FgInput type="text" label="姓氏" placeholder="First Name" v-model="user.firstName" name="firstName"></FgInput>
          </div>
          <div class="col-md-6">
            <FgInput type="text" label="名字" placeholder="Last Name" v-model="user.lastName" name="lastName"></FgInput>
          </div>
        </div>
        <div class="row">
          <div class="col-md-10 col-sm-10">
            <FgInput type="text" label="绑定手机" :disabled="true" placeholder="Phone number" v-model="user.phoneNumber" name="phoneNumber"></FgInput>
          </div>
          <div class="col-md-2 col-sm-2">
            <PButton  style="margin-top: 26px;width: 100%">
              更改绑定
            </PButton>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <FgInput type="text" label="公司" placeholder="Company" v-model="user.company" name="company"></FgInput>
          </div>
        </div>
        <div class="row">
          <div class="col-md-4">
            <FgInput type="text" label="所在城市" placeholder="City" v-model="user.city" name="city"></FgInput>
          </div>
          <div class="col-md-4">
            <FgInput type="text" label="所在国家" placeholder="Country" v-model="user.country" name="country"></FgInput>
          </div>
          <div class="col-md-4">
            <FgInput type="number" label="邮编" placeholder="ZIP Code" v-model="user.zipCode" name="zipCode"></FgInput>
          </div>
        </div>
        <div class="row">
          <div class="col-md-12">
            <div class="form-group">
              <label>用户简介</label>
              <textarea rows="5" class="form-control border-input" placeholder="Here can be your description or your company" v-model="user.userIntroduction" name="userIntroduction"></textarea>
            </div>
          </div>
        </div>
        <div class="text-center">
          <div class="col-md-12 col-sm-12" id="button">
            <PButton v-show="enableEdit" type="info" round @click.native.prevent="editProfile">
              编辑信息
            </PButton>
            <PButton type="info" v-show="!enableEdit" round @click.native.prevent="cancelEdit">
              取消编辑
            </PButton>
          </div>
          <div class="col-md-6 col-sm-6" v-show="!enableEdit">
            <PButton type="success" round @click.native.prevent="updateProfile">
              更新信息
            </PButton>
          </div>
        </div>
        <div class="clearfix"></div>
      </form>
    </div>
  </BaseCard>
</template>
<script>
import BaseCard from './Card.vue'
import PButton from '../Button.vue'
import FgInput from '../Inputs/formGroupInput.vue'
import $ from 'jquery'
export default {
  name: 'EditProfileForm',
  data () {
    return {
      user: JSON.parse(this.$store.state.data),
      enableEdit: true
    }
  },
  components: {
    BaseCard,
    PButton,
    FgInput
  },
  methods: {
    updateProfile: function () {
      const _this = this
      $('#editForm input[name=userId]').attr('disabled', false)
      $('#editForm input[name=phoneNumber]').attr('disabled', false)
      this.axios({
        url: window.server.COMMONS.userUrl + '/updateUser',
        method: 'post',
        data: new FormData(_this.$refs.updateForm)
      }).then(resp => {
        console.log(resp)
        if (resp.data.result) {
          _this.$message.success('个人信息更新成功')
          _this.$store.commit('saveData', _this.user)
        } else {
          _this.$message.warning('个人信息更新失败, 请稍后重试。')
          _this.user = JSON.parse(_this.$store.state.data)
        }
      }).catch(() => {
        _this.$message.warning('请求异常, 请稍后重试。')
        _this.user = JSON.parse(_this.$store.state.data)
        console.log(_this.user)
      }).finally(() => {
        this.cancelEdit()
      })
    },
    editProfile: function () {
      $('#button').removeClass('col-sm-12')
      $('#button').removeClass('col-md-12')
      $('#button').addClass('col-sm-6')
      $('#button').addClass('col-sm-6')
      $('#editForm textarea').attr('disabled', false)
      $('#editForm input').attr('disabled', false)
      $('#editForm input[name=userId]').attr('disabled', true)
      $('#editForm input[name=phoneNumber]').attr('disabled', true)
      this.enableEdit = false
    },
    cancelEdit: function () {
      this.enableEdit = true
      $('#editForm input').attr('disabled', true)
      $('#editForm textarea').attr('disabled', true)
      $('#button').addClass('col-sm-12')
      $('#button').addClass('col-md-12')
      $('#button').removeClass('col-sm-6')
      $('#button').removeClass('col-sm-6')
    }
  },
  mounted () {
    this.cancelEdit()
  }
}
</script>
<style scoped>

</style>
