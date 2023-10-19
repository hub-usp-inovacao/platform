<template>
  <v-container>
    <v-alert
      border="top"
      colored-border
      type="info"
      elevation="2"
      color="secondary"
    >
      Este dado <b>não</b> será publicado
    </v-alert>
    <div class="mt-5 text-h6 font-weight-regular">
      Qual foi o faturamento da empresa em {{ lastDateYear() }}?
      <CurrencyInput
        label="Faturamento"
        :value="lastYear"
        @input="setLastYear"
      />
      <legend class="body-2">
        Se for R$0,00 digite 0.
      </legend>
    </div>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import CurrencyInput from "@/components/CompanyForms/inputs/CurrencyInput.vue";

export default {
  components: {
    CurrencyInput,
  },
  computed: {
    ...mapGetters({
      lastYear: "company_forms/lastYear",
    }),
  },
  methods: {
    ...mapActions({
      setLastYear: "company_forms/setLastYear",
    }),
    lastDateYear() {
      const date = new Date();
      return date.getFullYear() - 1;
    },
  },
};
</script>
