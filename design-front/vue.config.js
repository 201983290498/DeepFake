const { defineConfig } = require('@vue/cli-service')
const webpack = require('webpack')

module.exports = defineConfig({
  transpileDependencies: true,
  runtimeCompiler: true,
  chainWebpack: (config) => {
    config.plugin('provide').use(webpack.ProvidePlugin, [{
      $: 'jquery',
      jquery: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery'
    }])
  },
  css: {
    sourceMap: true
  },
  devServer: {
    host: '127.0.0.1',
    port: 8090,
    open: true, // vue项目启动时自动打开浏览器
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080/detector',
        changOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
})
