<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-12" style="overflow: hidden">
        <div class="card">
          <div class="card-header">
            <h4>{{cardTitle}}</h4>
          </div>
<!--         search bar -->
          <div class="card-toolbar clearfix">
            <SearchBar :names="['名称', '日期', '级别', '文件' , '模式']"
                       :attrs="['projectName', 'createTime', 'projectLevel', 'detectFile', 'mode']"
                       :similar-search="similarSearch"
                       @changeValue="(value) => this.conditionValue=value"
                       @changeField="(value) => this.conditionField=value"></SearchBar>
          </div>
<!--          table -->
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered">
<!--                table的头部-->
                <thead>
                  <tr>
                    <th>#</th>
                    <th>记录名称</th>
                    <th>开始日期</th>
                    <th>截止日期</th>
                    <th>项目级别</th>
                    <th>检测文件</th>
                    <th>检测模式</th>
                    <th>操作</th>
                  </tr>
                </thead>
<!--                table的表格主体-->
                <tbody>
                <tr v-for="(record, i) in detectPage.records" :key="record.detectId">
                  <td>{{(detectPage.current-1)*detectPage.size+i+1}}</td>
                  <td>{{record.projectName}}</td>
                  <td>{{record.createTime}}</td>
                  <td>{{record.finishTime}}</td>
                  <td>{{record.projectLevel}}</td>
                  <td>{{record.detectFile}}</td>
                  <td>{{record.mode}}</td>
                  <td>
                    <div class="btn-group">
                      <a class="btn btn-xs btn-default" href="#" title="编辑" data-toggle="tooltip"><i class="mdi mdi-pencil"></i></a>
                      <span class="btn btn-xs btn-default" @click="preview(i)" title="结果预览" data-toggle="tooltip"><i class="mdi mdi-eye"></i></span>
                    </div>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <el-pagination @size-change="pageSizeChange"
                           @current-change="currentPageChange"
                           :current-page="detectPage.current"
                           :page-count="detectPage.pages"
                           :total="detectPage.total"
                           :page-size="detectPage.size"
                           :page-sizes="[5,10,15,20,25,30]"
                           layout="total,sizes, prev, pager, next,jumper" background style="text-align: center" :hideOnSinglePage="true">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
import common from '@/assets/js/common'
import SearchBar from '@/components/Inputs/SearchBar.vue'
export default {
  name: 'HistoryRecords',
  components: { SearchBar },
  data () {
    return {
      cardTitle: '检测记录',
      pageTitle: '控制台',
      detectPage: [],
      user: JSON.parse(this.$store.state.data),
      conditionValue: '',
      conditionField: 'projectName'
    }
  },
  watch: {
    conditionValue: function (newValue) {
      if (newValue === '') {
        this.queryRecord(this.user.userId, 1, this.detectPage.size)
      }
      if (this.conditionField === 'createTime') {
        // todo 学习jsDate类的使用
        this.searchProject(this.user.userId, this.conditionField, this.conditionValue.getTime(), false, 'createTime', 1, this.detectPage.size)
      }
    }
  },
  methods: {
    // 更新记录
    queryRecord: function (userId, pageNum, pageSize) {
      const _this = this
      $.ajax({
        url: window.server.Project.record.records,
        method: 'post',
        dataType: 'json',
        data: {
          userId,
          pageNum,
          pageSize
        },
        success: function (resp) {
          _this.updateRecords(resp)
        },
        error: function (error) {
          _this.$message.warning(error)
        }
      })
    },
    searchProject: function (userId, field, value, ordered, orderField, current, pageSize) {
      const _this = this
      $.ajax({
        url: window.server.Project.record.recordSearch,
        method: 'post',
        dataType: 'json',
        data: {
          userId,
          current,
          pageSize,
          field,
          value,
          ordered,
          orderField
        },
        success: function (resp) {
          _this.updateRecords(resp)
        },
        error: function (error) {
          _this.$message.warning(error)
        }

      })
    },
    similarSearch: function () {
      if (this.conditionValue !== '') {
        this.searchProject(this.user.userId, this.conditionField, this.conditionValue, true, 'createTime', 1, 10)
      }
    },
    updateRecords: function (resp) {
      const _this = this
      if (resp.result) {
        _this.detectPage = resp.data
        for (let i = 0; i < _this.detectPage.records.length; i++) {
          _this.detectPage.records[i].createTime = common.getLocalTime(_this.detectPage.records[i].createTime)
          _this.detectPage.records[i].finishTime = common.getLocalTime(_this.detectPage.records[i].finishTime)
        }
      } else {
        _this.$message.warning('服务器请求失败。')
      }
    },
    // 分页
    pageSizeChange: function (pageSize) {
      this.detectPage.size = pageSize
      if (this.conditionValue === '' || this.conditionValue === null) {
        this.queryRecord(this.user.userId, 1, pageSize)
      } else {
        if (this.conditionField !== 'createTime') {
          this.searchProject(this.user.userId, this.conditionField, this.conditionValue, true, 'createTime', 1, pageSize)
        } else {
          this.searchProject(this.user.userId, this.conditionField, this.conditionValue.getTime(), false, 'createTime', 1, pageSize)
        }
      }
    },
    currentPageChange: function (pageNum) {
      if (this.conditionValue === '' || this.conditionValue === null) {
        this.queryRecord(this.user.userId, pageNum, this.detectPage.size)
      } else {
        if (this.conditionField !== 'createTime') {
          this.searchProject(this.user.userId, this.conditionField, this.conditionValue, true, 'createTime', pageNum, this.detectPage.size)
        } else {
          this.searchProject(this.user.userId, this.conditionField, this.conditionValue.getTime(), false, 'createTime', pageNum, this.detectPage.size)
        }
      }
    },
    // 单页预览
    preview: function (recordId) {
      const _this = this
      const record = this.detectPage.records[recordId]
      if (record.fileLocation.endsWith('zip')) {
        _this.$message.warning('无法预览压缩包的检测结果')
        return null
      }
      const img = new Image()
      img.src = record.fileLocation
      img.setAttribute('crossOrigin', 'anonymous')
      img.onload = function () {
        const image = {}
        image.base64 = common.drawDetections(img, JSON.parse(record.fileResults).rects)
        image.name = record.detectFile
        _this.$emit('oneImageShow', image)
      }
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
    this.queryRecord(this.user.userId, 1, 10)
  },
  mounted () {
    $('th').addClass('text-center')
    $('table').addClass('text-center')
  }
}
</script>

<style scoped>

</style>
