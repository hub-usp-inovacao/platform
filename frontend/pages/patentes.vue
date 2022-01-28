<template>
  <div>
    <div class="background">
      <Panel
        title="Patentes"
        description="Os pesquisadores e estudantes da USP desenvolvem invenções que são protegidas por propriedade industrial - PI (patentes e registros de software). Estas PI estão disponíveis para organizações públicas e privadas que tenham interesse em licenciamento para aplicação e comercialização. Nesta plataforma, as PI estão organizadas por áreas tecnológicas e status (concedida, em análise e domínio público)."
        :value="preSearch"
        @search="search.term = $event"
        @clear="search.patents = undefined"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :pre-selected-tabs="preSelectedTabs"
      :tabs="tabs"
      :groups="groups"
      :colors="{ base: '#5B2C7D', active: '#9247C9' }"
      @select="filters = $event"
    />

    <DisplayData
      :reverse="true"
      :items="displayItems"
      group-name="Patentes"
      :selected="preSelected"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <p>
          <span>Classificação:</span>
          {{ item.classification.primary.cip }}
        </p>

        <p>{{ item.classification.primary.subareas }}</p>

        <HorizontalList
          title="Países com Proteção"
          :items="item.countriesWithProtection"
        />

        <HorizontalList
          v-if="item.ipcs.length > 0 && item.ipcs[0] != ''"
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
    <p
      align="center"
      justify="center"
      class="grey--text font-weight-medium my-5"
    >
      O acesso ao detalhamento das Patentes no Derwent Innovation contou com o
      apoio da Clarivate.
    </p>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { removeAccent } from "@/lib/format";

import Background from "@/components/first_level/Background.vue";
import Panel from "@/components/first_level/Panel.vue";
import MultipleFilters from "@/components/first_level/MultipleFilters.vue";
import BulletList from "@/components/first_level/BulletList.vue";
import DisplayData from "@/components/first_level/DisplayData.vue";
import HorizontalList from "@/components/first_level/HorizontalList.vue";

export default {
  components: {
    Panel,
    Background,
    MultipleFilters,
    BulletList,
    DisplayData,
    HorizontalList,
  },
  data: () => ({
    search: {
      term: "",
      patents: undefined,
    },

    selected_subareas: [],
    filters: undefined,
    filtered: undefined,
    queryParam: undefined,
    routeParam: undefined,
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
    ...mapGetters({
      dataStatus: "patentes/dataStatus",
      patents: "patentes/patents",
      isEmpty: "patentes/isEmpty",
      searchKeys: "patentes/searchKeys",
    }),
    searchTerm() {
      return this.search.term;
    },
    tabs() {
      return this.rawTabs.map((tab) => {
        const subareas = this.patents.reduce((acc, pat) => {
          if (pat.classification.primary.cip.substr(0, 1) == tab.code) {
            acc.add(pat.classification.primary.subarea);
          }

          if (pat.classification.secondary?.cip.substr(0, 1) == tab.code) {
            acc.add(pat.classification.secondary.subarea);
          }

          return acc;
        }, new Set());

        return {
          ...tab,
          subareas: Array.from(subareas),
        };
      });
    },
    groups() {
      return [
        {
          label: "Status",
          items: ["Concedida", "Em análise", "Domínio Público"],
          preSelected: this.queryParam ? this.queryParam.status : undefined,
        },
      ];
    },
    baseItems() {
      return this.filtered !== undefined ? this.filtered : this.patents;
    },
    displayItems() {
      return this.search.patents !== undefined
        ? this.search.patents
        : this.baseItems;
    },
    preSelected() {
      if (this.queryParam && this.queryParam.nome) {
        return this.displayItems.find(
          (item) => item.name == this.queryParam.nome
        );
      }

      return this.routeParam;
    },
    preSearch() {
      return this.queryParam ? this.queryParam.buscar : undefined;
    },
    preSelectedTabs() {
      if (this.queryParam && this.queryParam.areas) {
        return this.queryParam.areas
          .split(";")
          .map((area) => area.trim())
          .filter((area) => area.trim().length > 0);
      }

      return undefined;
    },
  },
  watch: {
    isEmpty() {
      if (!this.isEmpty) {
        if (this.filters != undefined || this.searchTerm != "") {
          this.pipeline();
        }
      }
    },
    searchTerm() {
      this.pipeline();
    },
    filters() {
      this.pipeline();
    },
  },
  beforeMount() {
    const env = { sheetsAPIKey: process.env.sheetsAPIKey };
    const route = this.$route;

    if (this.dataStatus == "ok" && this.patents.length == 0) {
      this.fetchSpreadsheets(env);
    }

    if (route.params.id) {
      this.routeParam = this.patents.find(
        (patent) => patent._id.$oid == route.params.id
      );
    } else if (route.query && Object.keys(route.query).length > 0) {
      this.queryParam = route.query;
    }

    if (this.queryParam && this.queryParam.buscar) {
      this.search.term = this.queryParam.buscar;
    }
  },
  methods: {
    ...mapActions({
      fetchSpreadsheets: "patentes/fetchSpreadsheets",
      setStrictResults: "global/setStrictResults",
      setFlexibleResults: "global/setFlexibleResults",
    }),
    fuzzySearch() {
      const searchResult = this.$fuzzySearch(
        removeAccent(this.search.term.trim()),
        this.baseItems,
        this.searchKeys
      );

      this.search.patents = searchResult;
    },
    filterData(context) {
      const primaryCodes = context.primary.map(
        (filterTab) => this.tabs.find((tab) => tab.name == filterTab).code
      );

      this.filtered = this.patents.filter((patent) =>
        this.$patentMatchesFilter(patent, { ...context, primary: primaryCodes })
      );
      console.log(this.filtered);
    },
    async pipeline() {
      if (this.filters) this.filterData(this.filters);

      if (this.search.term && this.search.term.trim()) {
        this.$ga.event({
          eventCategory: "Patentes",
          eventAction: "Search",
          eventLabel: this.search.term,
        });
        this.fuzzySearch();
      }
    },
  },
};
</script>
