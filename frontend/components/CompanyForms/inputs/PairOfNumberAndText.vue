<template>
  <v-row>
    <v-col>
      <MaskInput
        :placeholder="placeholderOne"
        :value="one"
        mask="###-#"
        :label="labelOne"
        v-model="one"
        @input="$emit('input', inputEvent)"
      />
    </v-col>
    <v-col cols="10">
      <ShortTextInputAutocomplete
        :placeholder="placeholderTwo"
        :value="two"
        :label="labelTwo"
        v-model="two"
        :items="items"
        @input="$emit('input', inputEvent)"
        @change="$emit('input', inputEvent)"
      />
    </v-col>
  </v-row>
</template>

<script>
import ShortTextInputAutocomplete from "@/components/CompanyForms/inputs/ShortTextInputAutocomplete.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";

export default {
  name: "PairOfNumberAndText",
  components: {
    MaskInput,
    ShortTextInputAutocomplete
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
    },
    items: {
        type: Array,
        required: true,
    },
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
  methods: {
    set(field, value) {
      this[field] = value;
      this.$emit("input", this.inputEvent);
    },
  },
  watch: {
    one() {
      this.$emit("changeOne", this.one);
    },
    two() {
      this.$emit("changeTwo", this.two);
    },
  },
};
</script>
