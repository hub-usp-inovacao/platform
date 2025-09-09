<template>
  <v-text-field
    :value="displayValue"
    :label="label"
    :hint="hint"
    :placeholder="placeholder"
    :required="required"
    :disabled="disabled"
    persistent-hint
    @input="handleInput"
    @focus="handleFocus"
    @blur="handleBlur"
  />
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: "R$ 0,00",
    },
    label: {
      type: String,
      default: "Valor",
    },
    hint: {
      type: String,
      default: "",
    },
    placeholder: {
      type: String,
      default: "0,00",
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
      internalValue: "",
      isFocused: false,
    };
  },
  computed: {
    displayValue() {
      if (this.isFocused) {
        return this.internalValue;
      }

      if (!this.value || this.value === "R$ 0,00") {
        return "";
      }

      return this.value;
    },
  },
  watch: {
    value: {
      immediate: true,
      handler(newValue) {
        if (!this.isFocused) {
          this.internalValue = this.getNumericPart(newValue || "");
        }
      }
    }
  },
  methods: {
    getNumericPart(value) {
      if (!value || value === "R$ 0,00") return "";
      return value.replace("R$ ", "").replace("R$", "");
    },

    handleFocus() {
      this.isFocused = true;
      this.internalValue = this.getNumericPart(this.value);
    },

    handleBlur() {
      this.isFocused = false;

      if (!this.internalValue || this.internalValue.trim() === "") {
        this.$emit("input", "R$ 0,00");
        return;
      }

      const formattedValue = this.formatCurrency(this.internalValue);
      this.$emit("input", `R$ ${formattedValue}`);
    },

    handleInput(inputValue) {
      this.internalValue = this.cleanInput(inputValue);

      if (!this.internalValue) {
        this.$emit("input", "R$ 0,00");
        return;
      }

      const previewValue = this.formatCurrency(this.internalValue);
      this.$emit("input", `R$ ${previewValue}`);
    },

    cleanInput(value) {
      if (!value) return "";

      let cleaned = value.replace(/[^\d,\.]/g, "");

      if (cleaned.includes(",")) {
        const parts = cleaned.split(",");
        if (parts.length === 2) {
          const integerPart = parts[0].replace(/\./g, "");
          const decimalPart = parts[1].substring(0, 2);
          cleaned = decimalPart ? `${integerPart},${decimalPart}` : integerPart;
        } else {
          const lastCommaIndex = cleaned.lastIndexOf(",");
          const integerPart = cleaned.substring(0, lastCommaIndex).replace(/[,\.]/g, "");
          const decimalPart = cleaned.substring(lastCommaIndex + 1).replace(/[,\.]/g, "").substring(0, 2);
          cleaned = decimalPart ? `${integerPart},${decimalPart}` : integerPart;
        }
      } else {
        // Remove pontos (separadores de milhares)
        cleaned = cleaned.replace(/\./g, "");
      }

      return cleaned;
    },

    formatCurrency(value) {
      if (!value || value === "0") return "0,00";

      let cleanValue = value.toString();

      if (cleanValue.includes(",")) {
        const parts = cleanValue.split(",");
        const integerPart = this.addThousandsSeparator(parts[0] || "0");
        const decimalPart = parts[1] ? parts[1].padEnd(2, "0").substring(0, 2) : "00";
        return `${integerPart},${decimalPart}`;
      } else {
        // Só parte inteira
        const integerPart = this.addThousandsSeparator(cleanValue);
        return `${integerPart},00`;
      }
    },

    addThousandsSeparator(value) {
      if (!value || value === "0") return "0";
      return value.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    },
  },
};
</script>
