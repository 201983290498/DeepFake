import SparkMD5 from 'spark-md5'

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
  getProcessImage: function (url, detections) {
    const image = new Image()
    image.src = url
    image.crossOrigin = ''
    return this.drawDetections(image, detections)
  }
}
