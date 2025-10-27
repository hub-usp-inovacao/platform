<template>
  <v-container class="tab-container">
    <v-tabs v-model="tab" fixed-tabs>
      <v-tab v-for="item in items" :key="item.tab">
        {{ item.tab }}
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="item in items" :key="item.tab">
        <component :is="item.content"></component>
      </v-tab-item>
    </v-tabs-items>
    <div class="mr-4 d-flex justify-end">
      <v-btn class="mr-4" color="secondary" @click="previousTab">
        Voltar
      </v-btn>
      <v-btn color="secondary" @click="nextTab"> Próximo </v-btn>
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

export default {
  components: {
    Base,
    About,
    Staff,
    Investments,
    Finance,
  },
  data: () => ({
    tab: 0,
    items: [
      { tab: "Dados da empresa", content: Base },
      { tab: "Sobre a empresa", content: About },
      { tab: "Incubação", content: Incubator },
      { tab: "Colaboradores", content: Staff },
      { tab: "Faturamento", content: Finance },
      { tab: "Investimentos", content: Investments },
    ],
  }),
  methods: {
    nextTab() {
      if (this.tab < this.items.length - 1) {
        this.tab = this.tab + 1;
      } else {
        this.$emit("nextStep");
      }
    },
    previousTab() {
      if (this.tab > 0) {
        this.tab = this.tab - 1;
      } else {
        this.$emit("previousStep");
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
