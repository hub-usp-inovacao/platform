<template>
  <div>
    <div class="background">
      <Panel
        title="Iniciativas"
        description="A USP mantém diversas iniciativas e programas para facilitar e estimular a inovação e o empreendedorismo, fazendo a ponte entre o ambiente acadêmico, as organizações e a sociedade. Clique nos ícones para conhecer os tipos de iniciativas e acessar as formas de contatar cada uma delas."
        url="http://www.inovacao.usp.br/editais/"
        forms-call="Confira os Editais"
        second-url="http://www.inovacao.usp.br/programas/"
        second-call="confira os Programas"
        :value="preSearch"
        @search="search.term = $event"
        @clear="search.iniciatives = undefined"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :pre-selected-tabs="preSelectedTabs"
      :tabs="tabs"
      :colors="{ base: '#222c63', active: '#525c93' }"
      :groups="groups"
      @select="filters = $event"
    />

    <DisplayData
      :items="display_entries"
      group-name="Iniciativas"
      :selected="preSelected"
      :has-image="false"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <p class="body-2">{{ item.unity }}</p>
        <p class="body-2 mb-4">{{ item.localization }}</p>
        <v-row>
          <v-col v-if="item.email">
            <p class="body-2 mb-4 mr-8">{{ item.email }}</p>
          </v-col>
          <v-col>
            <p
              class="body-2 mb-4"
            >
              {{ item.contact.info }}
            </p>
          </v-col>
        </v-row>
      </template>
      <template #content="{ item }">{{ item.description }}</template>
      <template #actions="{ item }">
        <v-btn
          :href="item.url"
          :disabled="!item.url"
          target="_blank"
          color="#222c63"
          class="white--text"
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
    tabs: [
      {
        name: "Agente Institucional",
        description:
          "Conjunto de agentes designados na estrutura da USP para atender a comunidade interna e externa em temas relacionados à pesquisa, inovação, empreendedorismo e internacionalização.",
      },
      {
        name: "Empresa Jr.",
        description:
          "Associações formadas pelos alunos da USP, de caráter empreendedor e sem fins lucrativos, que prestam diferentes tipos de serviços visando a capacitação profissional dos estudantes em suas respectivas carreiras.",
      },
      {
        name: "Entidade Associada",
        description: "Entidade Associada",
      },
      {
        name: "Entidade Estudantil",
        description:
          "Organizações formais de estudantes que incentivam a realização de projetos de inovação e empreendedorismo em diversas áreas de P&D&I.",
      },
      {
        name: "Espaço/Coworking",
        description:
          "Ambientes físicos e on-line destinados à troca de experiências relacionadas à inovação, empreendedorismo, desenvolvimento tecnológico e de novos negócios.",
      },
      {
        name: "Grupos e Iniciativas Estudantis",
        description:
          "Iniciativas organizadas por estudantes para incentivar a realização de projetos de inovação e empreendedorismo nas mais diversas áreas de P&D&I.",
      },
      {
        name: "Ideação",
        description:
          "Grupos voltados à discussão e incentivo ao desenvolvimento de negócios e inovações originários de diferentes áreas do conhecimento.",
      },
      {
        name: "Incubadora e Parque Tecnológico",
        description:
          "Habitats de inovação com o propósito de fornecer suporte à transformação do conhecimento em desenvolvimento econômico utilizando inovação tecnológica, social e cultural. Recebem principalmente os empreendedores vindos da USP.",
      },
    ],

    search: {
      term: "",
      iniciatives: undefined,
    },

    filters: undefined,
    filtered: undefined,
    queryParam: undefined,
    routeParam: undefined,
  }),
  computed: {
    ...mapGetters({
      iniciatives: "iniciativas/iniciatives",
      isEmpty: "iniciativas/isEmpty",
      dataStatus: "iniciativas/dataStatus",
      searchKeys: "iniciativas/searchKeys",
    }),
    groups() {
      return [
        {
          label: "Campus",
          items: this.$campi
            .map((c) => c.name)
            .concat(["Toda a USP"])
            .sort(),
          preSelected: this.queryParam ? this.queryParam.campus : undefined,
        },
      ];
    },
    searchTerm() {
      return this.search.term;
    },
    baseItems() {
      return this.filtered !== undefined ? this.filtered : this.iniciatives;
    },
    display_entries() {
      return this.search.iniciatives !== undefined
        ? this.search.iniciatives
        : this.baseItems;
    },
    preSelected() {
      if (this.queryParam && this.queryParam.nome) {
        return this.display_entries.find(
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
      console.log(this.search.term);
      this.pipeline();
    },
    filters() {
      this.pipeline();
    },
  },
  beforeMount() {
    const env = { sheetsAPIKey: process.env.sheetsAPIKey };
    const route = this.$route;

    if (this.dataStatus == "ok" && this.iniciatives.length == 0) {
      this.fetchSpreadsheets(env);
    }

    if (route.params.id) {
      this.routeParam = this.iniciatives.find(
        (iniciative) => iniciative.id == route.params.id
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
      fetchSpreadsheets: "iniciativas/fetchSpreadsheets",
      setStrictResults: "global/setStrictResults",
      setFlexibleResults: "global/setFlexibleResults",
    }),
    fuzzySearch() {
      const searchResult = this.$fuzzySearch(
        removeAccent(this.search.term.trim()),
        this.baseItems,
        this.searchKeys
      );

      this.search.iniciatives = searchResult;
    },
    filterData(context) {
      this.filtered = this.iniciatives.filter((iniciative) =>
        this.$iniciativeMatchesFilter(iniciative,context)
      );
    },
    async pipeline() {
      if (this.filters) this.filterData(this.filters);

      if (this.search.term && this.search.term.trim()) {
        this.$ga.event({
          eventCategory: "Iniciativas",
          eventAction: "Search",
          eventLabel: this.search.term,
        });
        this.fuzzySearch();
      }
    },
  },
};
</script>
