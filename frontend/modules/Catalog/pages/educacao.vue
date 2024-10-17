<template>
  <div>
    <div class="background">
      <Panel
        v-model="search.term"
        title="Educação"
        description="A USP oferece aos seus estudantes diversas disciplinas em nível de graduação e pós-graduação que se relacionam aos temas de Empreendedorismo e Inovação. Ao fazer uma busca, você encontrará as unidades, as condições de oferecimento, códigos e links para acesso às ementas nos sistemas institucionais, o Júpiter e o Janus."
        @clear="search.term = ''"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="tabs"
      :groups="groups"
      :colors="{ base: '#db8337', active: '#ab5307' }"
      @select="filters = $event"
    />

    <DisplayData
      :items="disciplines"
      group-name="Disciplinas"
      :has-image="false"
    >
      <template #title="{ item }">{{ item.name }}</template>
      <template #detailsText="{ item }">
        <v-container>
          <p class="body-2">{{ item.campus }}</p>
          <p class="body-2">{{ item.unity }}</p>
          <NDText :text="item.offeringPeriod" label=""/>
          <p class="body-2">{{ item.nature }}</p>
        </v-container>
      </template>
      <template #content="{ item }">
        <p>{{ item.description ? item.description : "" }}</p>
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
import Background from "../components/Background.vue";
import Panel from "../components/Panel.vue";
import MultipleFilters from "../components/MultipleFilters.vue";
import DisplayData from "../components/DisplayData.vue";
import NDText from "../components/NDText.vue";

import { debounce } from "debounce";

export default {
  components: {
    Panel,
    Background,
    MultipleFilters,
    DisplayData,
    NDText,
  },
  data: () => ({
    search: { term: "" },

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
    disciplines: [],
  }),
  computed: {
    groups() {
      return [
        {
          label: "Campus",
          items: this.$campi.map((c) => c.name),
        },
        {
          label: "Unidade",
          items: this.$campi
            .reduce((acc, value) => {
              return acc.concat(value.unities);
            }, [])
            .sort(),
        },
        {
          label: "Nível",
          items: [
            "Quero aprender!",
            "Tenho uma ideia, e agora?",
            "Preciso testar minha ideia!",
            "Tópicos avançados em Empreendedorismo",
          ],
        },
        {
          label: "Natureza",
          items: ["Graduação", "Pós-graduação"],
        },
        {
          label: "Período de Oferecimento",
          items: Array.from(
            this.disciplines.reduce((acc, discipline) => {
              const offeringPeriod = discipline.offeringPeriod;
              const hasPeriod = offeringPeriod && !acc.has(offeringPeriod) && offeringPeriod != "N/D"
              if (hasPeriod)
                acc.add(offeringPeriod);
              return acc;
            }, new Set())
          ),
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
        unity: this.filters?.terciary[1],
        level: this.filters?.terciary[2],
        nature: this.filters?.terciary[3],
        offeringPeriod: this.filters?.terciary[4],
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
    try {
      this.disciplines = await this.$DisciplineAdapter.requestData();
    } catch (error) {
      console.log(error);
    }
    if (this.$route.query.q !== undefined)
      this.search.term = this.$route.query.q;
  },
  methods: {
    async runSearch() {
      try {
        this.disciplines = await this.$DisciplineAdapter.filterData(
          this.queryParams
        );
      } catch (error) {
        console.log(error);
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
