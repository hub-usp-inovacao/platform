<template>
  <div>
    <div class="background">
      <Panel
        title="P&amp;D&amp;I"
        description="Na seção de Pesquisa &amp; Desenvolvimento &amp; Inovação, você encontra laboratórios, organizações e programas que atuam com desenvolvimento e inovação no âmbito da USP. Aqui, você pode consultar informações e contatos de CEPIDs, EMBRAPIIs, INCTs e NAPs, de acordo com as áreas de competência e serviços realizados."
        :value="preSearch"
        @search="search.term = $event"
        @clear="search.pdis = undefined"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :pre-selected-tabs="preSelectedTabs"
      :tabs="tabs"
      :colors="{ active: '#308C89', base: '#005C59' }"
      :groups="groups"
      @select="filters = $event"
    />

    <DisplayData
      :items="displayItems"
      group-name="P&D&I"
      :selected="preSelected"
      :has-image="false"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <p class="body-2 font-italic">{{ item.category }}</p>
        <p class="body-2">{{ item.unity }}</p>
        <p class="body-2">{{ item.campus }}</p>
        <p v-if="item.coordinator" class="body-2">
          Coordenador: {{ item.coordinator }}
        </p>
        <p v-if="item.phone" class="body-2">{{ item.phone }}</p>
        <p v-if="item.email" class="body-2">{{ item.email }}</p>
      </template>
      <template #content="{ item }">
        <div v-if="item.services.length > 0">
          <p>Serviços Tecnológicos</p>
          <ul>
            <li v-for="svc of item.services" :key="svc">{{ svc }}</li>
          </ul>
        </div>
        &nbsp;
        <p>{{ item.description.long }}</p>
      </template>
      <template #actions="{ item }">
        <v-btn
          class="white--text"
          target="_blank"
          :disabled="!item.url"
          :href="item.url"
          color="#005C59"
          >Saiba Mais</v-btn
        >
      </template>
    </DisplayData>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { removeAccent } from "@/lib/format";

import Background from "@/components/first_level/Background.vue";
import Panel from "@/components/first_level/Panel.vue";
import MultipleFilters from "@/components/first_level/MultipleFilters.vue";
import DisplayData from "@/components/first_level/DisplayData.vue";

export default {
  components: {
    Panel,
    Background,
    MultipleFilters,
    DisplayData,
  },
  data: () => ({
    search: {
      term: "",
      pdis: undefined,
    },

    tabs: [
      {
        name: "CEPID",
        description:
          "Centros de Pesquisa, Inovação e Difusão fomentados pela FAPESP que atuam na USP desenvolvendo investigações para contribuir à inovação por meio das transferências de tecnologia, além de oferecer atividades de extensão para o público em geral.",
        id: "cepid-filter",
      },
      {
        name: "EMBRAPII",
        description:
          "A EMBRAPII (Associação Brasileira de Pesquisa e Inovação Industrial), apoia instituições de pesquisa tecnológica, em determinadas áreas de competência, para executarem projetos de desenvolvimento de pesquisa tecnológica para inovação, em cooperação com empresas do setor industrial.",
        id: "embrapii-filter",
      },
      {
        name: "INCT",
        description:
          "Institutos Nacionais de Ciência e Tecnologia, fomentados pelo CNPq  articulam os  grupos de pesquisa na fronteira da ciência e em áreas estratégicas para o desenvolvimento sustentável do país; além de estimular aplicações para promover a inovação e o espírito empreendedor.",
        id: "inct-filter",
      },
      {
        name: "NAP",
        description:
          "Órgãos de integração que reúnem especialistas de diferentes áreas da universidade para pesquisas de caráter interdisciplinar e transdisciplinar, promovendo maior comunicação e integração entre as Unidades USP e variadas áreas do conhecimento.",
        id: "nap-filter",
      },
      {
        name: "Centro de Pesquisa em Engenharia",
        description:
          "Fomentados pela FAPESP, combinam o modelo do Programa Especial CEPID ao PITE, no qual há uma empresa parceira co-financiadora da pesquisa fortemente motivada para participar da definição dos temas focais a serem investigados, participar ativamente dos projetos de pesquisa, assim como para utilizar os resultados obtidos.",
      },
      {
        name: "Centrais Multiusuário",
        description: "",
      },
    ],
    unities: undefined,
    filters: undefined,
    filtered: undefined,
    queryParam: undefined,
    routeParam: undefined,
  }),
  computed: {
    ...mapGetters({
      dataStatus: "pdi/dataStatus",
      storePDIs: "pdi/pdis",
      isEmpty: "pdi/isEmpty",
      searchKeys: "pdi/searchKeys",
    }),
    groups() {
      return [
        {
          label: "Campus",
          items: this.$campi.map((c) => c.name),
          preSelected: this.queryParam ? this.queryParam.campus : undefined,
        },
        {
          label: "Unidade",
          items:
            this.unities == undefined
              ? this.$campi
                  .reduce((acc, value) => {
                    return acc.concat(value.unities);
                  }, [])
                  .sort()
              : this.unities,
          preSelected: this.queryParam ? this.queryParam.unidade : undefined,
        },
      ];
    },
    searchTerm() {
      return this.search.term;
    },
    pdis: function () {
      return this.dataStatus == "ok" ? this.storePDIs : [];
    },
    baseItems: function () {
      return this.filtered !== undefined ? this.filtered : this.pdis;
    },
    displayItems() {
      return this.search.pdis !== undefined ? this.search.pdis : this.baseItems;
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
    const payload = { sheetsAPIKey: process.env.sheetsAPIKey };
    const route = this.$route;

    if (this.dataStatus == "ok" && this.pdis.length == 0)
      this.$store.dispatch("pdi/fetchSpreadsheets", payload);

    if (route.params.id) {
      this.routeParam = this.pdis.find((pdi) => pdi.id == route.params.id);
    } else if (route.query && Object.keys(route.query).length > 0) {
      this.queryParam = route.query;
    }

    if (this.queryParam && this.queryParam.buscar) {
      this.search.term = this.queryParam.buscar;
    }
  },
  methods: {
    ...mapActions({
      setStrictResults: "global/setStrictResults",
      setFlexibleResults: "global/setFlexibleResults",
    }),
    fuzzySearch() {
      const searchResult = this.$fuzzySearch(
        removeAccent(this.search.term.trim()),
        this.baseItems,
        this.searchKeys
      );

      this.search.pdis = searchResult;
    },
    filterData(context) {
      const campi = context.terciary[0];
      this.unities =
        campi != undefined
          ? this.$campi.find((c) => c.name == campi).unities
          : undefined;

      this.filtered = this.pdis.filter((pdi) => pdi.matchesFilter(context));
    },
    async pipeline() {
      if (this.filters) this.filterData(this.filters);

      if (this.search.term && this.search.term.trim()) {
        this.$ga.event({
          eventCategory: "P&D&I",
          eventAction: "Search",
          eventLabel: this.search.term,
        });
        this.fuzzySearch();
      }
    },
  },
};
</script>

<style scoped></style>
