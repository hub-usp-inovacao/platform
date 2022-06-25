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
      required: true,
    },
    labelTwo: {
      type: String,
      required: true,
    },
  },
};
</script>