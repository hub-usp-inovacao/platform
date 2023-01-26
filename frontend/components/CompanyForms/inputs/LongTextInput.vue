<template>
  <v-textarea
    :value="value"
    :clearable="clearable"
    rows="2"
    :label="label"
    :hint="hint"
    persistent-hint
    @input="$emit('input', $event)"
  />
</template>

<script>
export default {
  components: {},
  props: {
    clearable: {
      type: Boolean,
      required: false,
      default: true,
    },
    value: {
      type: String,
    },
    label: {
      type: String,
      required: true,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
  },
};
</script>
