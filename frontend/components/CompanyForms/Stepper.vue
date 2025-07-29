<template>
  <v-container>
    <v-stepper v-model="e1" alt-labels non-linear>
      <v-stepper-header>
        <template v-for="{ id, title, hasError } in steps">
          <v-stepper-step
              :key="`header_${id}`"
              editable
              :step="id"
              color="success"
          >
            {{ title }}
          </v-stepper-step>

          <v-divider
              v-if="id < numberOfSteps"
              :key="`divider_${id}`"
          ></v-divider>
        </template>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content
            v-for="({ id, title }, index) in steps"
            :key="id"
            :step="id"
        >
          <component
              :is="getComponentForStep(id)"
              @updateData="updateFormData"
              class="component-border mb-12"
          ></component>

          <v-row class="mr-4" justify="end">
            <v-btn
                class="mr-4"
                color="secondary"
                :disabled="!index"
                @click="previousStep(id)"
            >
              Voltar
            </v-btn>
            <v-btn
                color="secondary"
                @click="nextStep(id)"
            >
              {{ nextStepBtnText(id) }}
            </v-btn>
          </v-row>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
  </v-container>
</template>

<script>
import Base from "@/components/CompanyForms/companyStep/Base.vue";
import About from "@/components/CompanyForms/companyStep/About.vue";
import Staff from "@/components/CompanyForms/companyStep/Staff.vue";
import Incubator from "@/components/CompanyForms/companyStep/Incubator.vue";
import Finance from "@/components/CompanyForms/companyStep/Finance.vue";
import Investments from "@/components/CompanyForms/companyStep/Investments.vue";

export default {
  components: {
    Base,
    About,
    Staff,
    Investments,
    Finance,
    Incubator
  },
  data: () => ({
    e1: 1,
    formData: {},
    steps: [
      { id: 1, title: "Dados da empresa", component: "Base" },
      { id: 2, title: "Sobre a empresa", component: "About" },
      { id: 3, title: "Incubação", component: "Incubator" },
      { id: 4, title: "Colaboradores", component: "Staff" },
      { id: 5, title: "Faturamento", component: "Finance" },
      { id: 6, title: "Investimentos", component: "Investments" },
    ],
  }),
  computed: {
    numberOfSteps() {
      return this.steps.length;
    },
  },
  methods: {
    getComponentForStep(id) {
      const step = this.steps.find(s => s.id === id);
      return step ? step.component : null;
    },
    nextStepBtnText(id) {
      const length = this.numberOfSteps;
      const lastId = this.steps[length - 1].id;
      return id < lastId ? "Seguir" : "Finalizar";
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
    updateFormData(stepData) {
      this.formData = { ...this.formData, ...stepData };
      this.$emit('updateCompanyData', this.formData);
    },
    sendData() {
      this.$emit("finish");
    },
  },
};
</script>

<style scoped>
.component-border {
  border-bottom: 0px solid rgba(0, 0, 0, 0.4);
}
</style>