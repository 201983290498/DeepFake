<template>
  <div class="container-fluid" id="historyProject">
    <div class="row">
      <div class="col-lg-12" style="overflow: hidden">
        <div class="card">
          <div class="card-header">
            <h4>{{cardTitle}}</h4>
          </div>
          <div class="card-toolbar clearfix">
            <SearchBar :names="['名称', '编号', '级别', '日期', '模式', '数量']"
                       :attrs="['projectName', 'detectId', 'projectLevel', 'createTime', 'mode', 'imageQuantity']"
                       :similar-search="similarSearch"
                       @changeValue="(value) => this.conditionValue=value"
                       @changeField="(value) => this.conditionField=value"></SearchBar>
            <div class="toolbar-btn-action">
<!--              todo label中的for与输入框input的id相同, 点击label是会聚焦到响应的输入框-->
              <router-link class="btn btn-primary btn-label m-r-5" v-show="!deleteMode" to="/showBoard/addProject"><label><i class="mdi mdi-plus"></i></label>新增</router-link>
              <span class="btn btn-secondary btn-label m-r-5" v-show="deleteMode" @click="deleteMode=false"><label><i class="mdi mdi-cancel"></i></label>取消</span>
              <span class="btn btn-danger btn-label" v-show="!deleteMode" @click="deleteMode=true"><label><i class="mdi mdi-window-close"></i></label>删除</span>
              <span class="btn btn-danger btn-label" v-show="deleteMode" @click="confirmDelete"><label><i class="mdi mdi-window-close"></i></label>确认删除</span>
            </div>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered text-center" id="projectTabel">
                <thead>
                <tr>
                  <th v-show="deleteMode" style="width: 5%;">
                    <label class="lyear-checkbox checkbox-primary">
                      <input type="checkbox" id="check-all" @click="checkAll"><span></span>
                    </label>
                  </th>
                  <th v-show="!deleteMode">#</th>
                  <th>
                    项目编号
                    <div class="d-inline-block">
                      <div class="dropup" style="margin-bottom: 2px;" @click="orderRecords('detectId', false)">
                        <span class="caret d-block" style="border-bottom: 6px dashed !important"></span>
                      </div>
                      <div @click="orderRecords('detectId', true)">
                        <span class="caret d-block" style="border-top: 6px dashed !important"></span>
                      </div>
                    </div></th>
                  <th>
                    项目名称
                    <div class="d-inline-block">
                      <div class="dropup" style="margin-bottom: 2px;" @click="orderRecords('projectName', false)">
                        <span class="caret d-block" style="border-bottom: 6px dashed !important"></span>
                      </div>
                      <div @click="orderRecords('projectName', true)">
                        <span class="caret d-block" style="border-top: 6px dashed !important"></span>
                      </div>
                    </div>
                  </th>
                  <th>级别</th>
                  <th>
                    开始日期
                    <div class="d-inline-block">
                      <div class="dropup" style="margin-bottom: 2px;" @click="orderRecords('createTime', false)">
                        <span class="caret d-block" style="border-bottom: 6px dashed !important"></span>
                      </div>
                      <div @click="orderRecords('createTime', true)">
                        <span class="caret d-block" style="border-top: 6px dashed !important"></span>
                      </div>
                    </div>
                  </th>
                  <th>
                    文件数量
                    <div class="d-inline-block">
                      <div class="dropup" style="margin-bottom: 2px;" @click="orderRecords('imageQuantity', false)">
                        <span class="caret d-block" style="border-bottom: 6px dashed !important"></span>
                      </div>
                      <div @click="orderRecords('imageQuantity', true)">
                        <span class="caret d-block" style="border-top: 6px dashed !important"></span>
                      </div>
                    </div>
                  </th>
                  <th>
                    检测模式
                  </th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(record, i) in detectPage.records" :key="record.detectId">
                  <td v-show="!deleteMode">{{(detectPage.current-1)*detectPage.size+i+1}}</td>
                  <td v-show="deleteMode" style="width: 5%;">
                    <label class="lyear-checkbox checkbox-primary">
                      <input type="checkbox" :data-detect-id="record.detectId" @change="checkboxChange" :checked="deleteIds.indexOf(record.detectId) !== -1"><span></span>
                    </label>
                  </td>
                  <td class="projectId"><router-link :to="{ name: 'ProjectDetail', params: { detectProject: record } }"> {{record.detectId}} </router-link></td>
                  <td class="project-name-td">{{record.projectName}}</td>
                  <td>{{record.projectLevel}}</td>
                  <td>{{getLocalTime(record.createTime)}}</td>
                  <td>{{record.imageQuantity}}</td>
                  <td>{{record.mode}}</td>
                  <td>
                    <div class="progress progress-striped progress-sm">
                      <div :class="'progress-bar ' + (record.finishTime !== null? 'progress-bar-success' : 'progress-bar-warning')" :style="'width: ' + (record.finishTime !== null? '100%;' : '75%;')">
                      </div>
                    </div>
                  </td>
                  <td>
                    <div class="btn-group">
                      <a class="btn btn-xs btn-default" href="#" title="编辑" data-toggle="tooltip" @click="editProject(i)"><i class="mdi mdi-pencil"></i></a>
                    </div>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <el-pagination @size-change="pageSizeChange" @current-change="currentPageChange" :current-page="detectPage.current" :page-count="detectPage.pages" :total="detectPage.total" :page-size="detectPage.size" :page-sizes="[5,10,15,20,25,30]" layout="total,sizes, prev, pager, next,jumper" background style="text-align: center" :hideOnSinglePage="true"></el-pagination>
          </div>
        </div>
      </div>
    </div>
