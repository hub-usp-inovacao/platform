<template>
  <v-row>
    <v-col>
      <MaskInput
        :placeholder="firstPlaceholder"
        :value="first"
        mask="###-#"
        :label="firstLabel"
        v-model="first"
        @input="$emit('input', inputEvent)"
      />
    </v-col>
    <v-col cols="10">
      <ShortTextInput
        :placeholder="secondPlaceholder"
        :value="second"
        :label="secondLabel"
        v-model="second"
        @input="$emit('input', inputEvent)"
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
    first: "",
    second: "",
  }),
  props: {
    firstPlaceholder: {
      type: String,
      default: () => "",
    },
    secondPlaceholder: {
      type: String,
      default: () => "",
    },
    firstLabel: {
      type: String,
      default: () => "first label",
    },
    secondLabel: {
      type: String,
      default: () => "second label",
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
      return `${this.first}${this.separator}${this.second}`;
    },
    splitValues() {
      if (this.value) {
        const split = this.value.split(this.separator);
        this.first = split[0];
        this.second = split[1];
      }
    },
  },
  methods: {
    set(field, value) {
      this[field] = value;
    },
  },
  watch: {
    first() {
      this.$emit("changeFirstField", this.first);
    },
    second() {
      this.$emit("changeSecondField", this.second);
    },
  },
};
</script>
