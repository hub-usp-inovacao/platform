<template>
  <v-container class="tab-container">
    <v-tabs v-model="tab" fixed-tabs>
      <v-tab
        v-for="(item, index) in items"
        :key="item.tab"
        :disabled="!isTabAccessible(index)"
        @click="handleTabClick(index)"
      >
        {{ item.tab }}
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="item in items" :key="item.tab">
        <component :is="item.content" :ref="item.ref"></component>
      </v-tab-item>
    </v-tabs-items>

    <v-alert
      v-if="validationErrors.length > 0"
      type="error"
      dismissible
      class="ma-4"
      @input="clearValidationErrors"
    >
      <strong>Corrija os seguintes erros antes de continuar:</strong>
      <ul class="mt-2">
        <li v-for="error in validationErrors" :key="error">{{ error }}</li>
      </ul>
    </v-alert>

    <div class="ma-4 d-flex justify-space-between">
      <v-btn
        class="mr-4"
        color="primary"
        :disabled="disableBackButton"
        @click="previousTab"
      >
        {{ backButtonName }}
      </v-btn>
      <v-btn color="primary" :disabled="disableNextButton" @click="nextTab">
        {{ nextButtonName }}
      </v-btn>
    </div>
  </v-container>
</template>

<script>
import Base from "@/components/CompanyForms/companyStep/Base.vue";
import About from "@/components/CompanyForms/companyStep/About.vue";
import Staff from "@/components/CompanyForms/companyStep/Staff.vue";
import Incubator from "@/components/CompanyForms/companyStep/Incubator.vue";
import Finance from "@/components/CompanyForms/companyStep/Finance.vue";
import Investments from "@/components/CompanyForms/companyStep/Investments.vue";
import AdditionalInfo from "@/components/CompanyForms/companyStep/AdditionalInfo.vue";

export default {
  components: {
    Base,
    About,
    Staff,
    Investments,
    Finance,
    AdditionalInfo,
  },
  data: () => ({
    tab: 0,
    validationErrors: [],
    completedTabs: new Set([0]),
    validatedTabs: new Set(), // Remover a inicialização com [0]
    items: [
      { tab: "Dados da empresa", content: Base, ref: "baseStep" },
      { tab: "Sobre a empresa", content: About, ref: "aboutStep" },
      { tab: "Incubação", content: Incubator, ref: "incubatorStep" },
      { tab: "Colaboradores", content: Staff, ref: "staffStep" },
      { tab: "Receita", content: Finance, ref: "financeStep" },
      { tab: "Investimentos", content: Investments, ref: "investmentsStep" },
      { tab: "Informações Adicionais", content: AdditionalInfo, ref: "additionalInfoStep" },
    ],
  }),
  computed: {
    backButtonName() {
      if (this.disableBackButton) return "Voltar";
      const tabName = this.items[this.tab - 1].tab;
      return `Voltar para aba "${tabName}"`;
    },
    nextButtonName() {
      if (this.disableNextButton) return "Próximo";
      const tabName = this.items[this.tab + 1].tab;
      return `Ir para aba "${tabName}"`;
    },
    disableBackButton() {
      return this.tab === 0;
    },
    disableNextButton() {
      return this.tab === this.items.length - 1;
    },
    currentStepRef() {
      return this.items[this.tab].ref;
    },
  },
  methods: {
    validateCurrentStep() {
      this.clearValidationErrors();

      const currentStepComponent = this.$refs[this.currentStepRef];

      if (currentStepComponent && currentStepComponent[0] && typeof currentStepComponent[0].validateStep === 'function') {
        const validation = currentStepComponent[0].validateStep();

        if (!validation.isValid) {
          this.validationErrors = validation.errors;
          return false;
        }
      }

      return true;
    },

    clearValidationErrors() {
      this.validationErrors = [];
    },

    validateSpecificStep(stepIndex) {
      const stepRef = this.items[stepIndex].ref;
      const stepComponent = this.$refs[stepRef];

      if (stepComponent && stepComponent[0] && typeof stepComponent[0].validateStep === 'function') {
        const validation = stepComponent[0].validateStep();
        return validation.isValid;
      }

      return true;
    },

    nextTab() {
      if (this.validateCurrentStep()) {
        this.completedTabs.add(this.tab);
        this.validatedTabs.add(this.tab); // Marca como validada
        this.tab = this.tab + 1;
        this.completedTabs.add(this.tab);
      } else {
        this.$vuetify.goTo(0);
      }
    },

    previousTab() {
      this.clearValidationErrors();
      this.tab = this.tab - 1;
    },

    validateStep() {
      const errors = [];

      if (this.tab !== this.items.length - 1) {
        errors.push('É necessário completar todas as subetapas da empresa antes de prosseguir');
      }

      for (let i = 0; i < this.items.length; i++) {
        const stepRef = this.items[i].ref;
        const stepComponent = this.$refs[stepRef];

        if (stepComponent && stepComponent[0] && typeof stepComponent[0].validateStep === 'function') {
          const validation = stepComponent[0].validateStep();
          if (!validation.isValid) {
            errors.push(...validation.errors.map(error => `${this.items[i].tab}: ${error}`));
          }
        }
      }

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },
    isTabAccessible(index) {
      if (index === 0) return true;

      for (let i = 0; i < index; i++) {
        if (!this.validatedTabs.has(i)) {
          return false;
        }
      }

      return true;
    },
    handleTabClick(index) {
      if (index === this.tab) {
        this.clearValidationErrors();
        return;
      }

      if (index < this.tab) {
        this.clearValidationErrors();
        this.tab = index;
        return;
      }

      if (!this.validateCurrentStep()) {
        this.$vuetify.goTo(0);
        return;
      }

      this.validatedTabs.add(this.tab);

      if (index === this.tab + 1) {
        this.clearValidationErrors();
        this.tab = index;
        this.completedTabs.add(index);
      } else {
        const nextStepName = this.items[this.tab + 1].tab;
        this.validationErrors = [`Você deve prosseguir para a próxima etapa: "${nextStepName}"`];
        this.$vuetify.goTo(0);
      }
    },
  },
};
</script>

<style>
.tab-container {
  padding: 0;
}
</style>