<!--    todo 学习modal框的使用 -->
    <div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="confirmModalLabel" style="margin-top: 6%;">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" style="color:red; font-weight: bold;">您正在删除项目, 请慎重考虑！</h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid card-body">
              <form class="form-horizontal">
                <div class="form-group">
                  <label class="col-md-3 control-label" for="example-hf-email">邮箱</label>
                  <div class="col-md-7">
                    <input class="form-control" style="display: inline-block;width: 60%;" type="email" id="example-hf-email" name="example-hf-email" :placeholder="user.email.slice(0,4) + '******' + user.email.slice(-4)" disabled>
                    <input type="button" class="btn btn-primary" style="display: inline-block;width: 40%; vertical-align: top;" id="resendBtn" @click="sendValidationMsg" value="重新发送">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-md-3 control-label" for="example-hf-password">验证码</label>
                  <div class="col-md-7">
                    <input class="form-control" type="password" id="example-hf-password" name="validationInfo" placeholder="请输入验证码..">
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            <button type="button" class="btn btn-danger" @click="deleteRecords">删除</button>
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
  name: 'HistoryProjects',
  components: { SearchBar },
  data () {
    return {
      cardTitle: '项目记录',
      pageTitle: '历史项目',
      detectPage: [],
      user: JSON.parse(this.$store.state.data),
      conditionValue: '',
      conditionField: 'projectName',
      deleteMode: false,
      deleteIds: [],
      deleteNum: 0,
      lastDeleteIds: []
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
    },
    deleteMode: function () {
      this.deleteIds = []
      this.deleteNum = 0
    },
    detectPage: function (newValue) {
      for (const index in newValue.records) {
        newValue.records[index].delete = false
      }
    },
    deleteNum: function (newValue, oldValue) {
      // 反应普通按钮对最上面按钮的影响
      if (newValue === this.detectPage.size && oldValue !== this.detectPage.size) {
        $('#check-all').prop('checked', true)
      }
      if (newValue !== this.detectPage.size && oldValue === this.detectPage.size) {
        $('#check-all').prop('checked', false)
      }
    }
  },
  methods: {
    // 查询记录
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
      _this.deleteNum = 0
      if (resp.result) {
        _this.detectPage = resp.data
        for (let i = 0; i < _this.detectPage.records.length; i++) {
          if (this.lastDeleteIds.indexOf(_this.detectPage.records[i].detectId) !== -1) { // 如果文件已经被删除了, 则直接删除
            continue
          }
          if (_this.deleteIds.indexOf(_this.detectPage.records[i].detectId) !== -1) {
            _this.deleteNum += 1
          }
        }
      } else {
        _this.$message.warning('服务器请求失败。')
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
    similarSearch: function () {
      if (this.conditionValue !== '') {
        this.searchProject(this.user.userId, this.conditionField, this.conditionValue, true, 'createTime', 1, 10)
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
    // 删除ids
    deleteRecords: function () { // todo 需要解决formdata传入对象的问题
      const _this = this
      $('#confirmModal').modal('hide')
      const data = new FormData()
      data.append('userId', this.user.userId)
      data.append('email', this.user.email)
      data.append('token', this.$store.state.Authorization)
      data.append('validationInfo', $('#historyProject input[name=validationInfo]').val())
      data.append('detectIds', this.deleteIds)
      this.axios({
        url: window.server.Project.detectProject.delete,
        method: 'post',
        data: data
      }).then((resp) => {
        if (!resp.data.result) {
          _this.$message.warning(resp.data.msg)
        } else {
          _this.lastDeleteIds = _this.deleteIds
          _this.deleteMode = false
          if (_this.conditionValue === '' || _this.conditionValue === null) {
            _this.queryRecord(_this.user.userId, 1, _this.detectPage.size)
          } else {
            if (_this.conditionField !== 'createTime') {
              _this.searchProject(_this.user.userId, _this.conditionField, _this.conditionValue, true, 'createTime', 1, _this.detectPage.size)
            } else {
              _this.searchProject(_this.user.userId, _this.conditionField, _this.conditionValue.getTime(), false, 'createTime', 1, _this.detectPage.size)
            }
          }
        }
      })
    },
    confirmDelete: function () {
      $('#confirmModal').modal('show')
      if ($('#resendBtn').attr('disabled') !== 'disabled') {
        $('#resendBtn').click()
      }
    },
    sendValidationMsg: function () {
      common.sendValidationMsg(this.user.email, window.CONSTANT.EMAIL.delete, $('#resendBtn'), 300000)
    },
    checkAll: function (event) { // 复选框变化事件
      const _this = event.target
      // todo 学习$的逻辑, 返回满足条件的第一个祖先元素, 在向下寻找
      $(_this).closest('table').find('td .lyear-checkbox input[type=checkbox]').each(function () {
        if ($(this).prop('checked') !== $(_this).prop('checked')) {
          $(this).click()
        }
      })
    },
    checkboxChange: function (event) {
      const node = event.target
      const detectId = $(node).data('detectId')
      if ($(node).prop('checked')) {
        this.deleteIds.push(detectId)
        this.deleteNum += 1
      } else {
        this.deleteIds.splice(this.deleteIds.indexOf(detectId), 1)
        this.deleteNum -= 1
      }
    },
    // 修改项目名称
    editProject: function (projectId) {
      const _this = this
      const node = $('#historyProject td.project-name-td').get(projectId)
      const text = $(node).text()
      $(node).html(`<input value="${text}" style="width:80%; border: 0px;">`)
      $(node).find('input').val('').focus().val(text)
      $(node).find('input').blur((event) => {
        if (event.target.value === '' || event.target.value === text) {
          $(node).html(text)
          return
        }
        $.ajax({
          url: window.server.Project.detectProject.update,
          method: 'post',
          dataType: 'json',
          data: {
            userId: _this.user.userId,
            detectId: _this.detectPage.records[projectId].detectId,
            projectName: event.target.value
          },
          success: function (resp) {
            if (resp.result) {
              $(node).html(event.target.value)
            } else {
              _this.$message.warning(resp.msg)
            }
          }
        })
      })
    },
    // 排序模型
    orderRecords: function (orderField, ordered) {
      this.searchProject(this.user.userId, this.conditionField, this.conditionValue, ordered, orderField, 1, this.detectPage.size)
    },
    // 获取本地时间
    getLocalTime: function (timeStamp) {
      return common.getLocalTime(timeStamp)
    }
  },
  created () {
    const _this = this
    this.$emit('changeActivePage', _this.pageTitle)
    this.queryRecord(this.user.userId, 1, 10)
  },
  mounted () {
    $('th').addClass('text-center')
  }
}
</script>

<style scoped>
.dropdown .caret {
  border-top: 8px dashed !important;
}
.dropup .caret {
  border-bottun: 8px dashed !important;
}
#historyProject .projectId:hover {
  text-decoration: underline;
}
</style>
