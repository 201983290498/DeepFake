<template>
  <!-- 上传器 -->
  <uploader
    ref="uploader"
    :options="options"
    :autoStart=false
    :file-status-text="statusText"
    @file-added="onFileAdded"
    @file-success="onFileSuccess"
    @file-progress="fileProgress"
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
    return {
      options: {
        target: 'localhost:8081/uploader', // todo 上传的url, 需要修改
        chunkSize: '20971520', // 分块大小
        testChunks: false,
        singleFile: true // 一次只允许上传一个文件
      },
      statusText: {
        success: '上传成功！',
        error: '出错了！',
        uploading: '上传中...',
        paused: '等待中...',
        waiting: '等待中...'
      },
      isUploadOk: true, // 是否还有没有上传完成的文件
      panelShow: true,
      show_filename: true
    }
  },
  methods: {
    computeMD5: function (file) {
      const fileReader = new FileReader()
      const blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice
      let currentChunk = 0
      const chunkSize = 20 * 1024 * 1024
      const chunks = Math.ceil(file.size / chunkSize)
      const spark = new SparkMD5.ArrayBuffer()
      file.paused()
      loadNext()
      fileReader.onload = (e => {
        spark.append(e.target.result)
        if (currentChunk < chunks) { // 文件太大会怎样呢
          currentChunk += 1
          loadNext()
          this.$nextTick(() => {}) // todo 有什么用呢?
        } else {
          file.uniqueIdentifier = spark.end() // 设置文件的识别码
          file.resume() // 开始上传
        }
      })
      function loadNext () {
        const start = currentChunk * chunkSize
        const end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end))
        currentChunk++
      }
    },
    onFileAdded: function (file) {
      const fileSizeLimit = 20 * 1024 * 1024 * 1024
      if (file.size > fileSizeLimit) {
        this.$message({
          showClose: true,
          message: '文件大小不能超过20G'
        })
        file.cancel() // 停止上传
        return false
      }
      this.panelShow = true // 展示上传panel
      this.show_filename = false // 不显示文件名
      this.computeMD5(file) // md验证上传
    },
    onFileSuccess: function (rootFile, file, response, chunk) {
      mergeFile
    },
    onFileError: function (rootFile, file, response, chunk) {
      this.$message({
        message: '上传失败, 请重新上传',
        type: 'error'
      })
    },
    fileProgress: function (rootFile, file, chunk) {
      if (file.isUploading()) {
        this.isUploadOk = false // 禁止上传
      } else {
        this.isUploadOk = true
      }
    }
  }
}
</script>

<style scoped>

</style>
