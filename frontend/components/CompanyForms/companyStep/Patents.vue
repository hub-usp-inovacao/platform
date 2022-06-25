<template>
  <div>
    <MultipleInputs
      v-model="values"
      component="PairOfShortTextInput"
      inputLabel="patente"
    />
  </div>
</template>

<script>
import PairOfShortTextInput from "@/components/CompanyForms/inputs/PairOfShortTextInput.vue";
import MultipleInputs from "../inputs/MultipleInputs.vue";

export default {
  components: {
    PairOfShortTextInput,
    MultipleInputs,
  },
  data: () => ({
    values: [],
  }),
};
</script>