<template>
  <v-text-field
    :value="value"
    :label="label"
    :hint="hint"
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
      type: String,
      default: '',
    },
    label: {
      type: String,
      default: 'URL',
    },
    hint: {
      type: String,
      default: 'Digite uma URL válida',
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
      return this.required ? [this.validateUrl] : [];
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
      if (this.required && (!value || value.trim() === '')) {
        this.hasError = true;
        this.errorMessage = 'Campo obrigatório';
        return false;
      }

      if (value && !this.isValidUrl(value)) {
        this.hasError = true;
        this.errorMessage = 'URL inválida';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateUrl(value) {
      if (this.required && (!value || value.trim() === '')) {
        return 'Campo obrigatório';
      }

      if (value && !this.isValidUrl(value)) {
        return 'URL inválida';
      }

      return true;
    },

    isValidUrl(url) {
      try {
        new URL(url);
        return url.startsWith('http://') || url.startsWith('https://');
      } catch {
        return false;
      }
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
