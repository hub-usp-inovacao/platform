<template>
  <div>
    <div class="background">
      <Panel
        v-model="search.term"
        title="Iniciativas"
        description="A USP mantém diversas iniciativas e programas para facilitar e estimular a inovação e o empreendedorismo, fazendo a ponte entre o ambiente acadêmico, as organizações e a sociedade. Clique nos ícones para conhecer os tipos de iniciativas e acessar as formas de contatar cada uma delas."
        url="http://www.inovacao.usp.br/editais/"
        forms-call="Confira os Editais"
        second-url="http://www.inovacao.usp.br/programas/"
        second-call="confira os Programas"
        @search="search.term = $event"
        @clear="search.term = ''"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="tabs"
      :colors="{ base: '#222c63', active: '#525c93' }"
      :groups="groups"
      @select="filters = $event"
    />

    <DisplayData
      :items="initiatives"
      group-name="Iniciativas"
      :has-image="false"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <NDText classes="body-2" :text="item.unity" />
        <p class="body-2 mb-4">{{ item.localization }}</p>
        <v-row>
          <v-col v-if="item.email && item.email.length > 0">
            <p class="body-2 mb-4 mr-8">{{ item.email.join("; ") }}</p>
          </v-col>
          <v-col>
            <NDText classes="body-2 mb-4" :text="item.contact.info" />
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
import { debounce } from "debounce";

import Background from "../components/Background.vue";
import DisplayData from "../components/DisplayData.vue";
import MultipleFilters from "../components/MultipleFilters.vue";
import Panel from "../components/Panel.vue";
import NDText from "../components/NDText.vue";

export default {
  components: {
    Panel,
    Background,
    MultipleFilters,
    DisplayData,
    NDText,
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

    search: { term: "" },

    filters: undefined,
    initiatives: [],
  }),
  computed: {
    groups() {
      return [
        {
          label: "Campus",
          items: this.$campi
            .map((c) => c.name)
            .concat(["Toda a USP"])
            .sort(),
        },
      ];
    },
    searchTerm() {
      return this.search.term === "" ? undefined : this.search.term;
    },
    queryParams() {
      return {
        classifications: this.filters?.primary,
        campus: this.filters?.terciary[0],
        term: this.searchTerm,
      };
    },
  },
  watch: {
    queryParams: debounce(function () {
      this.runSearch();
    }, 1000),
  },
  async beforeMount() {
    this.initiatives = await this.$InitiativeAdapter.requestData();
    if (this.$route.query.q !== undefined)
      this.search.term = this.$route.query.q;
  },
  methods: {
    async runSearch() {
      try {
        this.initiatives = await this.$InitiativeAdapter.filterData(
          this.queryParams
        );
      } catch (e) {
        console.log(e);
      }
    },
  },
};
</script>
