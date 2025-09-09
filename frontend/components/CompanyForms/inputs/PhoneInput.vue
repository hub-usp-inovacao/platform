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
    @keypress="onKeyPress"
    placeholder="(11) 99999-9999"
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
      default: 'Telefone',
    },
    hint: {
      type: String,
      default: 'Digite o telefone no formato (11) 99999-9999',
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
      return this.formatPhone(this.value);
    },
    validationRules() {
      return this.required ? [this.validatePhone] : [];
    },
  },
  methods: {
    formatPhone(value) {
      if (!value) return '';

      const numbers = value.replace(/\D/g, '');

      if (numbers.length <= 2) {
        return `(${numbers}`;
      } else if (numbers.length <= 6) {
        return `(${numbers.slice(0, 2)}) ${numbers.slice(2)}`;
      } else if (numbers.length <= 10) {
        return `(${numbers.slice(0, 2)}) ${numbers.slice(2, 6)}-${numbers.slice(6)}`;
      } else {
        const truncated = numbers.slice(0, 11);
        return `(${truncated.slice(0, 2)}) ${truncated.slice(2, 7)}-${truncated.slice(7)}`;
      }
    },

    handleInput(value) {
      const cleanValue = value.replace(/\D/g, '');

      const limitedValue = cleanValue.slice(0, 11);

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

      if (value && !this.isValidPhone(value)) {
        this.hasError = true;
        this.errorMessage = 'Telefone inválido';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validatePhone(value) {
      if (this.required && !value) {
        return 'Campo obrigatório';
      }

      if (value && !this.isValidPhone(value)) {
        return 'Telefone inválido';
      }

      return true;
    },

    isValidPhone(phone) {
      if (!phone) return true;

      const numbers = phone.replace(/\D/g, '');

      if (numbers.length < 10 || numbers.length > 11) return false;

      if (/^(\d)\1+$/.test(numbers)) return false;

      if (numbers.length === 11 && numbers.charAt(2) !== '9') return false;

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },

    onKeyPress(event) {
      const char = String.fromCharCode(event.keyCode);
      if (!/[0-9]/.test(char)) {
        event.preventDefault();
        return;
      }

      const currentNumbers = (this.value || '').replace(/\D/g, '');
      if (currentNumbers.length >= 11) {
        event.preventDefault();
        return;
      }
    },
  },
};
</script>
