<template>
  <v-container>
    <div class="mt-5 text-h6 font-weight-regular">
      Qual foi o faturamento da empresa em {{ lastYear() }}?
      <CurrencyInput
        label="Faturamento"
        :value="financeValue"
        @input="setFinanceValue"
      />
      <legend class="body-2">
        Se for R$0,00 digite 0. Este dado NÃO será publicado.
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
      financeValue: "company_forms/financeValue",
    }),
  },
  methods: {
    ...mapActions({
      setFinanceValue: "company_forms/setFinanceValue",
    }),
    lastYear() {
      const date = new Date();
      return date.getFullYear() - 1;
    },
  },
};
</script>
