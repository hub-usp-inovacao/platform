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
      default: 'Texto',
    },
    hint: {
      type: String,
      default: '',
    },
    required: {
      type: Boolean,
      default: false,
    },
    capitalization: {
      type: String,
      default: 'none',
      validator: (value) => ['none', 'title', 'upper', 'lower'].includes(value)
    },
    minLength: {
      type: Number,
      default: 0,
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
    formattedValue() {
      return this.applyCapitalization(this.value);
    },
    validationRules() {
      return this.required ? [this.validateText] : [];
    },
  },
  methods: {
    applyCapitalization(value) {
      if (!value) return '';

      switch (this.capitalization) {
        case 'title':
          return this.toTitleCase(value);
        case 'upper':
          return value.toUpperCase();
        case 'lower':
          return value.toLowerCase();
        default:
          return value;
      }
    },

    toTitleCase(str) {
      return str.replace(/\w\S*/g, (txt) => {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
      });
    },

    onKeyPress(event) {
      if (this.maxLength && this.value && this.value.length >= this.maxLength) {
        event.preventDefault();
        return;
      }
    },

    handleInput(value) {
      let processedValue = value;

      if (this.maxLength && value.length > this.maxLength) {
        processedValue = value.slice(0, this.maxLength);
      }

      this.$emit('input', processedValue);
      this.validateInput(processedValue);
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

      if (this.minLength && value && value.trim().length < this.minLength) {
        this.hasError = true;
        this.errorMessage = `Mínimo ${this.minLength} caracteres`;
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateText(value) {
      if (this.required && (!value || value.trim() === '')) {
        return 'Campo obrigatório';
      }

      if (this.minLength && value && value.trim().length < this.minLength) {
        return `Mínimo ${this.minLength} caracteres`;
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
