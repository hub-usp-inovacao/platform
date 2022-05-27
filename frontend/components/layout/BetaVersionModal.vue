<template>
  <v-dialog
    v-model="modal"
    :width="$vuetify.breakpoint.smAndDown ? '100%' : '50%'"
  >
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
          Versão beta
        </v-btn>
        <v-btn color="primary" :href="url"> Versão oficial </v-btn>
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
    path: "",
  }),

  computed: {
    url() {
      return this.baseUrl + this.path;
    },
  },

  mounted() {
    this.path = this.$route.fullPath;
  },
};
</script>
