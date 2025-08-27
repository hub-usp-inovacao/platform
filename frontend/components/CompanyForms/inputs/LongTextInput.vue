<template>
  <v-textarea
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
    :clearable="clearable"
    rows="4"
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
      default: 'Texto longo',
    },
    hint: {
      type: String,
      default: '',
    },
    required: {
      type: Boolean,
      default: false,
    },
    clearable: {
      type: Boolean,
      default: false,
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
    validationRules() {
      return this.required ? [this.validateText] : [];
    },
  },
  methods: {
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
