<template>
  <v-container>
    <v-alert
      type="info"
      color="secondary"
      elevation="2"
      colored-border
      border="top"
    >
      Este dado <b>não</b> será publicado
    </v-alert>
    <v-form>
      <NumberInput
        :value="numberOfCLTEmployees"
        label="Qual o número de funcionários contratados como CLT?"
        @input="setNumberOfCLTEmployees"
      />
      <NumberInput
        :value="numberOfPJColaborators"
        label="Qual o número de colaboradores contratados como pessoa jurídica (PJ)?"
        @input="setNumberOfPJColaborators"
      />
      <NumberInput
        :value="numberOfInterns"
        label="Qual o número de estagiários/bolsistas contratados?"
        @input="setNumberOfInterns"
      />
    </v-form>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import NumberInput from "@/components/CompanyForms/inputs/NumberInput.vue";

export default {
  components: {
    NumberInput,
  },
  computed: {
    ...mapGetters({
      numberOfCLTEmployees: "company_forms/numberOfCLTEmployees",
      numberOfPJColaborators: "company_forms/numberOfPJColaborators",
      numberOfInterns: "company_forms/numberOfInterns",
    }),
  },
  methods: {
    ...mapActions({
      setNumberOfCLTEmployees: "company_forms/setNumberOfCLTEmployees",
      setNumberOfPJColaborators: "company_forms/setNumberOfPJColaborators",
      setNumberOfInterns: "company_forms/setNumberOfInterns",
    }),
  },
};
</script>
