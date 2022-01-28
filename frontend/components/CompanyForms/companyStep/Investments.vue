<template>
  <v-container>
    <p class="body-2 my-5">
      Ultima atualização feita em: {{ formattedLastUpdated }}
    </p>
    <h2 class="text-h6 mt-6 font-weight-regular">
      A empresa recebeu investimento? *
    </h2>
    <em class="body-2">
      Exemplos: investimento próprio, crowdfunding, equity-crowdfunding,
      investimento-anjo, venture capital, BNDES, FINEP, PIPE-FAPESP, outros.
    </em>
    <BooleanInput
      :value="receivedInvestments"
      label=""
      @input="setReceivedInvestments"
    />
    <div v-if="receivedInvestments">
      <h2 class="text-h6 mt-6 font-weight-regular">
        Quais investimentos a empresa recebeu?
      </h2>
      <Dropdown
        :value="preDefinedInvestments"
        :options="investimentos"
        multiple-option
        @input="setPreDefinedInvestments"
      />
      <div class="mt-5 text-h6 font-weight-regular">
        Outros
        <v-divider />
        <v-container>
          <MultipleInputs
            :value="otherInvestments"
            input-label="Investimento"
            @items="outros = $event"
            @input="setOtherInvestments"
          />
        </v-container>
      </div>
      <CurrencyInput
        v-if="hasInvestmentTypeSelected('Investimento próprio')"
        :value="ownValue"
        label="Valor do investimento próprio"
        @input="setOwnValue"
      />
      <CurrencyInput
        v-if="hasInvestmentTypeSelected('Investimento-anjo')"
        :value="angelValue"
        label="Valor do investimento-anjo"
        @input="setAngelValue"
      />
      <CurrencyInput
        v-if="hasInvestmentTypeSelected('Venture capital')"
        :value="ventureCapitalValue"
        label="Valor do Venture Capital"
        @input="setVentureCapitalValue"
      />
      <CurrencyInput
        v-if="hasInvestmentTypeSelected('Private equity')"
        :value="privateEquityValue"
        label="Valor do Private Equity"
        @input="setPrivateEquityValue"
      />
      <CurrencyInput
        v-if="hasInvestmentTypeSelected('PIPE-FAPESP')"
        :value="pipeFapespValue"
        label="Valor do PIPE-FAPESP"
        @input="setPipeFapespValue"
      />
      <CurrencyInput
        v-if="hasOtherInvestmentSelected()"
        :value="otherValue"
        label="Outros investimentos"
        @input="setOtherValue"
      />
    </div>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import BooleanInput from "@/components/CompanyForms/inputs/BooleanInput.vue";
import MultipleInputs from "@/components/CompanyForms/inputs/MultipleInputs.vue";
import CurrencyInput from "@/components/CompanyForms/inputs/CurrencyInput.vue";

export default {
  components: {
    Dropdown,
    BooleanInput,
    MultipleInputs,
    CurrencyInput,
  },
  data: () => ({
    investimentos: [
      "Investimento próprio",
      "Investimento-anjo",
      "Crowdfunding",
      "Venture capital",
      "Private equity",
      "PIPE-FAPESP",
      "BNDES e/ou FINEP",
    ],
  }),
  computed: {
    ...mapGetters({
      investmentsLastUpdatedAt: "company_forms/investmentsLastUpdatedAt",
      receivedInvestments: "company_forms/receivedInvestments",
      investments: "company_forms/investments",
      investmentsValues: "company_forms/investmentsValues",
    }),
    formattedLastUpdated() {
      const date = this.investmentsLastUpdatedAt;
      return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    },
    preDefinedInvestments() {
      return this.investments.filter((inv) =>
        this.investimentos.find((i) => i == inv)
      );
    },
    otherInvestments() {
      return this.investments.filter(
        (inv) => !this.investimentos.find((i) => i == inv)
      );
    },
    ownValue() {
      return this.investmentsValues.own;
    },
    angelValue() {
      return this.investmentsValues.angel;
    },
    ventureCapitalValue() {
      return this.investmentsValues.ventureCapital;
    },
    privateEquityValue() {
      return this.investmentsValues.privateEquity;
    },
    pipeFapespValue() {
      return this.investmentsValues.pipeFapesp;
    },
    otherValue() {
      return this.investmentsValues.other;
    },
  },
  methods: {
    ...mapActions({
      setReceivedInvestments: "company_forms/setReceivedInvestments",
      setInvestments: "company_forms/setInvestments",
      setInvestmentsValues: "company_forms/setInvestmentsValues",
    }),
    hasInvestmentTypeSelected(type) {
      return this.preDefinedInvestments.find((inv) => inv === type);
    },
    hasOtherInvestmentSelected() {
      return (
        this.otherInvestments.length > 0 ||
        this.preDefinedInvestments.find(
          (inv) => inv === "Crowdfunding" || inv === "BNDES e/ou FINEP"
        )
      );
    },
    setPreDefinedInvestments(preDefinedInvestments) {
      this.setInvestments(preDefinedInvestments.concat(this.otherInvestments));
    },
    setOtherInvestments(otherInvestments) {
      this.setInvestments(this.preDefinedInvestments.concat(otherInvestments));
    },
    setOwnValue(newValue) {
      this.setInvestmentsValues({ ...this.investmentsValues, own: newValue });
    },
    setAngelValue(newValue) {
      this.setInvestmentsValues({ ...this.investmentsValues, angel: newValue });
    },
    setVentureCapitalValue(newValue) {
      this.setInvestmentsValues({
        ...this.investmentsValues,
        ventureCapital: newValue,
      });
    },
    setPrivateEquityValue(newValue) {
      this.setInvestmentsValues({
        ...this.investmentsValues,
        privateEquity: newValue,
      });
    },
    setPipeFapespValue(newValue) {
      this.setInvestmentsValues({
        ...this.investmentsValues,
        pipeFapesp: newValue,
      });
    },
    setOtherValue(newValue) {
      this.setInvestmentsValues({ ...this.investmentsValues, other: newValue });
    },
  },
};
</script>
