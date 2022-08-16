<template>
  <div style="min-height: 100vh">
    <div class="background">
      <Panel
        title="Competências"
        description="Nesta seção, você pode consultar quais as competências dos pesquisadores da USP, quem são e como contatá-los. A Plataforma Hub USPInovação utiliza como parâmetro de divisão de competências a Tabela das Áreas do Conhecimento apresentada pelo CNPq, e divide-as em dois níveis principais correspondentes, respectivamente, à área do conhecimento (ex.: Ciências Exatas e da Terra) e sua subárea (ex.: Matemática)."
        url="https://docs.google.com/forms/d/e/1FAIpQLSc-OmhsvBSUDBvx6uR6cvI6zq01M-_7JqdX4ktcB9mLE3oWzw/viewform"
        forms-call="Cadastre suas competências"
        :value="preSearch"
        @search="search.term = $event"
        @clear="search.skills = undefined"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :pre-selected-tabs="preSelectedTabs"
      :tabs="tabs"
      :colors="{ active: '#9b4c68', base: '#6b1c28' }"
      :groups="groups"
      @select="filters = $event"
    />

    <DisplayData
      :items="displayItems"
      group-name="Pesquisador"
      :selected="preSelected"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <v-container>
          <v-row v-for="{ name, label, site } in item.labsOrGroups" :key="name">
            <v-col v-if="site">
              <a :href="site" target="_blank">
                {{ label }}
              </a>
            </v-col>
            <v-col v-else>{{ label }}</v-col>
          </v-row>
          <v-row>
            <v-col>
              <span>{{ item.bond }}</span>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <span>{{ item.email }}</span>
            </v-col>
          </v-row>
          <v-row v-if="item.phone">
            <v-col>
              <span>{{ item.phone }}</span>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <span>{{ item.unities.join(", ") }}</span>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <span>{{ item.area.minors.join("; ") }}</span>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <span>{{ item.campus }}</span>
            </v-col>
          </v-row>
        </v-container>
      </template>
      <template #detailsImg="{ item }">
        <v-img
          v-if="item.picture"
          :key="item.picture"
          :lazy-src="require('@/static/base_skill_picture.png')"
          :src="item.picture"
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
          :src="require('@/static/base_skill_picture.png')"
        ></v-img>
      </template>
      <template #content="{ item }">
        <v-expansion-panels>
          <v-expansion-panel v-for="desc in itemDescriptions" :key="desc.key">
            <v-expansion-panel-header class="font-weight-bold">
              {{ desc.title }}
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-list>
                <v-list-item
                  v-for="description in item[desc.key]"
                  :key="description"
                >
                  <v-list-item-icon>
                    <v-icon v-text="'mdi-circle-small'"></v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <p class="mb-0">{{ description }}</p>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </template>
      <template #actions="{ item }">
        <div class="d-flex justify-space-around flex-wrap" style="width: 100%">
          <v-btn
            class="white--text"
            color="#6b1c28"
            target="_blank"
            :disabled="!item.lattes"
            :href="item.lattes"
            >lattes</v-btn
          >
        </div>
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
      skills: undefined,
    },

    itemDescriptions: [
      { key: "skills", title: "Competências" },
      { key: "services", title: "Serviços Tecnológicos" },
      { key: "equipments", title: "Equipamentos" },
    ],

    filters: undefined,
    filtered: undefined,
    queryParam: undefined,
    routeParam: undefined,

    unities: undefined,
  }),
  computed: {
    ...mapGetters({
      dataStatus: "competencia/dataStatus",
      isEmpty: "competencia/isEmpty",
      skills: "competencia/skills",
      searchKeys: "competencia/searchKeys",
      bonds: "competencia/bonds",
    }),
    tabs() {
      return this.$knowledgeAreas.map((area) => ({ ...area, description: "" }));
    },
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
        {
          label: "Vínculo do Pesquisador",
          items: this.bonds,
          preSelected: this.queryParam ? this.queryParam.vinculo : undefined,
        },
      ];
    },
    baseItems() {
      return this.filtered !== undefined ? this.filtered : this.skills;
    },
    displayItems() {
      return this.search.skills !== undefined
        ? this.search.skills
        : this.baseItems;
    },
    searchTerm() {
      return this.search.term;
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

    if (this.dataStatus == "ok" && this.skills.length == 0)
      this.fetchSpreadsheets({ ...env, areas: this.$knowledgeAreas });

    if (route.params.id) {
      this.routeParam = this.skills.find(
        (skill) => skill.id == route.params.id
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
      fetchSpreadsheets: "competencia/fetchSpreadsheets",
      setStrictResults: "global/setStrictResults",
      setFlexibleResults: "global/setFlexibleResults",
    }),
    fuzzySearch() {
      const searchResult = this.$fuzzySearch(
        removeAccent(this.search.term.trim()),
        this.baseItems,
        this.searchKeys
      );

      this.search.skills = searchResult;
    },
    filterData(context) {
      const campi = context.terciary[0];
      this.unities =
        campi != undefined
          ? this.$campi.find((c) => c.name == campi).unities
          : undefined;

      this.filtered = this.skills.filter((skill) =>
        this.$skillMatchesFilter(skill, context)
      );
    },
    async pipeline() {
      if (this.filters) this.filterData(this.filters);

      if (this.search.term && this.search.term.trim()) {
        this.$ga.event({
          eventCategory: "Competências",
          eventAction: "Search",
          eventLabel: this.search.term,
        });
        this.fuzzySearch();
      }
    },
  },
};
</script>

<style scoped>
.absolute {
  position: absolute;
}
.left-border {
  border-radius: 5px 0 0 5px;
}
.right-border {
  border-radius: 0 5px 5px 0;
}
</style>
