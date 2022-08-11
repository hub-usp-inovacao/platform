<template>
  <div>
    <div class="background">
      <Panel
        v-model="search.term"
        title="Patentes"
        description="Os pesquisadores e estudantes da USP desenvolvem invenções que são protegidas por propriedade industrial - PI (patentes e registros de software). Estas PI estão disponíveis para organizações públicas e privadas que tenham interesse em licenciamento para aplicação e comercialização. Nesta plataforma, as PI estão organizadas por áreas tecnológicas e status (concedida, em análise e domínio público)."
        @search="search.term = $event"
        @clear="search.term = ''"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="tabs"
      :groups="groups"
      :colors="{ base: '#5B2C7D', active: '#9247C9' }"
      @select="filters = $event"
    />

    <DisplayData :reverse="true" :items="patents" group-name="Patentes">
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <p>
          <span>Classificação:</span>
          {{ item.classification.primary.cip }}
        </p>

        <p>{{ item.classification.primary.subareas }}</p>

        <HorizontalList
          title="Países com Proteção"
          :items="item.countries_with_protection"
        />

        <HorizontalList
          v-if="item.ipcs.length > 0 && item.ipcs[0] !== ''"
          title="IPCs"
          :items="item.ipcs"
        />

        <BulletList title="Titulares" :items="item.owners" />
        <BulletList title="Inventores" :items="item.inventors" />
      </template>
      <template #detailsImg="{ item }">
        <v-img v-if="item.photo" eager :src="item.photo"></v-img>
        <a v-if="item.photo" target="_blank" :href="item.photo">Veja mais</a>
      </template>
      <template #content="{ item }">
        <p>{{ item.summary }}</p>
      </template>
      <template #actions="{ item }">
        <v-btn
          color="#64318A"
          :href="item.url"
          target="_blank"
          class="white--text"
          :disabled="!item.url"
          >Saiba Mais</v-btn
        >
      </template>
    </DisplayData>
    <p class="grey--text font-weight-medium my-5">
      O acesso ao detalhamento das Patentes no Derwent Innovation contou com o
      apoio da Clarivate.
    </p>
  </div>
</template>

<script>
import { debounce } from "debounce";

import Background from "../components/Background.vue";
import BulletList from "../components/BulletList";
import DisplayData from "../components/DisplayData";
import HorizontalList from "../components/HorizontalList";
import MultipleFilters from "../components/MultipleFilters.vue";
import Panel from "../components/Panel.vue";

export default {
  components: {
    Background,
    BulletList,
    DisplayData,
    HorizontalList,
    MultipleFilters,
    Panel,
  },
  data: () => ({
    search: { term: "" },

    patents: [],
    selected_subareas: [],
    filters: undefined,
    rawTabs: [
      { name: "Necessidades Humanas", code: "A" },
      { name: "Operações de Processamento; Transporte", code: "B" },
      { name: "Química; Metalurgia", code: "C" },
      { name: "Têxteis; Papel", code: "D" },
      { name: "Construções Fixas", code: "E" },
      {
        name: "Engenharia Mecânica; Iluminação; Aquecimento; Armas; Explosão",
        code: "F",
      },
      { name: "Física", code: "G" },
    ],
  }),
  computed: {
    searchTerm() {
      return this.search.term === "" ? undefined : this.search.term;
    },
    queryParams() {
      return {
        areaMajors: this.filters?.primary,
        areaMinors: this.filters?.secondary,
        status: this.filters?.terciary[0],
        term: this.searchTerm,
      };
    },
    tabs() {
      return this.rawTabs.map((tab) => {
        const subareas = this.patents.reduce((acc, pat) => {
          if (pat.classification.primary.cip.substr(0, 1) === tab.code) {
            acc.add(pat.classification.primary.subarea);
          }

          if (pat.classification.secondary?.cip.substr(0, 1) === tab.code) {
            acc.add(pat.classification.secondary.subarea);
          }

          return acc;
        }, new Set());

        return {
          code: tab.code,
          name: `${tab.code} - ${tab.name}`,
          subareas: Array.from(subareas),
        };
      });
    },
    groups() {
      return [
        {
          label: "Status",
          items: ["Concedida", "Em análise", "Domínio Público"],
        },
      ];
    },
  },
  watch: {
    queryParams: debounce(async function () {
      try {
        this.patents = await this.$PatentAdapter.filterData(this.queryParams);
      } catch (error) {
        console.log(error);
      }
    }, 1000),
  },
  async beforeMount() {
    try {
      this.patents = await this.$PatentAdapter.requestData();
    } catch (error) {
      this.patents = [];
    }

    if (this.$route.query.q !== undefined)
      this.search.term = this.$route.query.q;
  },
};
</script>
