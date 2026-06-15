<template>
  <v-container class="fill-height" style="min-height: 60vh;">
    <v-row class="w-100 align-center justify-center text-center pt-16">
      <v-col cols="11" md="10" lg="8" class="d-flex flex-column align-center">
        <h1 class="text-h4 text-md-h3 font-weight-bold mb-4">
          Tudo sobre empreendedorismo e inovação na USP, em um só lugar
        </h1>
        <p class="text-subtitle-1 text-md-h5 font-weight-light mb-8">
          Navegue pelas páginas específicas ou faça uma busca geral
        </p>

        <v-text-field
          v-model="search"
          solo
          flat
          rounded
          background-color="#ffcb96"
          append-outer-icon="search"
          :dense="$vuetify.breakpoint.smAndDown"
          class="search-bar"
          @keydown.enter="submitSearch"
        >
          <template v-slot:label>
            <p class="white--text font-weight-medium">Buscar</p>
          </template>
        </v-text-field>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    search: "",
  }),
  methods: {
    submitSearch() {
      this.$emit("search", this.search);
    },
  },
};
</script>

<style scoped>
.search-bar {
  width: 90%;
}

@media (min-width: 600px) {
  .search-bar {
    width: 80%;
  }
}

@media (min-width: 960px) {
  .search-bar {
    width: 60%;
  }
}
</style>
