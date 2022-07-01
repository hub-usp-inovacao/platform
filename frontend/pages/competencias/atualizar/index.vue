<template>
  <section>
    <div class="background">
      <Panel :title="title" :description="description()" no-search />
    </div>

    <v-form>
      <v-container>
        <v-row v-if="ok">
          <Stepper @finish="submitUpdate()" />
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
    ok: false,
    token: "",
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
    async submitUpdate() {
      this.submitUpdateData();
    },
  },
};
</script>
