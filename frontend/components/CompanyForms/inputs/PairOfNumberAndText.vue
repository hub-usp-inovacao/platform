<template>
  <div class="d-flex">
    <v-text-field
      :value="firstValue"
      :label="firstLabel"
      :placeholder="firstPlaceholder"
      @input="handleFirstInput"
      @blur="handleFirstBlur"
      @keypress="onFirstKeyPress"
      class="mr-2"
      style="max-width: 150px;"
      maxlength="5"
      hint="Ex: 206-2"
      persistent-hint
    />
    <span class="align-self-center mx-2">{{ separator }}</span>
    <v-text-field
      :value="secondValue"
      :label="secondLabel"
      @input="handleSecondInput"
      @blur="handleSecondBlur"
      @keypress="onSecondKeyPress"
      class="flex-grow-1"
      :maxlength="maxSecondLength"
      hint="Descrição completa da natureza jurídica"
      persistent-hint
    />
  </div>
</template>

<script>
import { getDescricaoNaturezaJuridica, getCodigoNaturezaJuridica, isCodigoValido } from '@/lib/naturezaJuridica.js';

export default {
  props: {
    value: {
      type: String,
      default: '',
    },
    firstLabel: {
      type: String,
      default: 'Primeiro',
    },
    secondLabel: {
      type: String,
      default: 'Segundo',
    },
    firstPlaceholder: {
      type: String,
      default: '',
    },
    separator: {
      type: String,
      default: ' - ',
    },
  },
  data() {
    return {
      maxSecondLength: 100,
    };
  },
  computed: {
    firstValue() {
      if (!this.value) return '';
      const parts = this.value.split(this.separator);
      return parts[0] || '';
    },
    secondValue() {
      if (!this.value) return '';
      const parts = this.value.split(this.separator);
      return parts.slice(1).join(this.separator) || '';
    },
  },
  methods: {
    onFirstKeyPress(event) {
      const char = String.fromCharCode(event.keyCode);

      if (!/[0-9-]/.test(char)) {
        event.preventDefault();
        return;
      }

      if (this.firstValue.length >= 5) {
        event.preventDefault();
        return;
      }

      if (this.firstValue.length === 3 && char !== '-') {
        this.handleFirstInput(this.firstValue + '-' + char);
        event.preventDefault();
        return;
      }
    },

    onSecondKeyPress(event) {
      if (this.secondValue.length >= this.maxSecondLength) {
        event.preventDefault();
        return;
      }
    },

    handleFirstInput(value) {
      let formattedCode = this.formatCode(value);

      let description = this.secondValue;
      if (formattedCode.length === 5) {
        const foundDescription = getDescricaoNaturezaJuridica(formattedCode);
        if (foundDescription) {
          description = foundDescription;
        }
      }

      const combined = formattedCode + this.separator + description;
      this.$emit('input', combined);
      this.$emit('changeFirstField', formattedCode);
    },

    handleSecondInput(value) {
      let limitedValue = value;
      if (value.length > this.maxSecondLength) {
        limitedValue = value.slice(0, this.maxSecondLength);
      }

      let code = this.firstValue;
      const foundCode = getCodigoNaturezaJuridica(limitedValue);
      if (foundCode) {
        code = foundCode;
      }

      const combined = code + this.separator + limitedValue;
      this.$emit('input', combined);
      this.$emit('changeSecondField', limitedValue);
    },

    formatCode(value) {
      const cleaned = value.replace(/[^0-9-]/g, '');

      const singleHyphen = cleaned.replace(/-+/g, '-');

      let formatted = singleHyphen.slice(0, 5);

      if (formatted.length === 4 && !formatted.includes('-')) {
        formatted = formatted.slice(0, 3) + '-' + formatted.slice(3);
      }

      return formatted;
    },

    handleFirstBlur() {
      if (this.firstValue && this.firstValue.length === 5) {
        if (!isCodigoValido(this.firstValue)) {
          console.warn('Código de natureza jurídica inválido:', this.firstValue);
        }
      }
    },

    handleSecondBlur() {
    },

    set(field, value) {
      if (field === 'first') {
        this.handleFirstInput(value);
      } else if (field === 'second') {
        this.handleSecondInput(value);
      }
    },

    isValid() {
      if (this.firstValue) {
        if (this.firstValue.length !== 5) return false;
        if (!isCodigoValido(this.firstValue)) return false;
      }

      if (this.firstValue && !this.secondValue.trim()) {
        return false;
      }

      return true;
    },
  },
};
</script>

<style scoped>
.v-text-field {
  margin-bottom: 0;
}
</style>
