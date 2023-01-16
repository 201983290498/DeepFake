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
        <uploader-btn id="global-uploader-btn" ref="uploadBtn">选择文件<i class="el-icon-upload el-icon--right"></i></uploader-btn>
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
        target: window.server.COMMONS.bigFileUpload + '/chunk', // todo 上传的url, 需要修改
        chunkSize: '20971520', // 分块大小
        testChunks: false,
        fileParameterName: 'upfile',
        singleFile: true, // 一次只允许上传一个文件
        // eslint-disable-next-line camelcase
        checkChunkUploadedByResponse: function (chunk, response_msg) {
          const objMessage = JSON.parse(response_msg)
          if (objMessage.skipUpload) {
            return true
          }
          return (objMessage.uploadedChunks || []).indexOf(chunk.offset + 1) >= 0
        }
      },
      statusText: {
        success: '上传成功！',
        error: '出错了！',
        uploading: '上传中...',
        paused: '暂停中...',
        waiting: '等待中...'
      },
      isUploadOk: true, // 是否还有没有上传完成的文件
      panelShow: true,
      show_filename: true
    }
  },
  methods: {
    computeMD5: function (file) {
      file.pause()
      const fileReader = new FileReader()
      const blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice
      var currentChunk = 0
      const chunkSize = 20 * 1024 * 1024
      const spark = new SparkMD5.ArrayBuffer()
      loadNext(file)
      fileReader.onload = (e) => {
        spark.append(e.target.result)
        if (currentChunk < 1) { // 文件太大会怎样呢
          loadNext(file)
        } else {
          file.uniqueIdentifier = spark.end() // 设置文件的识别码
          file.resume() // 开始上传
        }
      }
      function loadNext (file) {
        const start = currentChunk * chunkSize
        const end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end))
        currentChunk += 1
        console.log('计算第' + currentChunk + '块')
      }
    },
    onFileAdded: function (file) {
      console.log('onFileAdded')
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
      const _this = this
      console.log('onFileSuccess')
      _this.mergeFile(file).then(response => {
        if (response.data.result) {
          console.log('合并成功')
        } else {
          _this.$message({
            message: '合并异常' + response.data.msg,
            type: 'error'
          })
        }
      })
    },
    onFileError: function (rootFile, file, response, chunk) {
      console.log(file)
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
    },
    mergeFile: function (data) {
      console.log('mergeFile')
      return this.axios({
        url: window.server.COMMONS.bigFileUpload + '/mergeFile',
        method: 'post',
        data: data
      })
    }
  }
}
</script>

<style scoped>

</style>
