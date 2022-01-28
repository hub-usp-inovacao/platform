<template>
  <div>
    <div class="background">
      <Panel
        title="Empresas"
        description="As Empresas DNA USP estão organizadas nesta plataforma por: CNAEs (Classificação Nacional de Atividades Econômicas), cidade, habitats de inovação e porte."
        url="https://docs.google.com/forms/d/1q354be1_cPpeSIWVQkU2CXUpjUiyYuC0IU5W1_4W_zA/edit?usp=sharing"
        forms-call="Cadastre sua empresa aqui"
        second-url="/DNA_manual.pdf"
        second-call="Manual de uso da marca DNA USP"
        :value="preSearch"
        @search="search.term = $event"
        @clear="search.companies = undefined"
      />
      <USPDNA />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :pre-selected-tabs="preSelectedTabs"
      :tabs="tabs"
      :groups="groups"
      :colors="{ base: '#074744', active: '#0A8680' }"
      @select="filters = $event"
    />

    <DisplayData
      :items="displayItems"
      group-name="Empresas"
      :selected="preSelected"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <v-container>
          <p v-for="phone in item.phones" :key="phone">{{ phone }}</p>
          <p v-for="email in item.emails" :key="email">{{ email }}</p>
          <p>{{ item.address.venue }}</p>
          <p>{{ item.address.neightborhood }}</p>
          <p>{{ item.address.city.join(",") }} - {{ item.address.state }}</p>
          <p>{{ item.address.cep }}</p>
        </v-container>
      </template>
      <template #detailsImg="{ item }">
        <v-img
          v-if="item.logo"
          :key="item.logo"
          :lazy-src="require('@/static/base_company_picture.png')"
          :src="item.logo"
        >
          <template v-slot:placeholder>
            <v-row class="fill-height ma-0" align="center" justify="center">
              <v-progress-circular
                indeterminate
                color="grey lighten-5"
              ></v-progress-circular>
            </v-row>
          </template>
        </v-img>
        <v-img
          v-else
          eager
          :src="require('@/static/base_company_picture.png')"
        ></v-img>
      </template>
      <template #content="{ item }">
        <p v-if="item.incubated">
          <span class="font-weight-bold"
            >Incubadora{{ item.ecosystems.length > 1 ? "(s)" : "" }}</span
          >
          <span v-for="incub of item.ecosystems" :key="incub"
            >{{ incub }};&nbsp;</span
          >
        </p>

        <div v-for="size of item.companySize" :key="size">
          <p v-if="size != 'Unicórnio'">
            <span class="font-weight-bold">Porte:</span>
            {{ size }}
          </p>
        </div>

        <p>
          <span class="font-weight-bold">Descrição:</span>
          {{ item.description.long }}
        </p>

        <BulletList
          v-if="item.services.length > 0"
          title="Produtos e Serviços"
          :items="item.services"
        />

        <BulletList
          v-if="item.technologies.length > 0"
          title="Tecnologias"
          :items="item.technologies"
        />
      </template>
      <template #actions="{ item }">
        <v-btn
          class="white--text"
          color="#2bc570"
          :href="item.url"
          :disabled="!item.url"
          target="_blank"
          >Saiba Mais</v-btn
        >
      </template>
    </DisplayData>
    <p
      align="center"
      justify="center"
      class="grey--text font-weight-medium my-5"
    >
      As informações nesta seção são de responsabilidade de cada empresa.
    </p>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { removeAccent } from "@/lib/format";

import Background from "@/components/first_level/Background.vue";
import USPDNA from "@/components/first_level/USPDNA.vue";
import Panel from "@/components/first_level/Panel.vue";
import MultipleFilters from "@/components/first_level/MultipleFilters.vue";
import DisplayData from "@/components/first_level/DisplayData.vue";
import BulletList from "@/components/first_level/BulletList.vue";

export default {
  components: {
    Panel,
    MultipleFilters,
    Background,
    DisplayData,
    USPDNA,
    BulletList,
  },
  data: () => ({
    filters: undefined,
    filtered: undefined,
    search: {
      term: "",
      companies: undefined,
    },
    queryParam: undefined,
    routeParam: undefined,
  }),
  computed: {
    ...mapGetters({
      dataStatus: "empresas/dataStatus",
      companies: "empresas/companies",
      searchKeys: "empresas/searchKeys",
      incubators: "empresas/incubators",
      cities: "empresas/cities",
      isEmpty: "empresas/isEmpty",
    }),
    searchTerm() {
      return this.search.term;
    },
    tabs() {
      return Object.keys(this.$cnae).reduce((acc, code) => {
        const { major, minor } = this.$cnae[code];

        const tab = acc.find(({ name }) => name === major);

        if (tab) {
          if (!tab.subareas) tab.subareas = [];

          if (!tab.subareas.includes(minor)) tab.subareas.push(minor);
        } else {
          acc.push({
            name: major,
            subareas: [minor],
          });
        }

        return acc;
      }, []);
    },
    baseItems() {
      return this.filtered !== undefined ? this.filtered : this.companies;
    },
    displayItems() {
      return this.search.companies !== undefined
        ? this.search.companies
        : this.baseItems;
    },
    groups() {
      return [
        {
          label: "Cidade",
          items: this.cities,
          preSelected: this.queryParam ? this.queryParam.cidade : undefined,
        },
        {
          label: "Habitat de Inovação",
          items: this.incubators,
          preSelected: this.queryParam ? this.queryParam.incubadora : undefined,
        },
        {
          label: "Porte",
          items: this.$Company.sizes,
          preSelected: this.queryParam ? this.queryParam.porte : undefined,
        },
      ];
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
      if (!this.empty) {
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
    const route = this.$route;

    if (this.dataStatus == "ok" && this.companies.length == 0) {
      this.fetchSpreadsheets({});
    }

    if (route.params.id) {
      this.routeParam = this.companies.find(
        (company) => company._id.$oid == route.params.id
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
      fetchSpreadsheets: "empresas/fetchSpreadsheets",
      setStrictResults: "global/setStrictResults",
      setFlexibleResults: "global/setFlexibleResults",
    }),
    fuzzySearch() {
      const searchResult = this.$fuzzySearch(
        removeAccent(this.search.term.trim()),
        this.baseItems,
        this.searchKeys
      );

      this.search.companies = searchResult;
    },
    filterData(context) {
      this.filtered = this.companies.filter((company) =>
        this.$companyMatchesFilter(company, context)
      );
    },
    async pipeline() {
      if (this.filters) this.filterData(this.filters);

      if (this.search.term && this.search.term.trim()) {
        this.$ga.event({
          eventCategory: "Empresas",
          eventAction: "Search",
          eventLabel: this.search.term,
        });
        this.fuzzySearch();
      }
    },
  },
};
</script>
