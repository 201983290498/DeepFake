<template>
  <div id="picBoard">
    <div class="container-fluid">
      <div class="row">
        <div class="col-lg-12">
          <div class="card">
            <div class="card-body">
              <div class="masonry-grid" data-provide="photoswipe">
                <for v-for="record in files" :key="record.fileId" class="masonry-item" href="#">
                  <img src="#" :alt="record.detectFile">
                </for>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import common from '@/assets/js/common'
import $ from 'jquery'
export default {
  name: 'PicBoard',
  data () {
    return {
      files: [],
      user: JSON.parse(this.$store.state.data)
    }
  },
  watch: {
    files: function () {
      for (const item in this.files) {
        const file = this.files[item]
        const img = new Image()
        img.src = file.fileLocation
        img.setAttribute('crossOrigin', 'anonymous')
        img.onload = function () {
          console.log(img)
          const base64 = common.drawDetections(img, JSON.parse(file.fileResults).rects)
          $($('#picBoard .masonry-item img').get(item)).attr('src', base64)
        }
      }
    },
    showFiles: function () {
      this.files = this.showFiles
    }
  },
  props: {
    showFiles: {
      type: Array,
      default: () => { // todo 记住这个用法
        return []
      }
    },
    detail: {
      type: Boolean,
      default: () => {
        return false
      }
    }
  },
  created () {
    const _this = this
    if (!_this.detail) { // 分两种模式, 传入数据和自动获取数据
      common.getAllRecords(this.user.userId, null, 'image').then((resp) => {
        if (resp.data.result) {
          const files = []
          for (const item in resp.data.data) {
            const file = resp.data.data[item]
            if (file.fileLocation !== '') {
              files.push(file)
            }
          }
          _this.files = files
        }
      })
    } else {
      this.files = this.showFiles
    }
  }
}
</script>

<style scoped>

</style>
