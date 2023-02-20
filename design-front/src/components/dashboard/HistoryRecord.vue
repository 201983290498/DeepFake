<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-lg-12">
        <div class="card">
          <div class="card-header">
            <h4>{{cardTitle}}</h4>
          </div>
          <div class="card-toolbar clearfix">
            <form class="pull-right search-bar" method="get" action="#!" role="form">
              <div class="input-group">
                <div class="input-group-btn">
                  <input type="hidden" name="search_field" id="search-field" value="title">
                  <button class="btn btn-default dropdown-toggle" id="search-btn" data-toggle="dropdown" type="button" aria-haspopup="true" aria-expanded="false">
                    标题 <span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu">
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="title">标题</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="cat_name">栏目</a> </li>
                  </ul>
                </div>
                <input type="text" class="form-control" value="" name="keyword" placeholder="请输入名称">
              </div>
            </form>
            <div class="toolbar-btn-action">
              <a class="btn btn-primary m-r-5" href="#!"><i class="mdi mdi-plus"></i> 新增</a>
              <a class="btn btn-success m-r-5" href="#!"><i class="mdi mdi-check"></i> 启用</a>
              <a class="btn btn-warning m-r-5" href="#!"><i class="mdi mdi-block-helper"></i> 禁用</a>
              <a class="btn btn-danger" href="#!"><i class="mdi mdi-window-close"></i> 删除</a>
            </div>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>#</th>
                  <th>记录名称</th>
                  <th>开始日期</th>
                  <th>截止日期</th>
                  <th>项目等级</th>
                  <th>检测文件</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(record, i) in detectRecords" :key="record.detectId">
                  <td>{{i+1}}</td>
                  <td>{{record.projectName}}</td>
                  <td>{{record.createTime}}</td>
                  <td>{{record.finishTime}}</td>
                  <td>{{record.projectLevel}}</td>
                  <td>{{record.detectFile}}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <ul class="pagination">
              <li class="disabled"><span>«</span></li>
              <li class="active"><span>1</span></li>
              <li><a href="#!">»</a></li>
            </ul>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
export default {
  name: 'HistoryRecord',
  data () {
    return {
      cardTitle: '检测记录',
      pageTitle: '控制台',
      detectRecords: [],
      user: JSON.parse(this.$store.state.data)
    }
  },
  methods: {
    queryRecord: function (pageNum) {
      console.log(pageNum)
      const _this = this
      $.ajax({
        url: window.server.Project.detectProject + '/records',
        method: 'post',
        data: {
          // userId: _this.user.userId,
          userId: 'default',
          pageNum: pageNum
        },
        success: function (resp) {
          resp = JSON.parse(resp)
          if (resp.result) {
            _this.detectRecords = resp.data
          } else {
            this.$message.warning('服务器请求失败。')
          }
        },
        error: function (error) {
          this.$message.warning(error)
        }
      })
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
    this.queryRecord(1)
  }
}
</script>

<style scoped>

</style>
