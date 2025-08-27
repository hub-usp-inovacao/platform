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
      default: 'Campo',
    },
    hint: {
      type: String,
      default: '',
    },
    mask: {
      type: String,
      required: true,
    },
    rule: {
      type: RegExp,
      default: null,
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
      return this.applyMask(this.value);
    },
    validationRules() {
      return this.required ? [this.validateField] : [];
    },
    maxInputLength() {
      return (this.mask.match(/#/g) || []).length;
    },
  },
  methods: {
    onKeyPress(event) {
      const char = String.fromCharCode(event.keyCode);

      if (!/[0-9]/.test(char)) {
        event.preventDefault();
        return;
      }

      const currentValidChars = (this.value || '').replace(/\D/g, '');
      if (currentValidChars.length >= this.maxInputLength) {
        event.preventDefault();
        return;
      }
    },

    applyMask(value) {
      if (!value) return '';

      const cleanValue = value.replace(/\D/g, '');

      let maskedValue = '';
      let valueIndex = 0;

      for (let i = 0; i < this.mask.length && valueIndex < cleanValue.length; i++) {
        const maskChar = this.mask[i];

        if (maskChar === '#') {
          maskedValue += cleanValue[valueIndex];
          valueIndex++;
        } else {
          maskedValue += maskChar;
        }
      }

      return maskedValue;
    },

    handleInput(value) {
      const cleanValue = value.replace(/\D/g, '');
      const limitedValue = cleanValue.slice(0, this.maxInputLength);

      this.$emit('input', limitedValue);
      this.validateInput(limitedValue);
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

      if (value && this.rule && !this.rule.test(this.formattedValue)) {
        this.hasError = true;
        this.errorMessage = 'Formato inválido';
        return false;
      }

      this.hasError = false;
      this.errorMessage = '';
      return true;
    },

    validateField(value) {
      if (this.required && (!value || value.trim() === '')) {
        return 'Campo obrigatório';
      }

      if (value && this.rule && !this.rule.test(this.formattedValue)) {
        return 'Formato inválido';
      }

      return true;
    },

    isValid() {
      return this.validateInput(this.value);
    },
  },
};
</script>
