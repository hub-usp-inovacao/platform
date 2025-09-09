<template>
  <v-select
    :value="value"
    :items="options"
    :label="label"
    :hint="hint"
    :multiple="multipleOption"
    :error="hasError"
    :error-messages="errorMessage"
    :required="required"
    :rules="validationRules"
    @input="handleInput"
    @blur="handleBlur"
    persistent-hint
  />
</template>

<script>
export default {
  props: {
    value: {
      type: [String, Array],
      default: null,
    },
    options: {
      type: Array,
      required: true,
    },
    label: {
      type: String,
      default: 'Selecione',
    },
    hint: {
      type: String,
      default: '',
    },
    multipleOption: {
      type: Boolean,
      default: false,
    },
    required: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      hasError: false,
      errorMessage: '',
    };
  },
  computed: {
    validationRules() {
      return this.required ? [this.validateSelection] : [];
    },
  },
  methods: {
    handleInput(value) {
      this.$emit('input', value);
      this.validateInput(value);
    },

    handleBlur() {
      this.validateInput(this.value);
    },

    validateInput(value) {
      if (this.required) {
        if (this.multipleOption) {
          if (!value || !Array.isArray(value) || value.length === 0) {
            this.hasError = true;
            this.errorMessage = 'Campo obrigatório';
            return false;
          }
        } else {
          if (!value || value === '') {
            this.hasError = true;
            this.errorMessage = 'Campo obrigatório';
            return false;
          }
        }
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateSelection(value) {
      if (this.required) {
        if (this.multipleOption) {
          if (!value || !Array.isArray(value) || value.length === 0) {
            return 'Campo obrigatório';
          }
        } else {
          if (!value || value === '') {
            return 'Campo obrigatório';
          }
        }
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
