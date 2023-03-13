<template>
  <div id="picBoard" class="lyear-layout-content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-lg-12">
          <div class="card">
            <div class="card-body">
              <div class="masonry-grid" data-provide="photoswipe">
                <a v-for="(file, i) in files" :key="i" class="masonry-item" href="#">
                  <img src="" :alt="file.fileName">
                </a>
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
    files: function (newValue) {
      $('#picBoard masonry-item').each(function (index, node) { // 待检测
        const img = new Image()
        img.src = newValue[index].fileLocation
        img.setAttribute('crossOrigin', 'anonymous')
        img.onload = function () {
          const base64 = common.drawDetections(img, JSON.parse(newValue[index].fileResults).rects)
          node.attr('src', base64)
        }
      })
    }
  },
  props: {
    showFiles: {
      type: Array,
      default: () => { // todo 记住这个用法
        return []
      }
    }
  },
  created () {
    const _this = this
    if (this.showFiles.length === 0) { // 分两种模式, 传入数据和自动获取数据
      common.getAllRecords(this.user.userId, null, 'image').then((resp) => {
        _this.files = resp.data.data
      })
    } else {
      this.files = this.showFiles
    }
  }
}
</script>

<style scoped>

</style>
