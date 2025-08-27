<template>
  <v-container>
    <v-alert
      border="top"
      colored-border
      color="secondary"
      type="info"
      elevation="2"
    >
      Este dado <b>não</b> será publicado
    </v-alert>

    <h2 class="text-h6 mt-6 font-weight-regular">
      A empresa recebeu investimento? *
    </h2>
    <em class="body-2">
      Exemplos: investimento próprio, crowdfunding, equity-crowdfunding,
      investimento-anjo, venture capital, BNDES, FINEP, PIPE-FAPESP, outros.
    </em>
    
    <Dropdown
      :value="received"
      :options="['Sim', 'Não']"
      label=""
      @input="setReceived"
    />  

    <div v-if="received == 'Sim'">
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
        Outros investimentos - Valores
        <v-divider />
        <v-container>
          <div
            v-for="(value, index) in localOtherInvestmentValues"
            :key="`investment-value-${index}`"
            class="d-flex align-center mb-2"
          >
            <CurrencyInput
              :value="value"
              :label="`Valor do investimento ${index + 1}`"
              @input="updateOtherInvestmentValue(index, $event)"
              class="mr-2"
            />
            <v-btn
              icon
              small
              color="error"
              @click="removeOtherInvestment(index)"
              :disabled="localOtherInvestmentValues.length <= 1"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <v-btn
            small
            color="primary"
            @click="addOtherInvestment"
            :disabled="localOtherInvestmentValues.length >= 10"
          >
            <v-icon left>mdi-plus</v-icon>
            Adicionar investimento
          </v-btn>
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
import CurrencyInput from "@/components/CompanyForms/inputs/CurrencyInput.vue";
import TextInputFormatted from "@/components/CompanyForms/inputs/TextInputFormatted.vue";

export default {
  components: {
    Dropdown,
    CurrencyInput,
    TextInputFormatted,
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
    localOtherInvestmentValues: [''],
  }),
  computed: {
    ...mapGetters({
      received: "company_forms/received",
      investments: "company_forms/investments",
      investmentsValues: "company_forms/investmentsValues",
    }),

    preDefinedInvestments() {
      return (this.investments || []).filter((inv) =>
        this.investimentos.find((i) => i == inv)
      );
    },
    otherInvestments() {
      return (this.investments || []).filter(
        (inv) => !this.investimentos.find((i) => i == inv)
      );
    },
    ownValue() {
      return (this.investmentsValues || {}).own;
    },
    angelValue() {
      return (this.investmentsValues || {}).angel;
    },
    ventureCapitalValue() {
      return (this.investmentsValues || {}).venture;
    },
    privateEquityValue() {
      return (this.investmentsValues || {}).equity;
    },
    pipeFapespValue() {
      return (this.investmentsValues || {}).pipe;
    },
    otherValue() {
      return (this.investmentsValues || {}).others;
    },
  },
  methods: {
    ...mapActions({
      setReceived: "company_forms/setReceived",
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
      const currentOtherInvestments = this.otherInvestments || [];
      this.setInvestments(preDefinedInvestments.concat(currentOtherInvestments));
    },
    setOtherInvestments(otherInvestments) {
      const currentPreDefinedInvestments = this.preDefinedInvestments || [];
      this.setInvestments(currentPreDefinedInvestments.concat(otherInvestments));
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
        venture: newValue,
      });
    },
    setPrivateEquityValue(newValue) {
      this.setInvestmentsValues({
        ...this.investmentsValues,
        equity: newValue,
      });
    },
    setPipeFapespValue(newValue) {
      this.setInvestmentsValues({
        ...this.investmentsValues,
        pipe: newValue,
      });
    },
    setOtherValue(newValue) {
      this.setInvestmentsValues({ ...this.investmentsValues, others: newValue });
    },
    validateStep() {
      const errors = [];

      if (!this.received) {
        errors.push('É obrigatório informar se a empresa recebeu investimento');
      }

      if (this.received === 'Sim') {
        const hasAnyInvestment = this.preDefinedInvestments.length > 0 ||
                               this.localOtherInvestmentValues.some(value => value && value.trim() !== '' && value !== '0');

        if (!hasAnyInvestment) {
          errors.push('É necessário selecionar pelo menos um tipo de investimento ou informar valores de outros investimentos');
        }

        if (this.hasInvestmentTypeSelected('Investimento próprio') && (!this.ownValue || this.ownValue === '0')) {
          errors.push('É necessário informar o valor do investimento próprio');
        }
        if (this.hasInvestmentTypeSelected('Investimento-anjo') && (!this.angelValue || this.angelValue === '0')) {
          errors.push('É necessário informar o valor do investimento-anjo');
        }
        if (this.hasInvestmentTypeSelected('Venture capital') && (!this.ventureCapitalValue || this.ventureCapitalValue === '0')) {
          errors.push('É necessário informar o valor do Venture Capital');
        }
        if (this.hasInvestmentTypeSelected('Private equity') && (!this.privateEquityValue || this.privateEquityValue === '0')) {
          errors.push('É necessário informar o valor do Private Equity');
        }
        if (this.hasInvestmentTypeSelected('PIPE-FAPESP') && (!this.pipeFapespValue || this.pipeFapespValue === '0')) {
          errors.push('É necessário informar o valor do PIPE-FAPESP');
        }
      }

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },
    updateOtherInvestmentValue(index, value) {
      const sanitizedValue = value ? value.toString().trim() : '';
      this.localOtherInvestmentValues[index] = sanitizedValue;
      this.saveOtherInvestments();
    },
    addOtherInvestment() {
      if (this.localOtherInvestmentValues.length < 10) {
        this.localOtherInvestmentValues.push('');
      }
    },
    removeOtherInvestment(index) {
      if (this.localOtherInvestmentValues.length > 1) {
        this.localOtherInvestmentValues.splice(index, 1);
        this.saveOtherInvestments();
      }
    },
    saveOtherInvestments() {
      const filteredInvestments = this.localOtherInvestmentValues
        .filter(value => value && value.toString().trim() !== '' && value !== '0')
        .map((value, index) => `Outros investimentos ${index + 1}`);
      this.setOtherInvestments(filteredInvestments);
    },
  },
};
</script>
