// 用来定义全局的常量
window.server = { // 1. 定义服务器url接口
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
      checkEmail: '/api/users/account/emailExist',
      checkAccount: '/api/users/account/isExist',
      register: '/api/users/register',
      login: '/api/users/login'
    },
    imageUrl: '/api/images',
    checkMd5: '/api/files/checkMd5',
    authorization: '/api/authorize',
    verification: {
      genMsg: '/api/users/verification/genMsg',
      checkMsg: '/api/users/verification/checkMsg'
    }
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
window.CONSTANT = { // 2.定义服务器使用的相关常量
  EMAIL: {
    register: 'register',
    forgetPwd: 'forgetPwd',
    confirm: 'confirm',
    delete: 'delete'
  }
}
