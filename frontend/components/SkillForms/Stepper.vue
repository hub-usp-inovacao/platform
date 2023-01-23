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
  </v-container>
</template>

<script>
import PersonalStep from "./PersonalStep.vue";
import BondStep from "./BondStep.vue";
import ResourcesStep from "./ResourcesStep.vue";
import ConfirmationStep from "./ConfirmationStep.vue";

export default {
  components: {
    PersonalStep,
    BondStep,
    ResourcesStep,
    ConfirmationStep,
  },
  props: {
    update: {
      type: Boolean,
      default: true,
    },
  },
  data: () => ({
    e1: 1,
    steps: [
      { id: 1, title: "Pessoal", component: PersonalStep },
      { id: 2, title: "Vínculos", component: BondStep },
      { id: 3, title: "Recursos", component: ResourcesStep },
      { id: 4, title: "Confirmação", component: ConfirmationStep },
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
