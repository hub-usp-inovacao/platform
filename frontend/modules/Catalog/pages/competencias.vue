<template>
  <div style="min-height: 100vh">
    <div class="background">
      <Panel
        title="Competências"
        description="Nesta seção, você pode consultar quais as competências dos pesquisadores da USP, quem são e como contatá-los. A Plataforma Hub USPInovação utiliza como parâmetro de divisão de competências a Tabela das Áreas do Conhecimento apresentada pelo CNPq, e divide-as em dois níveis principais correspondentes, respectivamente, à área do conhecimento (ex.: Ciências Exatas e da Terra) e sua subárea (ex.: Matemática)."
        url="https://docs.google.com/forms/d/e/1FAIpQLSc-OmhsvBSUDBvx6uR6cvI6zq01M-_7JqdX4ktcB9mLE3oWzw/viewform"
        forms-call="Cadastre suas competências"
        @search="search.term = $event"
        @clear="search.skills = undefined"
      />
    </div>

    <Background class="absolute" />

    <MultipleFilters
      :tabs="tabs"
      :colors="{ active: '#9b4c68', base: '#6b1c28' }"
      :groups="groups"
      @select="filters = $event"
    />

    <DisplayData :items="researchers" group-name="Pesquisador">
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
          v-if="item.photo"
          :key="item.photo"
          :lazy-src="require('@/static/base_skill_picture.png')"
          :src="item.photo"
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
import { debounce } from "debounce";

import Background from "../components/Background.vue";
import DisplayData from "../components/DisplayData.vue";
import MultipleFilters from "../components/MultipleFilters.vue";
import Panel from "../components/Panel.vue";

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
    unities: undefined,
    researchers: [],
  }),
  computed: {
    bonds() {
      return this.researchers.reduce((acc, researcher) => {
        return acc.includes(researcher.bond)
          ? acc
          : acc.concat(researcher.bond);
      }, []);
    },
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
            this.unities === undefined
              ? this.$campi
                  .reduce((acc, value) => {
                    return acc.concat(value.unities);
                  }, [])
                  .sort()
              : this.unities,
        },
        {
          label: "Vínculo do Pesquisador",
          items: this.bonds,
        },
      ];
    },
    searchTerm() {
      return this.search.term;
    },
  },
  watch: {
    filters: debounce(async function () {
      const params = {
        areaMajors: this.filters.primary,
        areaMinors: this.filters.secondary,
        campus: this.filters.terciary[0],
        unity: this.filters.terciary[1],
        bond: this.filters.terciary[2],
      };

      try {
        this.researchers = await this.$ResearcherAdapter.filterData(params);
      } catch (error) {
        console.log(error);
      }
    }, 1000),
  },
  async beforeMount() {
    this.researchers = await this.$ResearcherAdapter.requestData();
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
