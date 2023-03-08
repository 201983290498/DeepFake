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
                  <button class="btn btn-default dropdown-toggle" id="search-btn" data-toggle="dropdown" type="button" aria-haspopup="true" aria-expanded="false">
                    名称<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu">
<!--                    todo data-field有什么用-->
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="projectName">名称</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="detectId">编号</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="projectLevel">级别</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="createTime">日期</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="mode">模式</a> </li>
                    <li> <a tabindex="-1" href="javascript:void(0)" data-field="imageQuantity">数量</a> </li>
                  </ul>
                </div>
                <input v-if="conditionField !== 'createTime'" type="text" class="form-control" id="search-input" v-model="conditionValue" placeholder="请输入名称">
                <el-date-picker v-if="conditionField === 'createTime'"
                  v-model="conditionValue"
                  type="datetime"
                  placeholder="选择开始的日期">
                </el-date-picker>
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
      user: JSON.parse(this.$store.state.data),
      conditionValue: '',
      conditionField: 'projectName'
    }
  },
  watch: {
    conditionValue: function (newValue, oldValue) {
      if (newValue === '') {
        this.queryRecord(this.user.userId, 1, 10)
      }
      if (this.conditionField === 'createTime') {
        // todo 学习jsDate类的使用
        this.searchProject(this.user.userId, this.conditionField, this.conditionValue.getTime(), false, 'createTime', 1, this.detectPage.pageSize)
      }
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
          _this.updateRecords(resp)
        },
        error: function (error) {
          _this.$message.warning(error)
        }
      })
    },
    updateRecords: function (resp) {
      const _this = this
      if (resp.result) {
        _this.detectPage = resp.data
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
    similarSearch: function (event) {
      event.preventDefault()
      if (this.conditionValue !== '') {
        this.searchProject(this.user.userId, this.conditionField, this.conditionValue, true, 'createTime', 1, 10)
      }
    },
    searchProject: function (userId, field, value, ordered, orderField, current, pageSize) {
      const _this = this
      $.ajax({
        url: window.server.Project.detectProject.similarSearch,
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
    pageSizeChange: function (pageSize) {
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
          this.searchProject(this.user.userId, this.conditionField, this.conditionValue, true, 'createTime', pageNum, this.detectPage.pageSize)
        } else {
          this.searchProject(this.user.userId, this.conditionField, this.conditionValue.getTime(), false, 'createTime', pageNum, this.detectPage.pageSize)
        }
      }
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
    this.queryRecord(this.user.userId, 1, 10)
  },
  mounted () {
    const _this = this
    $('th').addClass('text-center')
    $('.search-bar .dropdown-menu a').click(function () {
      const field = $(this).data('field') || ''
      _this.conditionField = field
      if (field !== 'imageQuantity') {
        $('#search-input').attr('placeholder', '请输入' + $(this).text())
      } else {
        $('#search-input').attr('placeholder', '输入至少包含的数量')
      }
      $('#search-btn').html($(this).text() + ' <span class="caret"></span>')
    })
  }
}
</script>

<style scoped>

</style>
