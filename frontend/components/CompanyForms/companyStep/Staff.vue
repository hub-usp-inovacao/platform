<template>
  <v-container>
    <p class="body-2 my-5">
      Ultima atualização feita em: {{ formattedLastUpdated }}
    </p>
    <v-form>
      <NumberInput
        :value="numberOfCTLEmployees"
        label="Qual o número de funcionários contratados como CLT?"
        @input="setNumberOfCTLEmployees"
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
      collaboratorsLastUpdatedAt: "company_forms/collaboratorsLastUpdatedAt",
      numberOfCTLEmployees: "company_forms/numberOfCTLEmployees",
      numberOfPJColaborators: "company_forms/numberOfPJColaborators",
      numberOfInterns: "company_forms/numberOfInterns",
    }),
    formattedLastUpdated() {
      const date = this.collaboratorsLastUpdatedAt;
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    },
  },
  methods: {
    ...mapActions({
      setNumberOfCTLEmployees: "company_forms/setNumberOfCTLEmployees",
      setNumberOfPJColaborators: "company_forms/setNumberOfPJColaborators",
      setNumberOfInterns: "company_forms/setNumberOfInterns",
    }),
  },
};
</script>
