<template>
  <v-text-field
    :value="formattedValue"
    :label="label"
    :hint="hint"
    :error="hasError"
    :error-messages="errorMessage"
    :required="required"
    :disabled="disabled"
    :rules="validationRules"
    @input="handleInput"
    @blur="handleBlur"
    @keypress="onKeyPress"
    placeholder="00.000.000/0000-00"
    persistent-hint
    type="text"
    maxlength="18"
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
      default: 'CNPJ',
    },
    hint: {
      type: String,
      default: 'Digite o CNPJ no formato 00.000.000/0000-00',
    },
    required: {
      type: Boolean,
      default: false,
    },
    disabled: {
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
      return this.formatCnpj(this.value);
    },
    validationRules() {
      return this.required ? [this.validateCnpj] : [];
    },
  },
  methods: {
    onKeyPress(event) {
      const char = String.fromCharCode(event.keyCode);
      if (!/[0-9]/.test(char)) {
        event.preventDefault();
        return;
      }

      const currentNumbers = (this.value || '').replace(/\D/g, '');
      if (currentNumbers.length >= 14) {
        event.preventDefault();
        return;
      }
    },

    formatCnpj(value) {
      if (!value) return '';

      const numbers = value.replace(/\D/g, '');

      if (numbers.length <= 2) {
        return numbers;
      } else if (numbers.length <= 5) {
        return `${numbers.slice(0, 2)}.${numbers.slice(2)}`;
      } else if (numbers.length <= 8) {
        return `${numbers.slice(0, 2)}.${numbers.slice(2, 5)}.${numbers.slice(5)}`;
      } else if (numbers.length <= 12) {
        return `${numbers.slice(0, 2)}.${numbers.slice(2, 5)}.${numbers.slice(5, 8)}/${numbers.slice(8)}`;
      } else {
        const truncated = numbers.slice(0, 14);
        return `${truncated.slice(0, 2)}.${truncated.slice(2, 5)}.${truncated.slice(5, 8)}/${truncated.slice(8, 12)}-${truncated.slice(12)}`;
      }
    },

    handleInput(value) {
      const cleanValue = value.replace(/\D/g, '');

      const limitedValue = cleanValue.slice(0, 14);

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

      if (value && !this.isValidCnpj(value)) {
        this.hasError = true;
        this.errorMessage = 'CNPJ inválido';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateCnpj(value) {
      if (this.required && !value) {
        return 'Campo obrigatório';
      }

      if (value && !this.isValidCnpj(value)) {
        return 'CNPJ inválido';
      }

      return true;
    },

    isValidCnpj(cnpj) {
      if (!cnpj) return true;

      const numbers = cnpj.replace(/\D/g, '');

      if (numbers.length !== 14) return false;

      if (/^(\d)\1+$/.test(numbers)) return false;

      let soma = 0;
      let peso = 2;

      for (let i = 11; i >= 0; i--) {
        soma += parseInt(numbers.charAt(i)) * peso;
        peso = peso === 9 ? 2 : peso + 1;
      }

      let resto = soma % 11;
      let digito1 = resto < 2 ? 0 : 11 - resto;

      if (parseInt(numbers.charAt(12)) !== digito1) return false;

      soma = 0;
      peso = 2;

      for (let i = 12; i >= 0; i--) {
        soma += parseInt(numbers.charAt(i)) * peso;
        peso = peso === 9 ? 2 : peso + 1;
      }

      resto = soma % 11;
      let digito2 = resto < 2 ? 0 : 11 - resto;

      return parseInt(numbers.charAt(13)) === digito2;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
