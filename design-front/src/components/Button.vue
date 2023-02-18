<template>
  <component
    :is="tag"
    :type="nativeType"
    :disabled="disabled || loading"
    class="btn"
    :class="[
      { 'btn-round': round },
      { 'btn-block': block },
      { 'btn-just-icon': icon },
      { [`btn-${type}`]: type && !outline },
      { [`btn-outline-${type}`]: type && outline },
      { [`btn-${size}`]: size },
      { 'btn-link': simple },
    ]"
  >
    <slot name="loading">
      <i v-if="loading" class="fa fa-spinner fa-spin"></i>
    </slot>
    <slot></slot>
  </component>
</template>
<script>
export default {
  name: 'PButton',
  props: {
    tag: {
      type: String,
      default: 'button'
    },
    round: Boolean,
    icon: Boolean,
    outline: Boolean,
    block: Boolean,
    loading: Boolean,
    disabled: Boolean,
    type: {
      type: String,
      default: 'default'
    },
    nativeType: {
      type: String,
      default: 'button'
    },
    size: {
      type: String,
      default: ''
    },
    simple: Boolean
  }
}
</script>
<style scoped>
@mixin btn-styles($btn-color, $btn-states-color) {
  background-color: $btn-color;
  border-color: $btn-color;
  color: $white-color;
  @include opacity(1);

  &:hover,
  &:focus,
  &:active,
  &.active,
  .show > &.dropdown-toggle{
    background-color: $btn-states-color;
    color: $white-color;
    border-color: $btn-states-color;
  }

  .caret{
    border-top-color: $white-color;
  }

  &.btn-link {
    color: $btn-color;

    &:hover,
    &:focus,
    &:active,
    &.active,
    .open > &.dropdown-toggle{
      background-color: $transparent-bg;
      color: $btn-states-color;
    }

    .caret{
      border-top-color: $btn-color;
    }
  }

  .caret{
    border-top-color: $white-color;
  }
}

@mixin btn-outline-styles($btn-color, $btn-states-color){
  border-color: $btn-color;
  color: $btn-color;
  @include opacity(1);

  &:hover,
  &:focus,
  &:active,
  &.active,
  .open > &.dropdown-toggle {
    background-color: $btn-color;
    color: $fill-font-color;
    border-color: $btn-color;
    .caret{
      border-top-color: $fill-font-color;
    }
  }

  .caret{
    border-top-color: $white-color;
  }

  &.disabled,
  &:disabled,
  &[disabled],
  fieldset[disabled] & {
    &,
    &:hover,
    &:focus,
    &.focus,
    &:active,
    &.active {
      background-color: $transparent-bg;
      border-color: $btn-color;
    }
  }
}

@mixin btn-size($padding-vertical, $padding-horizontal, $font-size, $line-height){
  font-size: $font-size;
  padding: $padding-vertical $padding-horizontal;

  &.btn-simple{
    padding: $padding-vertical + 2 $padding-horizontal;
  }

}

</style>
