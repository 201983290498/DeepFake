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
            <div class="toolbar-btn-action">
              <a class="btn btn-primary m-r-5" href="#"><i class="mdi mdi-plus"></i> 新增</a>
              <a class="btn btn-danger" href="#"><i class="mdi mdi-window-close"></i> 删除</a>
            </div>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered text-center">
                <thead>
                <tr>
                  <th>#</th>
                  <th>项目编号</th>
                  <th>项目名称</th>
                  <th>级别</th>
                  <th>开始日期</th>
                  <th>图片数量</th>
                  <th>检测模式</th>
                  <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(record, i) in detectPage.records" :key="record.detectId">
                  <td>{{(detectPage.current-1)*detectPage.size+i+1}}</td>
                  <td>{{record.detectId}}</td>
                  <td>{{record.projectName}}</td>
                  <td>{{record.projectLevel}}</td>
                  <td>{{record.createTime}}</td>
                  <td>{{record.imageQuantity}}</td>
                  <td>{{record.mode}}</td>
                  <td :style="'color:' + (record.finishTime === '已完成'? 'green' : 'red')">{{record.finishTime}}</td>
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
  name: 'HistoryProjects',
  data () {
    return {
      cardTitle: '项目记录',
      pageTitle: '历史项目',
      detectPage: [],
      user: JSON.parse(this.$store.state.data)
    }
  },
  methods: {
    queryRecord: function (userId, pageNum, pageSize) {
      const _this = this
      $.ajax({
        url: window.server.Project.detectProject.projects,
        method: 'post',
        dataType: 'json',
        data: {
          userId,
          current: pageNum,
          pageSize: pageSize
        },
        success: function (resp) {
          if (resp.result) {
            _this.detectPage = resp.data
            console.log(_this.detectPage)
            for (let i = 0; i < _this.detectPage.records.length; i++) {
              _this.detectPage.records[i].createTime = common.getLocalTime(_this.detectPage.records[i].createTime)
              if (_this.detectPage.records[i].finishTime != null) {
                _this.detectPage.records[i].finishTime = '已完成'
              } else {
                _this.detectPage.records[i].finishTime = '检测中'
              }
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
      console.log(1)
    },
    pageSizeChange: function (pageSize) {
      this.queryRecord(this.user.userId, 1, pageSize)
    },
    currentPageChange: function (pageNum) {
      this.queryRecord(this.user.userId, pageNum, this.detectPage.size)
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
    this.queryRecord(this.user.userId, 1, 10)
    $('th').addClass('text-center')
  }
}
</script>

<style scoped>

</style>
