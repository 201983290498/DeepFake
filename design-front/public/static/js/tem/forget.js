$(document).ready(function() {
  $('.guide-box').bootstrapWizard({
    'tabClass': 'nav-step',
    'nextSelector': '[data-wizard="next"]',
    'previousSelector': '[data-wizard="prev"]',
    'finishSelector': '[data-wizard="finish"]',
    'onTabClick': function(e, t, i) {
      if (!$('.guide-box').is('[data-navigateable="true"]')) return ! 1
    },
    'onTabShow': function(e, t, i) {
      t.children(":gt(" + i + ").complete").removeClass("complete");
      t.children(":lt(" + i + "):not(.complete)").addClass("complete");
    },
    'onFinish': function(e, t, i) {
      // 点击完成后处理提交
      return false;
    }
  });
});
