<template>
  <div>
    <div class="background">
      <Panel :title="title" :description="description" no-search />
    </div>
    <Stepper :update="false"/>
  </div>
</template>

<script>
import Stepper from "@/components/CompanyForms/Stepper.vue";
import Panel from "@/components/first_level/Panel.vue";

export default {
  components: {
    Stepper,
    Panel,
  },

  data: () => ({
    title: "Cadastro de Empresas",
    description:
      "Representantes das Empresas podem solicitar, nesta página, o cadastro de seus empresas no Hub.",
  }),
};
</script>
