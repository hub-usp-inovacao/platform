<template>
  <section>
    <div class="background">
      <Panel :title="title" :description="description()" no-search />
    </div>

    <v-form>
      <v-container>
        <v-row v-if="ok">
          <!-- aqui vem o formulário em si -->
        </v-row>
        <v-row v-else>
          <v-col cols="10">
            <v-text-field v-model="token" label="token" />
          </v-col>
          <v-col cols="2" align="bottom">
            <v-btn
              color="secondary"
              width="100"
              text
              :disabled="!isValid"
              @click="submit"
            >
              Enviar
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </section>
</template>

<script>
import Panel from "@/components/first_level/Panel.vue";

export default {
  components: {
    Panel,
  },
  data: () => ({
    title: "Atualização de Competências",
    Nome: "",
    skill: {
      nome: "",
    },
    ok: false,
    token: "",
  }),

  computed: {
    isValid() {
      return this.token.length > 0;
    },
  },

  methods: {
    description() {
      return "Nesta página os pesquisadores da USP cadastrados no HUBUSP INOVAÇÃO, podem pedir alteração de seus dados.";
    },
    async submit() {
      const body = { skill: { token: this.token } };
      const backendUrl = process.env.BACKEND_URL;
      const skill = await this.$axios.$post(backendUrl + "/skills", body);
      console.log(skill);
    },
  },
};
</script>
