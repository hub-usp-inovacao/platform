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
    placeholder="R$ 0,00"
    persistent-hint
  />
</template>

<script>
export default {
  props: {
    value: {
      type: [String, Number],
      default: '',
    },
    label: {
      type: String,
      default: 'Valor',
    },
    hint: {
      type: String,
      default: 'Digite um valor em reais',
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
      return this.formatCurrency(this.value);
    },
    validationRules() {
      return this.required ? [this.validateCurrency] : [];
    },
  },
  methods: {
    formatCurrency(value) {
      if (!value) return '';

      const numbers = value.toString().replace(/\D/g, '');

      if (!numbers) return '';

      const cents = parseInt(numbers);

      const formatted = (cents / 100).toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL',
      });

      return formatted;
    },

    handleInput(value) {
      const cleanValue = value.replace(/[^\d,.\s]/g, '');
      const numbers = cleanValue.replace(/\D/g, '');

      const sanitizedNumbers = numbers || '0';

      this.$emit('input', sanitizedNumbers);
      this.validateInput(sanitizedNumbers);
    },

    handleBlur() {
      const finalValue = this.value || '0';
      this.validateInput(finalValue);
    },

    validateInput(value) {
      const numericValue = value || '0';

      if (this.required && (numericValue === '0' || numericValue === '')) {
        this.hasError = true;
        this.errorMessage = 'Campo obrigatório';
        return false;
      }

      if (isNaN(parseInt(numericValue))) {
        this.hasError = true;
        this.errorMessage = 'Valor inválido';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateCurrency(value) {
      if (this.required && (!value || value === '0')) {
        return 'Campo obrigatório';
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
