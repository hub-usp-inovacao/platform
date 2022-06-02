<template>
  <section>
    <div class="background">
      <Panel :title="title" :description="description()" no-search />
    </div>

    <v-form ref="form">
      <v-container>
        <v-row v-if="ok">
          <v-dialog v-model="dialog.show" persistent max-width="500">
            <v-card>
              <v-card-title class="text-h5">
                {{ dialog.title }}
              </v-card-title>
              <v-card-text>
                {{ dialog.message }}
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="green darken-1" text @click="handleOkClick">
                  Ok!
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
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
              :loading="loading"
              :disabled="!isValid"
              @click="submit"
            >
              Enviar
            </v-btn>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12" offset-md="3" md="6" align="center">
            <p>
              Ainda não solicitou uma atualização?
              <nuxt-link to="/competencias/atualizar/solicitar">
                Solicite aqui
              </nuxt-link>
            </p>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
  </section>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Panel from "@/components/first_level/Panel.vue";

export default {
  components: {
    Panel,
  },
  data: () => ({
    title: "Atualização de Competências",

    loading: false,
    ok: false,
    dialog: {
      show: false,
      status: "",
      title: "",
      message: "",
    },

    token: "",
  }),

  computed: {
    ...mapGetters({
      errors: "skill_forms/errors",
    }),

    isValid() {
      return this.token.length > 0;
    },
  },

  mounted() {
    const token = this.$route.query.token;

    if (token !== undefined) {
      this.token = token;
    }
  },

  methods: {

    description() {
      if (this.ok == false) {
        return "Cadastrados nas páginas de competências podem solicitar alteração de seus dados nesta página. Insira a palavra-passe (token) ou acesse diretamente pelo link enviado por email.";
      } else {
        return "";
      }
    },

    async submit() {
      if (this.isValid) {
        this.setEmail(this.token);
        this.loading = true;
        await this.getSkillData();
        this.loading = false;

        if (this.errors.length === 0) {
          this.ok = true;
        } else {
          alert(this.errors);
        }
      }
    },

    async updateSkill() {
    },

    clearDialog() {
      this.dialog = {
        show: false,
        status: "",
        title: "",
        message: "",
      };
    },

    handleOkClick() {
      const { status } = this.dialog;
      this.clearDialog();

      if (status === "success") {
        this.$router.push("/");
      }
    },
  },
};
</script>
