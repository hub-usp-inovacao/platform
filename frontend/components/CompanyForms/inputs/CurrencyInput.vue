<template>
  <LastUpdated :label="label" :last-updated="lastUpdated">
    <v-text-field
      :label="label"
      :value="value"
      :hint="hint"
      persistent-hint
      @keydown="handleKeyDown"
      @click="moveCursorToTheEnd"
    />
  </LastUpdated>
</template>

<script>
import LastUpdated from "@/components/CompanyForms/inputs/LastUpdated.vue";

export default {
  components: {
    LastUpdated,
  },
  props: {
    value: {
      type: String,
      required: false,
      default: "R$ 0,00",
    },
    label: {
      type: String,
      required: true,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
    lastUpdated: {
      type: String,
      default: undefined,
    },
  },
  methods: {
    handleKeyDown(e) {
      const TAB = 9;
      const BACKSPACE = 8;
      const NUMBERS =
        (e.keyCode >= 48 && e.keyCode <= 57) ||
        (e.keyCode >= 96 && e.keyCode <= 105);

      if (e.keyCode !== TAB) {
        e.preventDefault();
        this.moveCursorToTheEnd(e);
      }

      if (e.keyCode === BACKSPACE) {
        this.removeLastDigit();
      }

      if (NUMBERS) {
        this.updateCurrencyValue(e.key);
      }
    },
    formatCurrency(digits) {
      const length = digits.length;
      const digitsBeforeComma = digits.slice(0, length - 2);
      const lastTwoDigits = digits.slice(length - 2);
      const formattedValue = digitsBeforeComma
        .reverse()
        .reduce(
          (acc, digit, index) =>
            index % 3 === 0 && index !== 0
              ? acc.concat([".", digit])
              : acc.concat(digit),
          []
        )
        .reverse()
        .concat(",")
        .concat(lastTwoDigits)
        .join("");

      return `R$ ${formattedValue}`;
    },
    updateCurrencyValue(newNumber) {
      const allDigits = this.value
        .split("")
        .filter((val) => "R$ .,".split("").every((elem) => elem !== val))
        .concat(newNumber);

      if (allDigits[0] === "0") {
        allDigits.shift();
      }

      this.$emit("input", this.formatCurrency(allDigits));
    },
    removeLastDigit() {
      const allDigits = this.value
        .split("")
        .filter((val) => "R$ .,".split("").every((elem) => elem !== val));

      if (allDigits.length <= 3) {
        allDigits.unshift("0");
      }

      allDigits.pop();
      this.$emit("input", this.formatCurrency(allDigits));
    },
    moveCursorToTheEnd(event) {
      const input = event.target;
      const length = input.value.length;
      input.setSelectionRange(length, length);
    },
  },
};
</script>
