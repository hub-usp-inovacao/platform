<template>
  <v-container>
    <p class="body-2 my-5">
      Ultima atualização feita em: {{ formattedLastUpdated }}
    </p>
    <div class="mt-5 text-h6 font-weight-regular">
      Qual foi o faturamento da empresa em {{lastYear()}}?
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
      revenuesLastUpdatedAt: "company_forms/revenuesLastUpdatedAt",
    }),
    formattedLastUpdated() {
      const date = this.revenuesLastUpdatedAt;
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    },
  },
  methods: {
    ...mapActions({
      setFinanceValue: "company_forms/setFinanceValue",
    }),
    lastYear() {
      const date = new Date
      return date.getFullYear() - 1
    }
  },
};
</script>
