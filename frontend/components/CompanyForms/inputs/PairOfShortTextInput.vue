<template>
  <v-row>
    <v-col>
      <ShortTextInput :label="labelOne" v-model="one" />
    </v-col>
    <v-col cols="4">
      <ShortTextInput :label="labelTwo" v-model="two" />
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
  data: () => ({
    one: "",
    two: "",
  }),
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