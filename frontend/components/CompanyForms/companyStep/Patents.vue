<template>
  <div>
    <MultipleInputs
      :value = "patents"
      @input = "setPatents"
      component="PatentNameAndCode"
      inputLabel="patente"
    />
  </div>
</template>

<script>
import PairOfShortTextInput from "@/components/CompanyForms/inputs/PairOfShortTextInput.vue";
import MultipleInputs from "../inputs/MultipleInputs.vue";
import {mapGetters, mapActions} from "vuex"

export default {
  components: {
    PairOfShortTextInput,
    MultipleInputs,
  },
  computed: {
    ...mapGetters({
      patents: "company_forms/patents"
    })
  },
  methods: {
    ...mapActions({
      setPatents: "company_forms/setPatents"
    })
  }
};
</script>