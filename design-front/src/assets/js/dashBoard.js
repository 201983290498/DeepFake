import $ from 'jquery'
export default {
  startMain: function () {
    $('body').on('click', '[data-stopPropagation]', function (e) {
      // e.stopPropagation()
    })
    // 侧边栏
    $(document).on('click', '.lyear-aside-toggler', function () {
      $('.lyear-layout-sidebar').toggleClass('lyear-aside-open')
      $('body').toggleClass('lyear-layout-sidebar-close')

      if ($('.lyear-mask-modal').length === 0) {
        $('<div class="lyear-mask-modal"></div>').prependTo('body')
      } else {
        $('.lyear-mask-modal').remove()
      }
    })

    // 遮罩层
    $(document).on('click', '.lyear-mask-modal', function () {
      $(this).remove()
      $('.lyear-layout-sidebar').toggleClass('lyear-aside-open')
      $('body').toggleClass('lyear-layout-sidebar-close')
    })

    // 侧边栏导航
    $(document).on('click', '.nav-item-has-subnav > a', function () {
      const $subnavToggle = $(this)
      const $navHasSubnav = $subnavToggle.parent()
      const $topHasSubNav = $subnavToggle.parents('.nav-item-has-subnav').last()
      const $subnav = $navHasSubnav.find('.nav-subnav').first()
      const $viSubHeight = $navHasSubnav.siblings().find('.nav-subnav:visible').outerHeight()
      const $scrollBox = $('.lyear-layout-sidebar-scroll')
      $navHasSubnav.siblings().find('.nav-subnav:visible').slideUp(500).parent().removeClass('open')
      $subnav.slideToggle(300, function () {
        $navHasSubnav.toggleClass('open')
        // 新增滚动条处理
        let scrollHeight = 0
        const pervTotal = $topHasSubNav.prevAll().length
        const boxHeight = $scrollBox.outerHeight()
        const innerHeight = $('.sidebar-main').outerHeight()
        const thisScroll = $scrollBox.scrollTop()
        const thisSubHeight = $(this).outerHeight()
        const footHeight = 121

        if (footHeight + innerHeight - boxHeight >= (pervTotal * 48)) {
          scrollHeight = pervTotal * 48
        }
        if ($subnavToggle.parents('.nav-item-has-subnav').length === 1) {
          $scrollBox.animate({ scrollTop: scrollHeight }, 300)
        } else {
          // 子菜单操作
          if (typeof ($viSubHeight) !== 'undefined' && $viSubHeight != null) {
            scrollHeight = thisScroll + thisSubHeight - $viSubHeight
            $scrollBox.animate({ scrollTop: scrollHeight }, 300)
          } else {
            if ((thisScroll + boxHeight - $scrollBox[0].scrollHeight) === 0) {
              scrollHeight = thisScroll - thisSubHeight
              $scrollBox.animate({ scrollTop: scrollHeight }, 300)
            }
          }
        }
      })
    })

    // 提示
    if ($('[data-toggle=tooltip]')[0]) {
      $('[data-toggle=tooltip]').tooltip({
        container: 'body'
      })
    }
    // 弹出框
    if ($('[data-toggle="popover"]')[0]) {
      $('[data-toggle="popover"]').popover()
    }

    // 标签
    $('.js-tags-input').each(function () {
      const $this = $(this)
      $this.tagsInput({
        height: $this.data('height') ? $this.data('height') : '38px',
        width: '100%',
        defaultText: $this.attr('placeholder'),
        removeWithBackspace: true,
        delimiter: [',']
      })
    })
    // 日期选择
    $('.js-datepicker').each(function () {
      const options = {
        weekStart: 1,
        autoclose: typeof ($(this).data('auto-close')) !== 'undefined' ? $(this).data('auto-close') : true,
        language: 'zh-CN', // 默认简体中文
        multidateSeparator: ', ', // 默认多个日期用,分隔
        format: $(this).data('date-format') ? $(this).data('date-format') : 'yyyy-mm-dd'
      }
      if ($(this).prop('tagName') !== 'INPUT') {
        options.inputs = [$(this).find('input:first'), $(this).find('input:last')]
      }
      $(this).datepicker(options)
    })

    // 颜色选取
    $('.js-colorpicker').each(function () {
      const $colorpicker = $(this)
      $colorpicker.colorpicker({
        format: $colorpicker.data('colorpicker-mode') ? $colorpicker.data('colorpicker-mode') : 'hex',
        inline: $colorpicker.data('colorpicker-inline')
      })
    })

    // 复选框全选
    $('#check-all').change(function () {
      const $boxname = $(this).data('name')
      if ($boxname) {
        $(this).closest('table').find("input[name='" + $boxname + "']").prop('checked', $(this).prop('checked'))
      } else {
        $(this).closest('table').find(".lyear-checkbox input[type='checkbox']").prop('checked', $(this).prop('checked'))
      }
    })
    // 设置主题配色
    const setTheme = function (inputName, dataName) {
      $("input[name='" + inputName + "']").click(function () {
        $('body').attr(dataName, $(this).val())
      })
    }
    setTheme('site_theme', 'data-theme')
    setTheme('logo_bg', 'data-logobg')
    setTheme('header_bg', 'data-headerbg')
    setTheme('sidebar_bg', 'data-sidebarbg')
  }
}
