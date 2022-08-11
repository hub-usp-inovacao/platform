<template>
  <div>
    <div class="background">
      <Panel
        title="Educação"
        description="A USP oferece aos seus estudantes diversas disciplinas em nível de graduação e pós-graduação que se relacionam aos temas de Empreendedorismo e Inovação. Ao fazer uma busca, você encontrará as unidades, as condições de oferecimento, códigos e links para acesso às ementas nos sistemas institucionais, o Júpiter e o Janus."
        :value="preSearch"
        @search="search.term = $event"
        @clear="search.disciplines = undefined"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :pre-selected-tabs="preSelectedTabs"
      :tabs="tabs"
      :groups="groups"
      :colors="{ base: '#db8337', active: '#ab5307' }"
      @select="filters = $event"
    />

    <DisplayData
      :items="displayItems"
      group-name="Disciplinas"
      :has-image="false"
      :selected="preSelected"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <v-container>
          <p class="body-2">{{ item.campus }}</p>
          <p class="body-2">{{ item.unity }}</p>
          <p class="body-2">{{ item.offeringPeriod }}</p>
          <p class="body-2">{{ item.nature }}</p>
        </v-container>
      </template>
      <template #content="{ item }">
        <p>{{ item.description.long }}</p>
      </template>
      <template #actions="{ item }">
        <v-btn
          class="white--text"
          color="#db8337"
          :href="item.url"
          target="_blank"
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
      disciplines: undefined,
    },

    levels: ["Graduação", "Pós-Graduação"],
    tabs: [
      {
        name: "Inovação",
        description: "Cursos e disciplinas relacionados à área de Inovação.",
        id: "innovation-filter",
      },
      {
        name: "Empreendedorismo",
        description:
          "Cursos e disciplinas relacionados à área de Empreendedorismo.",
        id: "enterpreneuship-filter",
      },
      {
        name: "Propriedade Intelectual",
        description:
          "Cursos e disciplinas relacionados à área de Propriedade Intelectual.",
        id: "intelectual-property-filter",
      },
      {
        name: "Negócios",
        description: "Cursos e disciplinas relacionados à área de Negócios.",
        id: "business-filter",
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
      dataStatus: "educacao/dataStatus",
      storeDisciplines: "educacao/disciplines",
      isEmpty: "educacao/isEmpty",
      searchKeys: "educacao/searchKeys",
    }),
    disciplines: function () {
      return this.dataStatus == "ok" ? this.storeDisciplines : [];
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
          label: "Nível",
          items: [
            "Quero aprender!",
            "Tenho uma ideia, e agora?",
            "Preciso testar minha ideia!",
            "Tópicos avançados em Empreendedorismo",
          ],
          preSelected: this.queryParam ? this.queryParam.nivel : undefined,
        },
        {
          label: "Natureza",
          items: ["Graduação", "Pós-Graduação"],
          preSelected: this.queryParam ? this.queryParam.natureza : undefined,
        },
        {
          label: "Período de oferecimento",
          items: Array.from(
            this.disciplines.reduce((acc, discipline) => {
              const offeringPeriod = discipline.offeringPeriod;
              if (
                offeringPeriod &&
                !acc.has(offeringPeriod) &&
                offeringPeriod != "N/D"
              )
                acc.add(offeringPeriod);
              return acc;
            }, new Set())
          ),
          preSelected: this.queryParam ? this.queryParam.periodo : undefined,
        },
      ];
    },
    baseItems() {
      return this.filtered !== undefined ? this.filtered : this.disciplines;
    },
    displayItems() {
      return this.search.disciplines !== undefined
        ? this.search.disciplines
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
    const payload = { sheetsAPIKey: process.env.sheetsAPIKey };
    const route = this.$route;

    if (this.dataStatus == "ok" && this.disciplines.length == 0)
      this.$store.dispatch("educacao/fetchSpreadsheets", payload);

    if (route.params.id) {
      this.routeParam = this.disciplines.find(
        (discipline) => discipline._id.$oid == route.params.id
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
      setStrictResults: "global/setStrictResults",
      setFlexibleResults: "global/setFlexibleResults",
    }),
    fuzzySearch() {
      const searchResult = this.$fuzzySearch(
        removeAccent(this.search.term.trim()),
        this.baseItems,
        this.searchKeys
      );

      this.search.disciplines = searchResult;
    },
    filterData(context) {
      const campi = context.terciary[0];
      this.unities =
        campi != undefined
          ? this.$campi.find((c) => c.name == campi).unities
          : undefined;

      this.filtered = this.disciplines.filter((discipline) =>
        this.$disciplineMatchesFilter(discipline, context)
      );
    },
    async pipeline() {
      if (this.filters) this.filterData(this.filters);

      if (this.search.term && this.search.term.trim()) {
        this.$ga.event({
          eventCategory: "Educação",
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
.left-border {
  border-radius: 5px 0 0 5px;
}
.right-border {
  border-radius: 0 5px 5px 0;
}
</style>
