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
    @keypress="onKeyPress"
    placeholder="12345678"
    persistent-hint
    type="text"
    maxlength="8"
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
      default: 'Digite o número USP (máximo 8 dígitos)',
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
      return this.required ? [this.validateNUSP] : [];
    },
  },
  methods: {
    onKeyPress(event) {
      const char = String.fromCharCode(event.keyCode);
      if (!/[0-9]/.test(char)) {
        event.preventDefault();
        return;
      }

      const currentValue = this.value || '';
      if (currentValue.length >= 8) {
        event.preventDefault();
        return;
      }
    },

    handleInput(value) {
      // Remove qualquer caractere não numérico e limita a 8 dígitos
      const numericValue = value.toString().replace(/\D/g, '').slice(0, 8);

      this.$emit('input', numericValue);
      this.validateInput(numericValue);
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

      if (value && value.length < 7) {
        this.hasError = true;
        this.errorMessage = 'NUSP deve ter pelo menos 7 dígitos';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateNUSP(value) {
      if (this.required && (!value || value.trim() === '')) {
        return 'Campo obrigatório';
      }

      if (value && value.length < 7) {
        return 'NUSP deve ter pelo menos 7 dígitos';
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
