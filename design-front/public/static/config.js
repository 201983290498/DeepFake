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
      merge: '/api/bigfile/mergeFile',
      projectFileMerge: '/api/bigfile/projectFileMergeFile'
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
      genMsg: '/api/verification/genMsg',
      checkMsg: '/api/verification/checkMsg'
    }
  },
  Project: {
    base: '/api/detectProject',
    detectProject: {
      projects: '/api/detectProject/projects',
      similarSearch: '/api/detectProject/project/similarSearch',
      delete: '/api/detectProject/project/delete',
      update: '/api/detectProject/project/update',
      insert: '/api/detectProject/project/insert',
      getResult: '/api/detectProject/project/results'
    },
    record: {
      recentImages: '/api/detectProject/recent/images',
      records: '/api/detectProject/records',
      recordSearch: '/api/detectProject/record/similarSearch'
    },
    file: {
      file: '/api/detectProject/detectedFile',
      conditionFile: '/api/detectProject/files'
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
