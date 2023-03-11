<template>
  <div class="container-fluid" id="addProject">
    <div class="row">
      <div class="col-lg-12">
        <div class="card">
          <div class="card-body">
            <form action="#" method="post" class="row">
              <div class="form-group col-md-12">
                <label>项目名称</label>
                <input type="text" class="form-control " name="projectName" placeholder="请输入项目标题" v-model="project.projectName" />
              </div>
              <div class="form-group col-md-12">
                <label>项目级别</label>
                <div class="form-controls">
                  <select name="projectLevel" class="form-control" v-model="project.projectLevel">
                    <option value="image">图片</option>
                    <option value="zip">压缩包</option>
                    <option value="project">混合项目</option>
                  </select>
                </div>
              </div>
              <div class="form-group col-md-12">
                <label>检测模式</label>
                <div class="form-controls">
                  <select name="projectLevel" class="form-control" v-model="project.mode">
                    <option value="accuracy">Accuracy</option>
                    <option value="speed">Speed</option>
                  </select>
                </div>
              </div>
              <div class="form-group col-md-12">
                <label>文件上传</label>
                <div class="form-controls">
                  <UploadFile style="text-align: center" :mode="project.mode" :allow-start="uploaded" ref="uploadFile" :merge-file="mergeFile"></UploadFile>
                </div>
              </div>
              <div class="form-group col-md-12" id="createSubmitBtn">
                <button type="submit" class="btn btn-primary ajax-post" target-form="add-form" @click="createProject">确 定</button>
                <button type="button" class="btn btn-default" @click="$router.back() ">返 回</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UploadFile from '@/components/detector/UploadFile.vue'
import $ from 'jquery'
export default {
  name: 'AddProject',
  data () {
    return {
      user: JSON.parse(this.$store.state.data),
      project: {
        mode: 'accuracy',
        projectLevel: 'project',
        projectName: '',
        userId: this.user.userId
      },
      uploaded: false
    }
  },
  components: {
    UploadFile
  },
  methods: {
    createProject: function (event) {
      event.preventDefault()
      const _this = this
      if (_this.project.projectName.length < 3 || _this.project.projectName.length > 100) {
        _this.$message.warning('项目标题应该3~100个字符')
      } else if (this.$refs.uploadFile.checkFile() === 0) {
        _this.$message.warning('未选中任何文件')
      } else {
        $.ajax({
          url: window.server.Project.detectProject.insert,
          method: 'post',
          dataType: 'json',
          data: _this.project,
          success: function (resp) {
            if (resp.result) {
              // 1. 修改上传按钮
              _this.uploaded = true
              // 2. 修改添加页面
              $('#createSubmitBtn').hide()
              $('#addProject input').attr('disabled', 'disabled')
              $('#addProject select').attr('disabled', 'disabled')
              _this.project = resp.data
              _this.$message.info('文件上传中, 请勿离开页面, 上传文件无法取消或修改.')
            } else {
              _this.$message.warning(resp.msg)
            }
          }
        })
      }
    },
    mergeFile: function (data) {
      data.userId = JSON.parse(this.$store.state.data).userId
      data.mode = this.mode
      data.detectId = this.project.detectId
      return this.axios({
        url: window.server.COMMONS.bigFileUpload.projectFileMerge,
        method: 'post',
        data: data
      })
    }
  }
}
</script>

<style scoped>

</style>
