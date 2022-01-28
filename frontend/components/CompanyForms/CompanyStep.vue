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
  computed: {
    backButtonName() {
      if (this.disableBackButton) return "Voltar";
      const tabName = this.items[this.tab - 1].tab;
      return `Voltar para "${tabName}"`;
    },
    nextButtonName() {
      if (this.disableNextButton) return "Próximo";
      const tabName = this.items[this.tab + 1].tab;
      return `Ir para "${tabName}"`;
    },
    disableBackButton() {
      return this.tab === 0;
    },
    disableNextButton() {
      return this.tab === this.items.length - 1;
    },
  },
  methods: {
    nextTab() {
      this.tab = this.tab + 1;
    },
    previousTab() {
      this.tab = this.tab - 1;
    },
  },
};
</script>

<style>
.tab-container {
  padding: 0;
}
</style>
