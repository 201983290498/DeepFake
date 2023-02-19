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
                  <th>项目名称</th>
                  <th>开始日期</th>
                  <th>截止日期</th>
                  <th>状态</th>
                  <th>进度</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                  <td>1</td>
                  <td>设计新主题</td>
                  <td>10/02/2019</td>
                  <td>12/05/2019</td>
                  <td><span class="label label-warning">待定</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-warning" style="width: 45%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>2</td>
                  <td>网站重新设计</td>
                  <td>01/03/2019</td>
                  <td>12/04/2019</td>
                  <td><span class="label label-success">进行中</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-success" style="width: 30%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>3</td>
                  <td>模型设计</td>
                  <td>10/10/2019</td>
                  <td>12/11/2019</td>
                  <td><span class="label label-warning">待定</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-warning" style="width: 25%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>4</td>
                  <td>后台管理系统模板设计</td>
                  <td>25/01/2019</td>
                  <td>09/05/2019</td>
                  <td><span class="label label-success">进行中</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-success" style="width: 55%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>5</td>
                  <td>前端设计</td>
                  <td>10/10/2019</td>
                  <td>12/12/2019</td>
                  <td><span class="label label-danger">未开始</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-danger" style="width: 0%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>6</td>
                  <td>桌面软件测试</td>
                  <td>10/01/2019</td>
                  <td>29/03/2019</td>
                  <td><span class="label label-success">进行中</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-success" style="width: 75%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>7</td>
                  <td>APP改版开发</td>
                  <td>25/02/2019</td>
                  <td>12/05/2019</td>
                  <td><span class="label label-danger">暂停</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-danger" style="width: 15%;"></div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>8</td>
                  <td>Logo设计</td>
                  <td>10/02/2019</td>
                  <td>01/03/2019</td>
                  <td><span class="label label-warning">完成</span></td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div class="progress-bar progress-bar-success" style="width: 100%;"></div>
                    </div>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
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
          if (resp.data.result) {
            console.log(resp.data.data)
            _this.detectRecords = resp.data.data
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
