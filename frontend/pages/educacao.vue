<template>
  <div>
    <div class="background">
      <Panel
        title="Educação"
        description="A USP oferece aos seus estudantes diversas disciplinas em nível de graduação e pós-graduação que se relacionam aos temas de Empreendedorismo e Inovação. Ao fazer uma busca, você encontrará as unidades, as condições de oferecimento, códigos e links para acesso às ementas nos sistemas institucionais, o Júpiter e o Janus."
        v-model="search.term"
        @clear="search.disciplines = undefined"
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
import { mapGetters } from "vuex";

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
    disciplines: [],
  }),
  computed: {
    ...mapGetters({
      dataStatus: "educacao/dataStatus",
      storeDisciplines: "educacao/disciplines",
      searchKeys: "educacao/searchKeys",
    }),
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
          items: ["Graduação", "Pós-Graduação"],
        },
      ];
    },
  },
  watch: {
    async filters() {
      const params = {
        categories: this.filters.primary,
        campus: this.filters.terciary[0],
        unity: this.filters.terciary[1],
        level: this.filters.terciary[2],
        nature: this.filters.terciary[3],
      };

      this.disciplines = await this.$DisciplineAdapter.filterData(params);
    },
  },
  async beforeMount() {
    this.disciplines = await this.$DisciplineAdapter.requestData();
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
