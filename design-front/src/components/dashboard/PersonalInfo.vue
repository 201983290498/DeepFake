<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-4 col-md-5 col-sm-6">
        <user-card></user-card>
      </div>
      <div class="col-lg-8 col-md-7 col-sm-6">
        <edit-profile-form> </edit-profile-form>
      </div>
    </div>
  </div>

</template>

<script>
import EditProfileForm from '@/components/card/EditProfileForm.vue'
import UserCard from '@/components/card/UserCard.vue'
export default {
  name: 'PersonalInfo',
  data () {
    return {
      pageTitle: '密码修改'
    }
  },
  components: {
    EditProfileForm,
    UserCard
  },
  mounted () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
  }
}
</script>

<style scoped>

</style>
