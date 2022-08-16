<template>
  <div>
    <div class="background">
      <Panel
        v-model="term"
        title="Busca Global"
        description="Aqui, você faz uma busca textual por todo o conteúdo do Hub"
        @clean="term = ''"
        @search="term = $event"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="dataTypes"
      :colors="yellowColors"
      @select="filters = $event"
    />

    <v-container>
      <v-card :style="cardStyle">
        <v-card-title>Resultados de busca para "{{ term }}":</v-card-title>
        <v-card-text>
          <v-list v-if="filtered.length > 0">
            <v-list-item-group v-model="selected">
              <v-list-item
                v-for="(item, index) in filtered"
                :key="index"
                three-line
              >
                <v-list-item-content :class="contentMarginStyle">
                  <v-list-item-title class="font-weight-bold item-title">
                    {{ item.name }}
                  </v-list-item-title>
                  <v-list-item-subtitle class="hidden-sm-and-down">
                    {{ item.description }}
                  </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-chip>{{ item.category }}</v-chip>
                </v-list-item-action>
              </v-list-item>
            </v-list-item-group>
          </v-list>
          <p v-else>Nenhum resultado a exibir</p>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import Panel from "../components/Panel.vue";
import Background from "../components/Background.vue";
import MultipleFilters from "../components/MultipleFilters.vue";
import { debounce } from "debounce";

export default {
  components: {
    Panel,
    Background,
    MultipleFilters,
  },
  data: () => ({
    term: "",
    dataTypes: [
      { name: "Competências" },
      { name: "Disciplinas" },
      { name: "Empresas" },
      { name: "Iniciativas" },
      { name: "Patentes" },
      { name: "P&D&I" },
    ],
    yellowColors: { base: "#fa00ee", active: "#9600fa" },
    filters: undefined,
    filtered: [],
    selected: undefined,
  }),
  computed: {
    isFiltersEmpty() {
      return this.filters === undefined || this.filters.primary.length === 0;
    },
    selectedTypes() {
      const allTypes = this.dataTypes.reduce(
        (acc, { name }) => acc.concat(name),
        []
      );

      if (this.isFiltersEmpty) return allTypes;

      return this.filters.primary;
    },
    cardStyle() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
        case "sm":
          return;
        default:
          return {
            overflowY: "auto",
            height: "35rem",
          };
      }
    },
    contentMarginStyle() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
        case "sm":
          return "";
        default:
          return "ml-2";
      }
    },
    query() {
      return this.$route.query.q;
    },
  },
  watch: {
    term() {
      this.runSearch();
    },
    selectedTypes() {
      this.runSearch();
    },
    selected() {
      if (this.selected === undefined) return;

      const entry = this.filtered[this.selected];

      this.$router.replace({
        path: this.pathOf(entry.category),
        query: { q: this.term },
      });
    },
  },
  beforeMount() {
    if (!this.query) this.$router.replace("/");
    else this.term = this.query;
  },
  methods: {
    pathOf(type) {
      switch (type) {
        case "Competências":
          return "/competencias";
        case "Disciplinas":
          return "/educacao";
        case "Empresas":
          return "/empresas";
        case "Iniciativas":
          return "/iniciativas";
        case "Patentes":
          return "/patentes";
        default:
          return "/inovacao";
      }
    },
    getAdapterOf(type) {
      switch (type) {
        case "Competências":
          return this.$ResearcherAdapter;
        case "Disciplinas":
          return this.$DisciplineAdapter;
        case "Empresas":
          return this.$CompanyAdapter;
        case "Iniciativas":
          return this.$InitiativeAdapter;
        case "Patentes":
          return this.$PatentAdapter;
        default:
          return this.$PDIAdapter;
      }
    },
    runSearch: debounce(async function () {
      this.filtered = [];

      for (const type of this.selectedTypes) {
        try {
          const partial = await this.getAdapterOf(type).filterData({
            term: this.term,
          });
          this.filtered = this.filtered.concat(
            partial.map((p) => ({ ...p, category: type }))
          );
        } catch (e) {
          console.log(e);
        }
      }
    }, 500),
  },
};
</script>

<style scoped>
.left-border {
  border-radius: 5px 0 0 5px;
}

.right-border {
  border-radius: 0 5px 5px 0;
}

.item-title {
  white-space: normal;
}
</style>
