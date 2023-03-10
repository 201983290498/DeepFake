<template>
  <div class="searchBar">
    <form class="pull-right search-bar" method="get" role="form">
      <div class="input-group">
        <div class="input-group-btn">
          <button class="btn btn-default dropdown-toggle" id="search-btn" data-toggle="dropdown" type="button" aria-haspopup="true" aria-expanded="false">
            名称<span class="caret"></span>
          </button>
          <ul class="dropdown-menu">
            <!--                    todo data-field有什么用-->
            <li v-for="(item, i) in names" :key="i"> <a tabindex="-1" href="javascript:void(0)" :data-field="attrs[i]" >{{item}}</a> </li>
          </ul>
        </div>
        <input v-if="conditionField !== 'createTime'" type="text" class="form-control" id="search-input" v-model="conditionValue" placeholder="请输入名称">
        <el-date-picker v-if="conditionField === 'createTime'"
                        v-model="conditionValue"
                        type="datetime"
                        placeholder="选择开始的日期">
        </el-date-picker>
        <input type="submit" style="display: none;" value="提交" name="keyword" @click="similarSearch1">
      </div>
    </form>
  </div>
</template>

<script>
import $ from 'jquery'

export default {
  name: 'SearchBar',
  data () {
    return {
      conditionField: 'projectName',
      conditionValue: ''
    }
  },
  props: {
    names: [],
    attrs: [],
    similarSearch: Function
  },
  watch: {
    conditionValue: function (newValue) {
      this.$emit('changeValue', newValue)
    },
    conditionField: function (newValue) {
      if (newValue === 'createTime') {
        this.conditionValue = new Date()
      } else {
        this.conditionValue = ''
      }
      this.$emit('changeField', newValue)
    }
  },
  methods: {
    similarSearch1: function (event) {
      event.preventDefault()
      this.similarSearch()
    }
  },
  mounted () {
    const _this = this
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
