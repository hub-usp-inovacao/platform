<template>
  <section>
    <div class="background">
      <Panel :title="title" :description="description()" no-search />
    </div>

    <v-form>
      <v-container>
        <v-row v-if="ok">
          <Stepper @finish="submitUpdate()" />
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
              :disabled="!isValid"
              @click="submitToken"
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
import { mapActions } from "vuex";
import Panel from "@/components/first_level/Panel.vue";
import Stepper from "@/components/SkillForms/Stepper.vue";

export default {
  components: {
    Panel,
    Stepper,
  },
  data: () => ({
    title: "Atualização de Competências",
    Nome: "",
    skill: {
      nome: "",
    },
    ok: true,
    token: "",
    dialog: {
      show: false,
      title: "",
      status: "",
      message: "",
    },
  }),

  computed: {
    isValid() {
      return this.token.length > 0;
    },
  },

  methods: {
    ...mapActions({
      submitUpdateData: "skill_form/submitUpdateData",
    }),

    description() {
      return "Nesta página os pesquisadores da USP cadastrados no HUBUSP INOVAÇÃO, podem pedir alteração de seus dados.";
    },
    async submitToken() {
      const body = { skill: { token: this.token } };
      const backendUrl = process.env.BACKEND_URL;
      const skill = await this.$axios.$post(backendUrl + "/skills", body);
      console.log(skill);
    },
    submitUpdate() {
      try {
        this.submitUpdateData();
      } catch (error) {
        this.dialog.status = "error";
        this.dialog.title = "Erro no formulário";
        this.dialog.message =
          "Veracidade das informações deve ter sido confirmada";
        this.dialog.show = true;
      }
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
