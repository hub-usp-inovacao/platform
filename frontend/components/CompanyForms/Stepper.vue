<template>
  <v-container>
    <v-stepper v-model="e1" alt-labels non-linear>
      <v-stepper-header>
        <template v-for="{ id, title } in steps">
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
          v-for="({ id, component }, index) in steps"
          :key="id"
          :step="id"
        >
          <component :is="component" :isUpdate=update class="component-border mb-12"></component>

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
   }
  },
  data: () => ({
    e1: 1,
    steps: [
      { id: 1, title: "Introdução", component: IntroStep },
      { id: 2, title: "Sócios", component: PartnersStep },
      { id: 3, title: "Empresa", component: CompanyStep },
      { id: 4, title: "Encerramento", component: DNAUSPStep },
    ],
  }),
  computed: {
    numberOfSteps() {
      return this.steps.length;
    },
  },
  methods: {
    nextStepBtnText(id) {
      const length = this.numberOfSteps;
      const lastId = this.steps[length - 1].id;
      return id < lastId ? "Seguir" : "Finalizar";
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
