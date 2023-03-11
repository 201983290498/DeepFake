<template>
  <div class="df-detector">
    <ServiceTitle :titleHeader="title"></ServiceTitle>
    <ServiceModule @changeMode="changeMode"></ServiceModule>
    <ServiceDisplay :disImgs="disImgs" @uploadImage="uploadImage" ref="serviceDisplay"></ServiceDisplay>
    <ServiceUpload @uploadImage="uploadImage" @changeDisBoard="changeDisBoard" @uploadZip="uploadZip" ref="serviceUpload" :uploaded="uploaded" :downToZero="downToZero" :loginStatus="loginStatus"></ServiceUpload>
    <UploadFile style="margin-top: 35px" v-show="loginStatus" :mode="mode" title="大文件上传(>100M):"></UploadFile>
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
import common from '@/assets/js/common'
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
      disImgs: [],
      uploaded: 0,
      loginStatus: JSON.parse(this.$store.state.status),
      mode: 'accuracy',
      imgs: [require('@/assets/img/deepfake_examples/fake1.jpg'), require('@/assets/img/deepfake_examples/real1.jpg'), require('@/assets/img/deepfake_examples/fake2.jpg'), require('@/assets/img/deepfake_examples/real2.jpg'), require('@/assets/img/deepfake_examples/fake3.png'), require('@/assets/img/deepfake_examples/real3.png'), require('@/assets/img/deepfake_examples/fake4.png'), require('@/assets/img/deepfake_examples/real4.png'), require('@/assets/img/deepfake_examples/fake5.png'), require('@/assets/img/deepfake_examples/real5.png')],
      currentPage: 1
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
      imgs.md5 = common.base64ToArrayBufferToMD5(imgs.base64.substring(imgs.base64.indexOf(',', 1) + 1))
      imgs.mode = this.mode
      imgs.userId = JSON.parse(this.$store.state.data).userId
      imgs.fileName = imgs.fileName.substring(imgs.fileName.lastIndexOf('=') + 1)
      $.ajax({
        type: 'post',
        url: window.server.COMMONS.checkMd5,
        dataType: 'json',
        data: {
          fileName: imgs.fileName,
          md5: imgs.md5,
          mode: _this.mode
        },
        success: function (response) {
          if (response.result) {
            const data = JSON.parse(response.data)
            const rects = JSON.parse(data.fileResults).rects
            imgs.base64 = common.drawDetections(image, rects)
            _this.$refs.serviceDisplay.updateDetectedImage(imgs)
            if (data.fileLocation === '') {
              _this.addShowImage(data.fileName)
            } else {
              _this.addShowImage(data.fileLocation)
            }
          } else {
            $.ajax({
              type: 'post',
              url: _this.deepfakeDetector.detectUrl,
              dataType: 'json',
              contentType: 'application/json',
              data: JSON.stringify(imgs),
              success: function (response) {
                if (response.result) {
                  imgs.base64 = common.drawDetections(image, response.data.rects)
                  _this.$refs.serviceDisplay.updateDetectedImage(imgs)
                  setTimeout(() => {
                    _this.addShowImage(response.data.imageUrl)
                  }, 1500)
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
    changeDisBoard: function (img) {
      this.$refs.serviceDisplay.changeDisboardToTask(img)
    },
    uploadZip: function (zipFile) {
      const _this = this
      zipFile.md5 = common.base64ToArrayBufferToMD5(zipFile.base64.substring(zipFile.base64.indexOf(',') + 1))
      zipFile.mode = this.mode
      zipFile.userId = JSON.parse(this.$store.state.data).userId
      zipFile.fileName = zipFile.fileName.substring(zipFile.fileName.lastIndexOf('=') + 1)
      $.ajax({
        type: 'post',
        url: window.server.COMMONS.checkMd5,
        dataType: 'json',
        data: {
          fileName: zipFile.fileName,
          md5: zipFile.md5,
          mode: _this.mode
        },
        success: function (response) {
          if (response.result) {
            const url = JSON.parse(response.data).fileResults
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
    changeMode: function (mode) {
      this.mode = mode
    },
    getHistoryDetectedImages: function (userId, page) {
      const data = new FormData()
      data.append('userId', userId)
      data.append('page', page)
      return this.axios({
        url: window.server.Project.record.recentImages,
        method: 'post',
        data: data
      })
    },
    getDisplayImages: function (showImage) {
      const _this = this
      let height = 170
      if (!isNaN($('#example1 img').height()) && $('#example1 img').height() !== 0) {
        height = $('#example1 img').height()
      }
      let imageNum = $('.display-minor').height() / height + 1
      if (_this.disImgs.length >= imageNum) {
        return
      }
      imageNum = imageNum - _this.disImgs.length
      if (this.loginStatus) {
        const user = JSON.parse(this.$store.state.data)
        this.getHistoryDetectedImages(user.userId, _this.currentPage).then(resp => {
          if (resp.data.result) {
            _this.imgs = resp.data.data
            for (let i = 0; i < imageNum; i++) {
              _this.disImgs.push(_this.imgs[i])
            }
          } else {
            for (let i = 0; i < imageNum; i++) {
              _this.disImgs.push(_this.imgs[i])
            }
          }
          if (showImage) {
            _this.$refs.serviceDisplay.getBase64(_this.disImgs[0])
          }
        }).catch(() => {
          _this.$message.warning('服务器异常')
        })
      } else {
        for (let i = 0; i < imageNum; i++) {
          this.disImgs.push(this.imgs[i])
        }
        if (showImage) {
          _this.$refs.serviceDisplay.getBase64(_this.disImgs[0])
        }
      }
    },
    queryRecentList: function (maxNum) {
      const _this = this
      if (_this.loginStatus) {
        let insertNum = 0
        let i = 0
        for (; i < _this.imgs.length; i++) {
          const img = _this.imgs[i]
          if (_this.disImgs.indexOf(img) === -1) {
            _this.disImgs.push(img)
            insertNum += 1
          }
          if (insertNum === maxNum) {
            break
          }
        }
        if (insertNum < maxNum) {
          _this.currentPage += 1
          const user = JSON.parse(_this.$store.state.data)
          _this.getHistoryDetectedImages(user.userId, _this.currentPage).then(resp => {
            if (resp.data.result) {
              _this.imgs = resp.data.data
              _this.queryRecentList(maxNum - insertNum)
            } else {
              _this.currentPage -= 1
            }
          })
        }
      } else { // 未登入状态则将所有的检测图片放进去
        for (let i = 0; i < _this.imgs.length; i++) {
          const img = _this.imgs[i]
          if (_this.disImgs.indexOf(img) === -1) {
            _this.disImgs.push(img)
          }
        }
      }
    },
    addShowImage: function (url) {
      if (typeof url === 'undefined') {
        return
      }
      const _this = this
      const imageName = url.substring(url.lastIndexOf('/') + 1)
      let i = 0
      for (; i < _this.disImgs.length; i++) {
        if (_this.disImgs[i].indexOf(imageName) !== -1) {
          break
        }
      }
      if (i === _this.disImgs.length) {
        _this.disImgs.unshift(url)
      }
    }
  },
  mounted () {
    const _this = this
    $(window).resize(() => {
      _this.getDisplayImages(false)
    })
    // todo 滚动条的监听
    $('.display-minor.inb').scroll(function (e) {
      if (e.target.scrollTop + e.target.clientHeight === e.target.scrollHeight) {
        _this.queryRecentList(5)
      }
    })
    _this.getDisplayImages(true)
  }
}
</script>
