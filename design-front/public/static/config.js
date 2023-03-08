window.server = {
  DEEPFAKE: {
    detectUrl: '/api/deepfake/upload'
  },
  NORMAL: {
    detectUrl: '/api/normal/upload'
  },
  COMMONS: {
    bigFileUpload: {
      base: '/api/bigfile',
      chunk: '/api/bigfile/chunk',
      merge: '/api/bigfile/mergeFile'
    },
    userUrl: {
      base: '/api/users',
      updateUser: '/api/users/updateUser',
      updatePhoto: '/api/users/updatePhoto',
      forgetPwd: '/api/users/forgetPwd',
      checkMsg: '/api/users/register/checkMsg',
      checkEmail: '/api/users/account/emailExist',
      genMsg: '/api/users/register/genMsg',
      checkAccount: '/api/users/account/isExist',
      register: '/api/users/register',
      login: '/api/users/login'
    },
    imageUrl: '/api/images',
    checkMd5: '/api/files/checkMd5',
    authorization: '/api/authorize'
  },
  Project: {
    base: '/api/detectProject',
    detectProject: {
      records: '/api/detectProject/records',
      projects: '/api/detectProject/projects',
      recentImages: '/api/detectProject/recent/images',
      file: '/api/detectProject/detectedFile',
      similarSearch: '/api/detectProject/project/similarSearch'
    }
  }
}
