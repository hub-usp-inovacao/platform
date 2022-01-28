<template>
  <footer class="text-h6 bg-black d-flex flex-column align-center">
    <v-container fluid>
      <v-row>
        <v-col cols="12" lg="6" offset-lg="3">
          <v-row justify="center">
            <p class="text-center my-8">Conheça também nossas redes sociais!</p>
          </v-row>

          <v-row justify="center">
            <v-spacer class="hidden-sm-and-down"></v-spacer>
            <v-col
              v-for="{ label, url } in icons"
              :key="label"
              justify="center"
              cols="2"
              class="d-flex justify-center"
            >
              <v-btn dark icon :href="url" target="_blank">
                <v-icon size="35px">{{ label }}</v-icon>
              </v-btn>
            </v-col>
            <v-spacer class="hidden-sm-and-down"></v-spacer>
          </v-row>

          <v-row justify="center">
            <p class="text-center my-8">
              (11) 3091-4166 hubusp.inovacao@usp.br
            </p>
          </v-row>

          <v-row justify="center">
            <v-col cols="4" md="1" lg="2" offset-lg="0">
              <v-img :src="require('@/vectors/codelab-ccsl.svg')"></v-img>
            </v-col>
            <v-col cols="4" md="1" offset-md="1" lg="2" offset-lg="1">
              <v-img :src="require('@/vectors/auspin-white.svg')"></v-img>
            </v-col>
            <v-col cols="4" md="1" offset-md="1" lg="2" offset-lg="1">
              <v-img :src="require('@/vectors/usp-white.svg')"></v-img>
            </v-col>
          </v-row>

          <v-row justify="center">
            <p>{{ new Date().getFullYear() }}</p>
          </v-row>
        </v-col>

        <v-col class="hidden-md-and-down" cols="0" align="center">
          <img
            :src="require('@/assets/images/hub-logo.png')"
            alt="Plataforma Hub USPInovação"
            class="mt-8"
          />
        </v-col>
      </v-row>
    </v-container>
  </footer>
</template>

<script>
export default {
  data: () => ({
    icons: [
      {
        label: "mdi-facebook",
        url: "https://www.facebook.com/uspinovacao1",
        color: "rgba(59,89,153 ,1 )",
      },
      {
        label: "mdi-twitter",
        url: "https://twitter.com/uspinovacao",
        color: "#00aced",
      },
      {
        label: "mdi-linkedin",
        url: "https://www.linkedin.com/company/uspinovacao",
        color: "#0e76a8",
      },
      {
        label: "mdi-instagram",
        url: "http://www.instagram.com/uspinovacao",
        color: "#C13584",
      },
      {
        label: "mdi-youtube",
        url: "https://www.youtube.com/channel/UCD7crX4uDNU-4Hqh-rYgwiQ",
        color: "#C13584",
      },
    ],
  }),
};
</script>

<style scoped>
footer {
  margin: 0;
  padding: 2rem;
  width: 100%;
  min-height: 20vh;
}

.bg-black {
  background-color: #212121;
  color: #f9f9f9;
}
</style>
