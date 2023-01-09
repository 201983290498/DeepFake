<template>
  <!-- 上传器 -->
  <uploader
    ref="uploader"
    :options="options"
    :autoStart=false
    :file-status-text="fileStatusText"
    @file-added="onFileAdded"
    @file-success="onFileSuccess"
    @file-progress="onFileProgress"
    @file-error="onFileError"
    class="uploader-ui">
    <uploader-unsupport></uploader-unsupport>
    <uploader-drop>
      <div>
        <uploader-btn id="global-uploader-btn" :attrs="attrs" ref="uploadBtn">选择文件<i class="el-icon-upload el-icon--right"></i></uploader-btn>
      </div>
    </uploader-drop>
    <uploader-list></uploader-list>
  </uploader>
</template>

<script>
import SparkMD5 from 'spark-md5'
export default {
  name: 'UploadFile',
  data () {

  }
}
</script>

<style scoped>

</style>
