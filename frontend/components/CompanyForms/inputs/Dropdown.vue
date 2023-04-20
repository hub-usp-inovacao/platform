<template>
  <v-select
    :items="options"
    :value="value"
    :label="label"
    clearable
    :disabled="disabled"
    :multiple="multipleOption"
    @input="$emit('input', $event)"
    :hint=hint
    persistent-hint
  />
</template>

<script>
export default {
  components: {},
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    multipleOption: {
      type: Boolean,
      default: false,
    },
    options: {
      type: Array,
      required: true,
    },
    value: {
      required: false,
    },
    label: {
      type: String,
      default: "",
    },
    hint: {
      type: String,
      default: "",
    }
  },
};
</script>
