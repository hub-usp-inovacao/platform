<template>
  <v-container>
    <v-alert dense type="error" class="text-center" v-if="hasErrors">
      Etapas marcadas com <code>(!)</code> contém erros de validação
    </v-alert>
    <v-stepper v-model="e1" alt-labels non-linear>
      <v-stepper-header>
        <template v-for="{ id, title, hasError } in steps">
          <v-stepper-step
              :key="`header_${id}`"
              editable
              :step="id"
              :color="hasError ? 'error' : 'success'"
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
    isLoading: false
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

      if (id < lastId) {
        this.e1 = id + 1;
      } else {
        this.sendData();
      }
    },
    async sendData() {
      this.isLoading = true;

      try {
        // Acessando dados do store corretamente
        const store = this.$store.state.company_forms;

        const formData = {
          name: store.name,
          corporateName: store.corporateName,
          cnpj: store.cnpj,
          year: store.year,
          cnae: store.cnae,
          address: {
            venue: store.address.venue,
            neighborhood: store.address.neighborhood,
            city: store.address.city,
            state: store.address.state,
            cep: store.address.cep
          },
          phones: store.phones,
          emails: store.emails,
          description: store.description,
          services: store.services,
          technologies: store.technologies,
          logo: store.logo,
          url: store.site,
          incubated: store.incubated,
          ecosystems: store.ecosystems ? [store.ecosystems] : [],
          companySize: store.size ? [store.size] : [],
          partners: store.partners.map(p => ({
            name: p.name,
            nusp: p.nusp || null,
            bond: p.bond || null,
            unity: p.unity || null,
            email: p.email || null,
            phone: p.phone || null,
            position: p.role || null
          })),
          odss: store.odss || [],
          linkedin: store.linkedin || null,
          instagram: store.instagram || null,
          facebook: store.facebook || null,
          dnaUspInfo: {
            wantsSeal: store.wantsDna,
            contactEmail: store.wantsDna ? store.dnaContactEmail : null,
            contactName: store.wantsDna ? store.dnaContactName : null,
            contactAgreements: store.permissions || [],
            category: store.category || null,
            confirmationStatus: "Pendente"
          },
          employeeInfo: {
            cltEmployees: parseInt(store.numberOfCLTEmployees) || 0,
            pjCollaborators: parseInt(store.numberOfPJColaborators) || 0,
            internsScholars: parseInt(store.numberOfInterns) || 0,
            totalEmployees: (parseInt(store.numberOfCLTEmployees) || 0) + (parseInt(store.numberOfPJColaborators) || 0) + (parseInt(store.numberOfInterns) || 0)
          },
          investmentInfo: {
            hasInvestment: store.received === "Sim",
            investmentTypes: store.investments || [],
            ownInvestmentAmount: store.investmentsValues?.own || null,
            angelInvestmentAmount: store.investmentsValues?.angel || null,
            ventureCapitalAmount: store.investmentsValues?.venture || null,
            privateEquityAmount: store.investmentsValues?.equity || null,
            pipeAmount: store.investmentsValues?.pipe || null,
            otherInvestmentsAmount: store.investmentsValues?.others || null,
            revenue: store.lastYear || null,
            companySize: store.size || null
          },
          companyType: store.companyType || null,
          operationalStatus: store.registry_status || "Ativa",
          totalPartners: store.partners?.length || 0,
          hasUspPartners: store.partners?.some(p => p.nusp) || false,
          wantsToAddMorePartners: false,
          incubatorName: store.ecosystems || null,
          isUnicorn: false,
          agreements: store.permissions || []
        };


        const response = await fetch("http://localhost:8080/company/register", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (result.status === "enviado") {
          this.resultTitle = "Sucesso!";
          this.resultMessage = "Seu formulário foi enviado com sucesso!";
        } else {
          this.resultTitle = "Erro";
          this.resultMessage = `Ocorreu um erro: ${result.mensagem}`;
        }

      } catch (error) {
        this.resultTitle = "Erro";
        this.resultMessage = `Ocorreu um erro: ${error.message}`;
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