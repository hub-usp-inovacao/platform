<template>
  <v-row>
    <v-col>
      <MaskInput
        :placeholder="placeholderOne"
        :value="one"
        mask="###-#"
        :label="labelOne"
        v-model="one"
      />
    </v-col>
    <v-col cols="10">
      <ShortTextInput
        :placeholder="placeholderTwo"
        :value="two"
        :label="labelTwo"
        v-model="two"
      />
    </v-col>
  </v-row>
</template>

<script>
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";

export default {
  name: "PairOfNumberAndText",
  components: {
    ShortTextInput,
    MaskInput
  },
  data: () => ({
    one: "",
    two: "",
  }),
  props: {
    placeholderOne: {
      type: String,
      default: () => "",
    },
    placeholderTwo: {
      type: String,
      default: () => "",
    },
    labelOne: {
      type: String,
      default: () => "label one",
    },
    labelTwo: {
      type: String,
      default: () => "label two",
    },
    separator: {
      type: String,
      default: () => " ",
    },
    value: {
      type: String,
    }
  },
  computed: {
    inputEvent() {
      return `${this.one}${this.separator}${this.two}`;
    },
    splitValues() {
      if (this.value) {
        const split = this.value.split(this.separator);
        this.one = split[0];
        this.two = split[1];
      }
    },
  },
  watch: {
    one() {
      this.$emit("input", this.inputEvent);
    },
    two() {
      this.$emit("input", this.inputEvent);
    },
  },
};
</script>
