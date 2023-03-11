<template>
  <div class="container-fluid addProject">
    <div class="row">
      <div class="col-lg-12">
        <div class="card">
          <div class="card-body">
            <form action="#" method="post" class="row">
              <div class="form-group col-md-12">
                <label>项目名称</label>
                <input type="text" class="form-control" name="projectName" placeholder="请输入项目标题" v-model="project.projectName"/>
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
                  <UploadFile style="text-align: center" :mode="project.mode" :allow-start="uploaded"></UploadFile>
                </div>
              </div>
              <div class="form-group col-md-12">
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
export default {
  name: 'AddProject',
  data () {
    return {
      project: {
        mode: 'accuracy',
        projectLevel: 'project',
        projectName: '',
        uploaded: false
      }
    }
  },
  components: {
    UploadFile
  },
  methods: {
    createProject: function (event) {
      event.preventDefault()
    }
  }
}
</script>

<style scoped>

</style>
