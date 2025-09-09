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
    placeholder="exemplo@dominio.com"
    persistent-hint
    type="email"
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
      default: 'Email',
    },
    hint: {
      type: String,
      default: 'Digite um email válido',
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
      return this.required ? [this.validateEmail] : [];
    },
  },
  methods: {
    handleInput(value) {
      this.$emit('input', value.toLowerCase().trim());
      this.validateInput(value);
    },

    handleBlur() {
      this.validateInput(this.value);
    },

    validateInput(value) {
      if (this.required && !value.trim()) {
        this.hasError = true;
        this.errorMessage = 'Campo obrigatório';
        return false;
      }

      if (value && !this.isValidEmail(value)) {
        this.hasError = true;
        this.errorMessage = 'Email inválido';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateEmail(value) {
      if (this.required && !value.trim()) {
        return 'Campo obrigatório';
      }

      if (value && !this.isValidEmail(value)) {
        return 'Email inválido';
      }

      return true;
    },

    isValidEmail(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(email);
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
