<template>
  <v-app style="max-height: 100vh">
    <v-carousel
      cycle
      interval="3000"
      height="100%"
      hide-delimiters
      :show-arrows="false"
    >
      <v-carousel-item v-for="(photo, i) in photos" :key="i">
        <img style="min-width: 100%; min-height: 100%" :src="photo" />
      </v-carousel-item>
    </v-carousel>

    <div
      style="
        position: absolute;
        top: 0;
        height: 100%;
        width: 100%;
        overflow: auto;
      "
    >
      <v-row class="fill-height fit-page ma-0">
        <v-col
          style="background-color: rgba(255, 255, 255, 0.9)"
          cols="12"
          sm="10"
          md="7"
        >
          <v-container class="fill-height">
            <v-row class="mt-5" justify="center">
              <v-col cols="11" sm="10">
                <v-row>
                  <v-col cols="8" offset="1">
                    <v-img
                      contain
                      eager
                      aspect-ratio="1"
                      width="15rem"
                      class="mb-4"
                      :src="require('@/vectors/hub_logo.svg')"
                      alt="Hub USPInovação"
                    ></v-img>
                    <p
                      class="font-weight-medium ma-0"
                      :class="$breakpoint.smAndDown ? 'body-1' : 'title'"
                    >
                      Aqui você encontra as mais diversas informações sobre
                      empreendedorismo e inovação na Universidade de São Paulo
                    </p>
                  </v-col>
                </v-row>

                <v-row class="mt-4">
                  <v-col cols="10" offset="1">
                    <p
                      class="font-weight-light"
                      :class="$breakpoint.smAndDown ? 'body-2' : 'body-1'"
                    >
                      Você pode navegar usando as páginas específicas ou fazer
                      uma busca geral utilizando palavras-chave.
                    </p>

                    <v-text-field
                      v-model="search"
                      solo
                      flat
                      rounded
                      background-color="#ffcb96"
                      append-outer-icon="search"
                      :dense="$breakpoint.smAndDown"
                      :style="setSearchBarWidth"
                      @keydown.enter="submitSearch"
                    >
                      <template v-slot:label>
                        <p class="white--text font-weight-medium">Buscar</p>
                      </template>
                    </v-text-field>
                  </v-col>
                </v-row>
              </v-col>
            </v-row>

            <v-row>
              <v-col cols="11" sm="10" offset="1">
                <v-row>
                  <v-col cols="4" offset="1" md="2">
                    <v-img :src="require('@/vectors/logo_auspin.svg')"></v-img>
                  </v-col>
                  <v-col cols="4" offset="1" md="2">
                    <v-img :src="require('@/vectors/logo_usp.svg')"></v-img>
                  </v-col>
                </v-row>
              </v-col>
            </v-row>
          </v-container>
        </v-col>
      </v-row>
    </div>
  </v-app>
</template>

<script>
export default {
  data: () => ({
    search: "",
    photos: [
      require("@/assets/images/img01.jpg"),
      require("@/assets/images/img02.jpg"),
      require("@/assets/images/img03.jpg"),
      require("@/assets/images/img04.jpg"),
      require("@/assets/images/img05.jpg"),
      require("@/assets/images/img06.jpg"),
      require("@/assets/images/img07.jpg"),
      require("@/assets/images/img09.jpg"),
      require("@/assets/images/img10.jpg"),
      require("@/assets/images/img11.jpg"),
      require("@/assets/images/img12.jpg"),
      require("@/assets/images/img13.jpg"),
      require("@/assets/images/img14.jpg"),
      require("@/assets/images/img15.jpg"),
      require("@/assets/images/img16.jpg"),
      require("@/assets/images/img17.jpg"),
      require("@/assets/images/img18.jpg"),
      require("@/assets/images/img19.jpg"),
      require("@/assets/images/img20.jpg"),
      require("@/assets/images/img21.jpg"),
      require("@/assets/images/img22.jpg"),
      require("@/assets/images/img23.jpg"),
      require("@/assets/images/img24.jpg"),
      require("@/assets/images/img25.jpg"),
      require("@/assets/images/img26.jpg"),
      require("@/assets/images/img27.jpg"),
      require("@/assets/images/img28.jpg"),
      require("@/assets/images/img29.jpg"),
      require("@/assets/images/img30.jpg"),
      require("@/assets/images/img31.jpg"),
      require("@/assets/images/img33.jpg"),
      require("@/assets/images/img34.jpg"),
      require("@/assets/images/img35.jpg"),
      require("@/assets/images/img36.jpg"),
    ],
  }),
  computed: {
    setSearchBarWidth() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          return { width: "100%" };
        case "sm":
          return { width: "80%" };
        default:
          return { width: "60%" };
      }
    },
  },
  methods: {
    submitSearch() {
      this.$emit("search", this.search);
    },
  },
};
</script>

<style scoped>
.fit-page {
  max-width: 100%;
  max-height: 100%;
}
</style>
