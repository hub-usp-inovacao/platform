<template>
  <div>
    <div class="background">
      <Panel
        v-model="search.term"
        title="P&amp;D&amp;I"
        description="Na seção de Pesquisa &amp; Desenvolvimento &amp; Inovação, você encontra laboratórios, organizações e programas que atuam com desenvolvimento e inovação no âmbito da USP. Aqui, você pode consultar informações e contatos de CEPIDs, EMBRAPIIs, INCTs e NAPs, de acordo com as áreas de competência e serviços realizados."
        @search="search.term = $event"
        @clear="search.term = ''"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="tabs"
      :colors="{ active: '#308C89', base: '#005C59' }"
      :groups="groups"
      @select="filters = $event"
    />

    <DisplayData :items="pdis" group-name="P&D&I" :has-image="false">
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <p class="body-2 font-italic">{{ item.category }}</p>
        <p class="body-2">{{ item.unity }}</p>
        <p class="body-2">{{ item.campus }}</p>
        <p class="body-2">{{ item.keywords.join('; ') }}</p>
        <p v-if="item.coordinator" class="body-2">
          Coordenador: {{ item.coordinator }}
        </p>
        <p v-if="item.phone" class="body-2">{{ item.phone }}</p>
        <p v-if="item.email" class="body-2">{{ item.email }}</p>
      </template>
      <template #content="{ item }">
        <p>{{ item.description }}</p>
      </template>
      <template #actions="{ item }">
        <v-btn
          class="white--text"
          target="_blank"
          :disabled="!item.site"
          :href="item.site"
          color="#005C59"
          >Saiba Mais</v-btn
        >
      </template>
    </DisplayData>
  </div>
</template>

<script>
import Background from "../components/Background.vue";
import Panel from "../components/Panel.vue";
import MultipleFilters from "../components/MultipleFilters.vue";
import DisplayData from "../components/DisplayData.vue";
import { debounce } from "debounce";

export default {
  components: {
    Panel,
    Background,
    MultipleFilters,
    DisplayData,
  },
  data: () => ({
    search: { term: "" },

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
    ],
    filters: undefined,
    pdis: [],
  }),
  computed: {
    groups() {
      return [
        {
          label: "Campus",
          items: this.$campi.map((c) => c.name),
        },
      ];
    },
    searchTerm() {
      return this.search.term === "" ? undefined : this.search.term;
    },
    queryParams() {
      return {
        categories: this.filters?.primary,
        campus: this.filters?.terciary[0],
        term: this.searchTerm,
      };
    },
  },
  watch: {
    queryParams: debounce(async function () {
      try {
        this.pdis = await this.$PDIAdapter.filterData(this.queryParams);
      } catch (e) {
        console.log(e);
      }
    }, 1000),
  },
  async beforeMount() {
    this.pdis = await this.$PDIAdapter.requestData();

    if (this.$route.query.q !== undefined)
      this.search.term = this.$route.query.q;
  },
};
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>
