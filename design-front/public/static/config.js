window.server = {
  DEEPFAKE: {
    detectUrl: '/api/deepfake/upload'
  },
  NORMAL: {
    detectUrl: '/api/normal/upload'
  },
  COMMONS: {
    bigFileUpload: '/api/bigfile',
    userUrl: '/api/users',
    imageUrl: '/api/images',
    checkMd5: '/api/files/checkMd5'
  },
  Project: {
    detectProject: '/api/detectProject'
  }
}
window.getLocalTime = function (timeStamp) {
  const date = new Date(timeStamp + 8 * 3600 * 1000)
  return JSON.stringify(date).substr(6, 11).replace('T', ' ')
}
