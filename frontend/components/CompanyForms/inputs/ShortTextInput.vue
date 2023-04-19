<template>
  <v-text-field
    :value="value"
    :clearable="clearable"
    :counter="counter"
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
    maxLength: {
      type: Number,
      required: false,
      default: -1,
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

  computed: {
    counter() {
      return this.maxLength <= 0 ? false : this.maxLength;
    },
  },
};
</script>
