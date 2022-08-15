<template>
  <div>
    <div class="background">
      <Panel
        v-if="FLAG_COMPANY_UPDATE_RELEASED"
        v-model="search.term"
        title="Empresas"
        description="As Empresas DNA USP estão organizadas nesta plataforma por: CNAEs (Classificação Nacional de Atividades Econômicas), cidade, habitats de inovação e porte."
        url="https://docs.google.com/forms/d/1q354be1_cPpeSIWVQkU2CXUpjUiyYuC0IU5W1_4W_zA/edit?usp=sharing"
        forms-call="Cadastre sua empresa aqui"
        second-url="/empresas/atualizar/solicitar"
        second-call="Atualize seu cadastro aqui"
        third-url="/DNA_manual.pdf"
        third-call="Manual de uso da marca DNA USP"
        @search="search.term = $event"
        @clear="search.term = ''"
      />
      <Panel
        v-else
        v-model="search.term"
        title="Empresas"
        description="As Empresas DNA USP estão organizadas nesta plataforma por: CNAEs (Classificação Nacional de Atividades Econômicas), cidade, habitats de inovação e porte."
        url="https://docs.google.com/forms/d/1q354be1_cPpeSIWVQkU2CXUpjUiyYuC0IU5W1_4W_zA/edit?usp=sharing"
        forms-call="Cadastre sua empresa aqui"
        second-url="/DNA_manual.pdf"
        second-call="Manual de uso da marca DNA USP"
        @search="search.term = $event"
        @clear="search.term = ''"
      />
      <USPDNA />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="tabs"
      :groups="groups"
      :colors="{ base: '#074744', active: '#0A8680' }"
      @select="filters = $event"
    />

    <DisplayData :items="companies" group-name="Empresas">
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <v-container>
          <p v-for="phone in item.phones" :key="phone">{{ phone }}</p>
          <p v-for="email in item.emails" :key="email">{{ email }}</p>
          <p>{{ item.address.venue }}</p>
          <p>{{ item.address.neighborhood }}</p>
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
          <p v-if="size !== 'Unicórnio'">
            <span class="font-weight-bold">Porte:</span>
            {{ size }}
          </p>
        </div>

        <p>
          <span class="font-weight-bold">Descrição:</span>
          {{ item.description }}
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
    <p class="grey--text font-weight-medium my-5">
      As informações nesta seção são de responsabilidade de cada empresa.
    </p>
  </div>
</template>

<script>
import { debounce } from "debounce";

import Background from "../components/Background";
import BulletList from "../components/BulletList.vue";
import DisplayData from "../components/DisplayData.vue";
import MultipleFilters from "../components/MultipleFilters.vue";
import Panel from "../components/Panel.vue";
import USPDNA from "../components/USPDNA.vue";

export default {
  components: {
    Background,
    BulletList,
    DisplayData,
    MultipleFilters,
    Panel,
    USPDNA,
  },
  data: () => ({
    filters: undefined,
    search: { term: "" },
    companies: [],
    ecosystems: [],
    companySizes: [
      "Microempresa",
      "Pequena Empresa",
      "Média Empresa",
      "Grande Empresa",
      "Unicórnio",
      "Não Informado",
    ],
  }),
  computed: {
    FLAG_COMPANY_UPDATE_RELEASED() {
      return process.env.OPEN_COMPANY_UPDATE_FORMS === "true";
    },
    cities() {
      const cities = this.companies.reduce((all, company) => {
        return all.concat(
          company.address.city.filter((city) => {
            return city !== "N/D" && city !== "n/d";
          })
        );
      }, []);

      const citiesSet = cities
        .map((city) => city.trim())
        .filter((city) => city.length > 0)
        .reduce((set, city) => {
          if (!set[city]) {
            set[city] = city;
          }

          return set;
        }, {});

      return Object.keys(citiesSet).sort((a, b) => a.localeCompare(b));
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
    groups() {
      return [
        {
          label: "Cidade",
          items: this.cities,
        },
        {
          label: "Habitat de Inovação",
          items: this.ecosystems,
        },
        {
          label: "Porte",
          items: this.companySizes,
        },
      ];
    },
    searchTerm() {
      return this.search.term === "" ? undefined : this.search.term;
    },
    queryParams() {
      return {
        areaMajors: this.filters?.primary,
        areaMinors: this.filters?.secondary,
        city: this.filters?.terciary[0],
        ecosystem: this.filters?.terciary[1],
        size: this.filters?.terciary[2],
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
    this.companies = await this.$CompanyAdapter.requestData();
    this.ecosystems = await this.$CompanyAdapter.getEcosystems();

    if (this.$route.query.q !== undefined)
      this.search.term = this.$route.query.q;
  },
  methods: {
    async runSearch() {
      try {
        this.companies = await this.$CompanyAdapter.filterData(
          this.queryParams
        );
      } catch (error) {
        console.log(error);
      }
    },
  },
};
</script>