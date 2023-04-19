<template>
  <div>
    <v-text-field
      v-mask="mask"
      :label="label"
      type="text"
      :rules="rules.input"
      :value="value"
      :disabled="disabled"
      :hint="hint"
      :placeholder="placeholder"
      persistent-hint
      @input="$emit('input', $event)"
    />
  </div>
</template>

<script>
export default {
  components: {},

  props: {
    label: {
      type: String,
      required: true,
    },
    rule: {
      type: RegExp,
      required: false,
      default: () => /.*/,
    },
    mask: {
      type: String,
      default: "",
    },
    value: {
      type: String,
    },
    disabled: {
      type: Boolean,
      required: false,
      default: () => false,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
    placeholder: {
      type: String,
      required: false,
      default: () => "",
    },
  },

  computed: {
    rules() {
      return {
        input: [(f) => this.rule.test(f) || "Formato inválido"],
      };
    },
  },
};
</script>
