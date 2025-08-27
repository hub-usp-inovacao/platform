<template>
  <v-container>
    <v-alert dense type="error" class="text-center" v-if="hasErrors">
      Etapas marcadas com <code>(!)</code> contêm erros de validação
    </v-alert>

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

    <v-stepper v-model="e1" alt-labels>
      <v-stepper-header>
        <template v-for="{ id, title, hasError } in steps">
          <v-stepper-step
              :key="`header_${id}`"
              :step="id"
              :color="hasError ? 'error' : 'success'"
              :complete="isStepCompleted(id)"
              :rules="[() => isStepAccessible(id)]"
          >
            <v-flex justify-center>
              <template v-if="hasError">
                <v-icon color="error">mdi-alert-circle</v-icon>
              </template>
              {{ title }}
            </v-flex>
          </v-stepper-step>

          <v-divider
              v-if="id < numberOfSteps"
              :key="`divider_${id}`"
          ></v-divider>
        </template>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content
            v-for="({ id, component }, index) in steps"
            :key="id"
            :step="id"
        >
          <ul v-if="errorsOfStep(id) && errorsOfStep(id).length > 0">
            <v-alert type="error"><strong>Erros de validação</strong></v-alert>
            <li v-for="errMsg in errorsOfStep(id)" :key="errMsg">
              <strong>{{ errMsg }}</strong>
            </li>
          </ul>
          <component
              :is="component"
              :is-update="update"
              class="component-border mb-12"
              :ref="`step${id}`"
          ></component>

          <v-row class="mr-4" justify="end">
            <v-btn
                class="mr-4"
                color="secondary"
                :disabled="!index"
                @click="previousStep(id)"
            >Voltar</v-btn
            >
            <v-btn color="secondary" @click="nextStep(id)">
              {{ nextStepBtnText(id) }}
            </v-btn>
          </v-row>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>

    <v-dialog v-model="showResult" max-width="500">
      <v-card>
        <v-card-title>{{ resultTitle }}</v-card-title>
        <v-card-text>{{ resultMessage }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="showResult = false">Fechar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import CompanyStep from "@/components/CompanyForms/CompanyStep.vue";
import PartnersStep from "@/components/CompanyForms/PartnersStep.vue";
import IntroStep from "@/components/CompanyForms/IntroStep.vue";
import DNAUSPStep from "@/components/CompanyForms/DNAUSPStep.vue";

export default {
  components: {
    PartnersStep,
    CompanyStep,
    IntroStep,
    DNAUSPStep,
  },
  props: {
    update: {
      type: Boolean,
      default: true,
    },
    errors: {
      type: Object,
      default: () => ({}),
    },
  },
  data: () => ({
    e1: 1,
    showResult: false,
    resultTitle: "",
    resultMessage: "",
    isLoading: false,
    validationErrors: [],
    completedSteps: new Set([1])
  }),
  computed: {
    partnersHasErrors() {
      return this.errors && Object.keys(this.errors).includes("partners");
    },
    companyHasErrors() {
      return [
        "company_data",
        "about_company",
        "investment",
        "revenue",
        "incubation",
        "colaborators",
      ].some((el) => this.errors && Object.keys(this.errors).includes(el));
    },
    DNAHasErrors() {
      return this.errors && Object.keys(this.errors).includes("dna_usp_stamp");
    },
    hasErrors() {
      return (
          this.partnersHasErrors || this.DNAHasErrors || this.companyHasErrors
      );
    },
    steps() {
      return [
        { id: 1, title: "Introdução", component: IntroStep, hasError: false },
        {
          id: 2,
          title: "Sócios",
          component: PartnersStep,
          hasError: this.partnersHasErrors,
        },
        {
          id: 3,
          title: "Empresa",
          component: CompanyStep,
          hasError: this.companyHasErrors,
        },
        {
          id: 4,
          title: "Encerramento",
          component: DNAUSPStep,
          hasError: this.DNAHasErrors,
        },
      ];
    },
    numberOfSteps() {
      return this.steps.length;
    },
  },

  watch: {
    errors() {
      console.log("errors");
      console.log(this.errors);
    },
  },

  methods: {
    errorsOfStep(id) {
      let companyErrors = [];

      if (this.errors.company_data) {
        companyErrors = companyErrors.concat(this.errors.company_data);
      }

      if (this.errors.about_company) {
        companyErrors = companyErrors.concat(this.errors.about_company);
      }

      if (this.errors.investment) {
        companyErrors = companyErrors.concat(this.errors.investment);
      }

      if (this.errors.incubation) {
        companyErrors = companyErrors.concat(this.errors.incubation);
      }

      if (this.errors.colaborators) {
        companyErrors = companyErrors.concat(this.errors.colaborators);
      }

      if (this.errors.revenue) {
        companyErrors = companyErrors.concat(this.errors.revenue);
      }

      switch (id) {
        case 2:
          return this.errors.partners;
        case 3:
          return companyErrors;
        case 4:
          return this.errors.dna_usp_stamp;
      }
    },
    nextStepBtnText(id) {
      const length = this.numberOfSteps;
      const lastId = this.steps[length - 1].id;

      const nextStepIndex = this.steps.findIndex((step) => step.id === id) + 1;
      const nextStepName = this.steps[nextStepIndex]?.title;

      return id < lastId ? `Seguir para passo "${nextStepName}"` : "Finalizar";
    },
    isStepCompleted(number) {
      return this.e1 > number;
    },
    previousStep(id) {
      const firstId = this.steps[0].id;

      if (id > firstId) {
        this.e1 = id - 1;
      }
    },
    nextStep(id) {
      const length = this.numberOfSteps;
      const lastId = this.steps[length - 1].id;

      if (!this.validateCurrentStep(id)) {
        return;
      }

      this.completedSteps.add(id);

      if (id < lastId) {
        this.e1 = id + 1;
        this.completedSteps.add(id + 1);
      } else {
        this.sendData();
      }
    },
    isStepAccessible(stepId) {
      return this.completedSteps.has(stepId);
    },
    validateCurrentStep(stepId) {
      const currentStepComponent = this.getCurrentStepComponent(stepId);

      if (currentStepComponent && typeof currentStepComponent.validateStep === 'function') {
        const validation = currentStepComponent.validateStep();

        if (!validation.isValid) {
          this.validationErrors = validation.errors;
          this.$vuetify.goTo(0);
          return false;
        }
      }

      this.validationErrors = [];
      return true;
    },
    getCurrentStepComponent(stepId) {
      const stepRef = this.$refs[`step${stepId}`];
      return Array.isArray(stepRef) ? stepRef[0] : stepRef;
    },
    clearValidationErrors() {
      this.validationErrors = [];
    },
    async sendData() {
      this.isLoading = true;

      try {
        const result = await this.$store.dispatch('company_forms/updateCompanyForm');

        if (result) {
          this.resultTitle = "Sucesso!";
          this.resultMessage = "Seu formulário foi enviado com sucesso!";

          setTimeout(() => {
            this.$router.push('/');
          }, 3000);
        } else {
          this.resultTitle = "Erro";
          this.resultMessage = "Ocorreu um erro ao validar os dados. Verifique se todos os campos obrigatórios foram preenchidos corretamente.";
        }

      } catch (error) {
        console.error('Erro ao enviar formulário:', error);
        this.resultTitle = "Erro";
        this.resultMessage = `Ocorreu um erro: ${error.message || 'Erro interno'}`;
      } finally {
        this.isLoading = false;
        this.showResult = true;
        this.$emit("finish");
      }
    }
  },
};
</script>

<style scoped>
.component-border {
  border-bottom: 0px solid rgba(0, 0, 0, 0.4);
}
</style>