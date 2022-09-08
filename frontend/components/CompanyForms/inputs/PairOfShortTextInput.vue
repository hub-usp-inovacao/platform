<template>
  <v-row>
    <v-col>
      <ShortTextInput v-model="one" :label="labelOne" />
    </v-col>
    <v-col cols="4">
      <ShortTextInput v-model="two" :label="labelTwo" />
    </v-col>
  </v-row>
</template>

<script>
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";

export default {
  name: "PairOfShortTextInput",
  components: {
    ShortTextInput,
  },
  props: {
    labelOne: {
      type: String,
      default: () => "label one",
    },
    labelTwo: {
      type: String,
      default: () => "label two",
    },
  },
  data: () => ({
    one: "",
    two: "",
  }),
  computed: {
    inputEvent() {
      const event = {};
      event[this.labelOne] = this.one;
      event[this.labelTwo] = this.two;

      return event;
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
