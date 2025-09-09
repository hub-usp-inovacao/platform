<template>
  <v-text-field
    :value="formattedValue"
    :label="label"
    :hint="hint"
    :error="hasError"
    :error-messages="errorMessage"
    :required="required"
    :rules="validationRules"
    @input="handleInput"
    @blur="handleBlur"
    placeholder="12345678"
    persistent-hint
    type="tel"
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
      default: 'Número USP',
    },
    hint: {
      type: String,
      default: 'Digite o NUSP (máximo 8 dígitos)',
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
    formattedValue() {
      return this.value;
    },
    validationRules() {
      return this.required ? [this.validateNusp] : [];
    },
  },
  methods: {
    handleInput(value) {
      const numbers = value.replace(/\D/g, '');

      const limitedValue = numbers.slice(0, 8);

      this.$emit('input', limitedValue);
      this.validateInput(limitedValue);
    },

    handleBlur() {
      this.validateInput(this.value);
    },

    validateInput(value) {
      if (this.required && !value) {
        this.hasError = true;
        this.errorMessage = 'Campo obrigatório';
        return false;
      }

      if (value && (value.length < 7 || value.length > 8)) {
        this.hasError = true;
        this.errorMessage = 'NUSP deve ter 7 ou 8 dígitos';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateNusp(value) {
      if (this.required && !value) {
        return 'Campo obrigatório';
      }

      if (value && (value.length < 7 || value.length > 8)) {
        return 'NUSP deve ter 7 ou 8 dígitos';
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
