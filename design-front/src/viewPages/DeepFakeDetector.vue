<template>
  <div class="df-detector">
    <ServiceTitle :titleHeader="title"></ServiceTitle>
    <ServiceModule @changeMode="changeMode"></ServiceModule>
    <ServiceDisplay :disImgs="disImgs" @uploadImage="uploadImage" ref="serviceDisplay"></ServiceDisplay>
    <ServiceUpload @uploadImage="uploadImage" @changeDisBoard="changeDisBoard" @uploadZip="uploadZip" ref="serviceUpload" :uploaded="uploaded" :downToZero="downToZero" :loginStatus="loginStatus"></ServiceUpload>
    <UploadFile style="margin-top: 35px" v-show="loginStatus" :mode="mode"></UploadFile>
  </div>
</template>
<!--components为啥要用大括号呢-->

<script>
import ServiceDisplay from '../components/detector/ServiceDisplay.vue'
import ServiceModule from '../components/detector/ServiceModule.vue'
import ServiceUpload from '../components/detector/ServiceUpload.vue'
import ServiceTitle from '../components/detector/ServiceTitle.vue'
import UploadFile from '../components/detector/UploadFile.vue'
import $ from 'jquery'
import SparkMD5 from 'spark-md5'
export default {
  name: 'DeepFakeDetector',
  components: {
    ServiceDisplay,
    ServiceModule,
    ServiceTitle,
    ServiceUpload,
    UploadFile
  },
  data () {
    return {
      title: 'DeepFake篡改检测',
      deepfakeDetector: window.server.DEEPFAKE,
      disImgs: [require('../../static/imgs/fake1.jpg'), require('../../static/imgs/fake2.jpg'), require('../../static/imgs/real1.jpg')],
      uploaded: 0,
      loginStatus: JSON.parse(this.$store.state.status),
      mode: 'accuracy'
    }
  },
  watch: {
    uploaded: function (newValue, oldValue) {
      if (newValue === 100) {
        setTimeout(() => {
          this.uploaded = 0
        }, 4000)
      }
    }
  },
  methods: {
    uploadImage: function (imgs) { // 上传文件返回结果
      const _this = this
      _this.$refs.serviceDisplay.disBoardMask(true) // 给主显示框加上扫描效果
      const image = new Image()
      image.src = imgs.base64
      image.crossOrigin = ''
      imgs.md5 = this.base64ToArrayBufferToMD5(imgs.base64.substring(imgs.base64.indexOf(',', 1) + 1))
      imgs.mode = this.mode
      imgs.userId = JSON.parse(this.$store.state.data).userId
      $.ajax({
        type: 'post',
        url: window.server.COMMONS.checkMd5,
        dataType: 'json',
        data: {
          md5: imgs.md5,
          mode: _this.mode
        },
        success: function (response) {
          if (response.result) {
            const rects = JSON.parse(response.data).rects
            imgs.base64 = _this.drawDetections(image, rects)
            _this.$refs.serviceDisplay.updateDetectedImage(imgs)
          } else {
            $.ajax({
              type: 'post',
              url: _this.deepfakeDetector.detectUrl,
              dataType: 'json',
              contentType: 'application/json',
              data: JSON.stringify(imgs),
              success: function (response) {
                if (response.result) {
                  // 通过base64字符串加载图片
                  imgs.base64 = _this.drawDetections(image, response.data.rects)
                  _this.$refs.serviceDisplay.updateDetectedImage(imgs)
                } else {
                  // 显示错误信息
                  _this.$message.warning(response.msg)
                }
              }
            })
          }
        }
      })
    },
    drawDetections: function (img, detections) { // 绘制矩形框
      const canvas = document.createElement('canvas')
      canvas.width = img.width
      canvas.height = img.height
      const ctx = canvas.getContext('2d')
      ctx.drawImage(img, 0, 0, canvas.width, canvas.height)
      ctx.font = '20px Arial'
      detections.forEach(function (detection) {
        if (detection.type === 'fake') {
          ctx.strokeStyle = 'red' // 边框颜色
        } else {
          ctx.strokeStyle = 'green' // 边框颜色
        }
        ctx.lineWidth = 2 // 线条宽度
        ctx.strokeRect(detection.x1, detection.y1, detection.x2 - detection.x1, detection.y2 - detection.y1)
        const txt = '[ ' + detection.type + ', ' + detection.confidence + ' ]'
        ctx.lineWidth = 1 // 线条宽度
        ctx.strokeText(txt, detection.x1 + 3, detection.y2 - 5)
      })
      return canvas.toDataURL()
    },
    changeDisBoard: function (img) {
      this.$refs.serviceDisplay.changeDisboardToTask(img)
    },
    uploadZip: function (zipFile) {
      const _this = this
      zipFile.md5 = this.base64ToArrayBufferToMD5(zipFile.base64.substring(zipFile.base64.indexOf(',') + 1))
      zipFile.mode = this.mode
      zipFile.userId = JSON.parse(this.$store.state.data).userId
      $.ajax({
        type: 'post',
        url: window.server.COMMONS.checkMd5,
        dataType: 'json',
        data: {
          md5: zipFile.md5,
          mode: _this.mode
        },
        success: function (response) {
          if (response.result) {
            const url = JSON.parse(response.data)
            _this.$message.success('检测文本存储于: ' + url)
            _this.uploaded = 100
          } else {
            $.ajax({
              type: 'post',
              url: _this.deepfakeDetector.detectUrl,
              dataType: 'json',
              contentType: 'application/json',
              data: JSON.stringify(zipFile),
              success: function (response) {
                if (response.result) {
                  _this.$message.success('检测文本存储于: ' + response.data)
                } else {
                  _this.$message.warning(response.msg)
                }
              },
              xhr: function () { // 显示加载进度
                const xhr = $.ajaxSettings.xhr()
                if (xhr.upload) {
                  xhr.upload.addEventListener('progress', function (event) {
                    // 已经上传的进度条
                    _this.uploaded = Math.round(event.loaded / event.total * 100)
                  }, false)
                }
                return xhr
              }
            })
          }
        }
      })
    },
    downToZero: function () {
      this.uploaded = 0
    },
    base64ToArrayBufferToMD5: function (base64) {
      const binaryString = window.atob(base64)
      const len = binaryString.length
      const bytes = new Uint8Array(len)
      for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i)
      }
      const spark = new SparkMD5.ArrayBuffer()
      spark.append(bytes)
      const hexHash = spark.end(false)
      return hexHash
    },
    changeMode: function (mode) {
      this.mode = mode
      console.log(mode)
    }
  }
}
</script>
