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
    persistent-hint
    type="text"
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
      default: 'Número',
    },
    hint: {
      type: String,
      default: 'Digite apenas números',
    },
    required: {
      type: Boolean,
      default: false,
    },
    minValue: {
      type: Number,
      default: null,
    },
    maxValue: {
      type: Number,
      default: null,
    },
    maxLength: {
      type: Number,
      default: null,
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
      return this.required ? [this.validateNumber] : [];
    },
  },
  methods: {
    onKeyPress(event) {
      const char = String.fromCharCode(event.keyCode);
      if (!/[0-9]/.test(char)) {
        event.preventDefault();
        return;
      }

      const currentValue = this.value?.toString() || '';
      if (this.maxLength && currentValue.length >= this.maxLength) {
        event.preventDefault();
        return;
      }
    },

    handleInput(value) {
      const numericValue = value.toString().replace(/\D/g, '');

      let finalValue = numericValue;
      if (this.maxLength && numericValue.length > this.maxLength) {
        finalValue = numericValue.slice(0, this.maxLength);
      }

      const intValue = finalValue ? parseInt(finalValue) : null;

      this.$emit('input', intValue);
      this.validateInput(finalValue);
    },

    handleBlur() {
      this.validateInput(this.value?.toString() || '');
    },

    validateInput(value) {
      const numValue = value ? parseInt(value) : null;

      if (this.required && (value === '' || numValue === null)) {
        this.hasError = true;
        this.errorMessage = 'Campo obrigatório';
        return false;
      }

      if (numValue !== null) {
        if (this.minValue !== null && numValue < this.minValue) {
          this.hasError = true;
          this.errorMessage = `Valor mínimo: ${this.minValue}`;
          return false;
        }

        if (this.maxValue !== null && numValue > this.maxValue) {
          this.hasError = true;
          this.errorMessage = `Valor máximo: ${this.maxValue}`;
          return false;
        }
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateNumber(value) {
      const numValue = value ? parseInt(value) : null;

      if (this.required && (value === '' || numValue === null)) {
        return 'Campo obrigatório';
      }

      if (numValue !== null) {
        if (this.minValue !== null && numValue < this.minValue) {
          return `Valor mínimo: ${this.minValue}`;
        }

        if (this.maxValue !== null && numValue > this.maxValue) {
          return `Valor máximo: ${this.maxValue}`;
        }
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value?.toString() || '');
    },
  },
};
</script>
