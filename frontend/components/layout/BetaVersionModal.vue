<template>
  <v-dialog v-model="modal" :width="width">
    <v-card>
      <v-card-title class="text-h5 grey lighten-2">
        Site versão beta
      </v-card-title>

      <v-card-text style="max-width: 500px; margin: 0 auto; padding: 20px">
        Este site é uma versão beta de uso exclusivo da AUSPIN. Para acessar a
        versão aberta ao público, visite o
        <a :href="baseUrl">domínio oficial do Hub</a>. Nos botões abaixo, você
        deve escolher para qual versão deseja continuar.
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions>
        <v-spacer />
        <v-btn color="secondary" text @click="modal = false">
          {{ stayLabel }}
        </v-btn>
        <v-btn color="primary" :href="url">{{ redirectLabel }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: () => ({
    modal: true,
    path: "",
    baseUrl: "https://hubusp.inovacao.usp.br",
  }),
  computed: {
    url() {
      return this.baseUrl + this.path;
    },
    width() {
      return this.$vuetify.breakpoint.smAndDown ? "100%" : "50%";
    },
    stayLabel() {
      const long = "Ficar na versão beta (não recomendado)";
      const short = "Versão Beta";
      return this.$vuetify.breakpoint.smAndDown ? short : long;
    },
    redirectLabel() {
      const long = "Ir para a versão oficial";
      const short = "Versão Oficial";
      return this.$vuetify.breakpoint.smAndDown ? short : long;
    },
  },
  mounted() {
    this.path = this.$route.fullPath;
  },
};
</script>
