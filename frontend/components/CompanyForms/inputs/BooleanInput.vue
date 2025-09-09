<template>
  <v-checkbox
    :input-value="value"
    :label="label"
    :hint="hint"
    @change="handleChange"
    persistent-hint
  />
</template>

<script>
export default {
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    label: {
      type: String,
      default: 'Opção',
    },
    hint: {
      type: String,
      default: '',
    },
  },
  methods: {
    handleChange(value) {
      this.$emit('input', value);
    },
  },
};
</script>
