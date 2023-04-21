import SparkMD5 from 'spark-md5'
import store from '@/store/index'
import axios from 'axios'
import Vue from 'vue'

export default {
  getLocalTime: function (timeStamp) {
    const date = new Date(timeStamp + 8 * 3600 * 1000)
    return JSON.stringify(date).substr(6, 11).replace('T', ' ')
  },
  drawDetections: function (img, detections) { // 绘制矩形框
    const canvas = document.createElement('canvas')
    canvas.width = img.width
    canvas.height = img.height
    const ctx = canvas.getContext('2d')
    let multiFactor = 1
    ctx.drawImage(img, 0, 0, canvas.width, canvas.height)
    if (canvas.width > 1000 && canvas.height > 1000) {
      multiFactor = 2
    }
    detections.forEach(function (detection) {
      if (detection.type === 'fake') {
        ctx.strokeStyle = 'red' // 边框颜色
      } else {
        ctx.strokeStyle = 'green' // 边框颜色
      }
      ctx.font = 30 * multiFactor + 'px Arial'
      ctx.lineWidth = 2 * multiFactor // 线条宽度
      ctx.strokeRect(detection.x1, detection.y1, detection.x2 - detection.x1, detection.y2 - detection.y1)
      const txt = '[ ' + Math.round(100 - detection.confidence * 100) / 100 + ', ' + Math.round(detection.confidence * 100) / 100 + ' ]'
      ctx.lineWidth = 2 * multiFactor // 线条宽度
      ctx.textAlign = 'center'
      ctx.strokeText(txt, detection.x1 + (detection.x2 - detection.x1) / 2, detection.y2 - 5)
      ctx.lineWidth = 3 * multiFactor
      ctx.font = 50 * multiFactor + 'px Arial'
      ctx.strokeText(detection.type, detection.x1 + (detection.x2 - detection.x1) / 2, detection.y1 - 5)
    })
    ctx.strokeStyle = 'white'
    ctx.font = 20 * multiFactor + 'px Serif'
    ctx.lineWidth = 1 * multiFactor // 线条宽度
    ctx.textAlign = 'left'
    ctx.strokeText('注：[a,b]表示为[真概率，为假概率]', 3, canvas.height - 3)
    return canvas.toDataURL()
  },
  drawText: function (img, text) {
    const canvas = document.createElement('canvas')
    canvas.width = img.width
    canvas.height = img.height
    const ctx = canvas.getContext('2d')
    ctx.drawImage(img, 0, 0, canvas.width, canvas.height)
    ctx.strokeStyle = 'black'
    ctx.font = 40 + 'px Serif'
    ctx.lineWidth = 2 // 线条宽度
    ctx.textAlign = 'center'
    ctx.strokeText(text, img.width / 2, canvas.height - 5)
    return canvas.toDataURL()
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
  downLoadPic: function (picture) {
    // todo 如何给下载的文件命名
    const a = document.createElement('a')
    a.download = picture.name
    a.href = picture.base64
    a.click()
  },
  checkToken: function () {
    const token = store.state.Authorization
    const data = new FormData()
    data.append('token', token)
    return axios({
      url: window.server.COMMONS.authorization,
      method: 'post',
      data: data
    })
  },
  sendValidationMsg: function (email, type, btn, expireTime = null) {
    const data = new FormData()
    data.append('email', email)
    data.append('type', type)
    if (expireTime != null) {
      data.append('expireTime', expireTime)
    }
    axios({
      url: window.server.COMMONS.verification.genMsg,
      method: 'post',
      data: data
    }).then(function (resp) {
      if (resp.data.result) { // todo check
        Vue.prototype.$message.success('验证信息已经发送到邮箱!')
      } else {
        Vue.prototype.$message.warning(resp.data.msg)
      }
    })
    if (btn !== null) {
      btn.addClass('avoid')
      btn.val('重新发送(60s)')
      btn.attr('disabled', 'disabled')
      const waitProcess = setInterval(() => {
        const content = btn.val()
        let time = parseInt(content.substr(5, 2))
        time = time - 1
        if (time < 10) {
          btn.val('重新发送(0' + time + 's)')
        } else {
          btn.val('重新发送(' + time + 's)')
        }
      }, 1000)// 每隔一段时间修改时间
      setTimeout(() => {
        clearInterval(waitProcess)
        btn.removeClass('avoid')
        btn.removeAttr('disabled')
        btn.val('发送验证信息')
      }, 60000)
    }
  },
  checkValidationMsg: function (email, message, type) {
    const data = new FormData()
    data.append('email', email)
    data.append('type', type)
    data.append('message', message)
    return axios({
      url: window.server.COMMONS.verification.checkMsg,
      method: 'post',
      data: data
    })
  },
  getProjectResult: function (userId, detectId) {
    const data = new FormData()
    data.append('userId', userId)
    data.append('detectId', detectId)
    return axios.post(window.server.Project.detectProject.getResult, data)
  },
  getAllRecords: function (userId, detectId, detectType) {
    const data = new FormData()
    data.append('userId', userId)
    if (detectId !== null) {
      data.append('detectId', detectId)
    }
    if (detectType !== null) {
      data.append('detectType', detectType)
    }
    return axios.post(window.server.Project.file.conditionFile, data)
  }
}
