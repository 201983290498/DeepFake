<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-12" style="overflow: hidden">
        <div class="card">
          <div class="card-header">
            <h4>{{cardTitle}}</h4>
          </div>
          <div class="card-toolbar clearfix">
            <form class="pull-right search-bar" method="get" role="form">
              <div class="input-group">
                <div class="input-group-btn">
                  <input type="hidden" name="search_field" id="search-field" value="title">
                  <button class="btn btn-default dropdown-toggle" id="search-btn" data-toggle="dropdown" type="button" aria-haspopup="true" aria-expanded="false">
                    标题<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu">
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="title">标题</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="cat_name">栏目</a> </li>
                  </ul>
                </div>
                <input type="text" class="form-control" value="" name="keyword" placeholder="请输入名称">
                <input type="submit" style="display: none;" value="提交" name="keyword" @click="similarSearch">
              </div>
            </form>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered">
                <thead>
                <tr>
                  <th>#</th>
                  <th>记录名称</th>
                  <th>开始日期</th>
                  <th>截止日期</th>
                  <th>项目等级</th>
                  <th>检测文件</th>
                  <th>检测模式</th>
                  <th>操作</th>
                </tr>
                </thead>
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
export default {
  name: 'HistoryRecord',
  data () {
    return {
      cardTitle: '检测记录',
      pageTitle: '控制台',
      detectPage: [],
      user: JSON.parse(this.$store.state.data)
    }
  },
  methods: {
    queryRecord: function (userId, pageNum, pageSize) {
      const _this = this
      $.ajax({
        url: window.server.Project.detectProject + '/records',
        method: 'post',
        dataType: 'json',
        data: {
          userId,
          pageNum,
          pageSize
        },
        success: function (resp) {
          if (resp.result) {
            _this.detectPage = resp.data
            console.log(resp.data)
            for (let i = 0; i < _this.detectPage.records.length; i++) {
              _this.detectPage.records[i].createTime = common.getLocalTime(_this.detectPage.records[i].createTime)
              _this.detectPage.records[i].finishTime = common.getLocalTime(_this.detectPage.records[i].finishTime)
            }
          } else {
            _this.$message.warning('服务器请求失败。')
          }
        },
        error: function (error) {
          _this.$message.warning(error)
        }
      })
    },
    similarSearch: function (event) {
      event.preventDefault()
    },
    pageSizeChange: function (pageSize) {
      this.queryRecord(this.user.userId, 1, pageSize)
    },
    currentPageChange: function (pageNum) {
      this.queryRecord(this.user.userId, pageNum, this.detectPage.size)
    },
    preview: function (recordId) {
      const record = this.detectPage.records[recordId]
      console.log(record)
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
    this.queryRecord(this.user.userId, 1, 10)
  }
}
</script>

<style scoped>

</style>
