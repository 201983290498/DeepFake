<template>
  <div id="projectDetail">
    <div class="row">
      <div class="col-lg-12">
        <div class="card">
          <div class="card-body">
            <form action="#" method="post" class="row">
              <div class="form-group col-md-12">
                <label>项目名称 <a class="btn btn-xs btn-default" href="#" title="编辑" data-toggle="tooltip" @click="editProject"><i class="mdi mdi-pencil"></i></a></label>
                <input type="text" class="form-control " name="projectName" disabled="disabled" v-model="project.projectName" />
              </div>
              <div class="form-group col-md-12">
                <label>项目级别</label>
                <input type="text" class="form-control " name="projectLevel" disabled="disabled" v-model="project.projectLevel" />
              </div>
              <div class="form-group col-md-12">
                <label>检测模式</label>
                <input type="text" class="form-control " name="mode" disabled="disabled" v-model="project.mode" />
              </div>
              <div class="form-group col-md-12">
                <label>创建时间</label>
                <input type="text" class="form-control " name="createTime" disabled="disabled" :value="new Date(project.createTime).toLocaleString()" />
              </div>
              <div class="form-group col-md-12">
<!--                有代替换成正在执行的进度 -->
                <label>完成时间</label>
                <input type="text" class="form-control " name="createTime" disabled="disabled" :value="project.finishTime === null? '正在检测中' : new Date(project.finishTime).toLocaleString()" />
              </div>
              <div class="form-group col-md-12">
                <label>检测结果样本</label>
                <div class="form-group">
                  <button type="button" class="btn btn-primary" @click="downloadFile">点击下载</button>
                </div>
              </div>
              <div class="form-group col-md-12">
                <label>文件详情</label>
                <PicBoard :show-files="files" :detail="true"></PicBoard>
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
import common from '@/assets/js/common'
import PicBoard from '@/components/dashboard/common/PicBoard.vue'
export default {
  name: 'ProjectDetail',
  data () {
    return {
      project: this.$route.params.detectProject,
      user: JSON.parse(this.$store.state.data),
      pageTitle: '项目详情',
      createTimeLocal: '',
      files: []
    }
  },
  components: {
    PicBoard
  },
  methods: {
    editProject: function () {
      const _this = this
      const node = $('#projectDetail input[name=projectName]')
      node.removeAttr('disabled')
      const value = node.val()
      node.val('').focus().val(value)
      node.blur((event) => {
        if (event.target.value === value || event.target.value === '') {
          node.val(value)
        } else {
          $.ajax({
            url: window.server.Project.detectProject.update,
            method: 'post',
            dataType: 'json',
            data: { userId: _this.project.userId, detectId: _this.project.detectId, projectName: event.target.value },
            success: function (resp) {
              if (!resp.result) {
                _this.$message.warning('服务器异常')
              }
            }
          })
        }
        node.attr('disabled', 'disabled')
      })
    },
    downloadFile: function () {
      const _this = this
      common.getProjectResult(this.project.userId, this.project.detectId).then((resp) => {
        if (resp.data.result) {
          const url = resp.data.data
          common.downLoadPic({ name: _this.project.projectName.replaceAll(' ', '_') + '.txt', base64: url })
        }
      })
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', this.pageTitle)
    common.getAllRecords(this.user.userId, this.project.detectId, 'image').then((resp) => {
      if (resp.data.result) {
        _this.files = resp.data.data
      }
    })
  }
}
</script>

<style scoped>

</style>
