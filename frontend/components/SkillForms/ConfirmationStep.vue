<template>
  <v-container>
    <v-row>
      <v-col>
        <h2>Você tem interesse em participar de P&amp;D&amp;I?</h2>
        <Dropdown :value="interest" :options="interests" @input="setInterest" />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <h2>Declaração de Responsabilidade</h2>
        <BooleanInput
          :value="truthful"
          :label="truthfulStatement"
          @input="setTruthful"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <h2>Permissão</h2>
        <BooleanInput
          :value="allow"
          :label="allowingStatement"
          @input="setAllow"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import Dropdown from "../CompanyForms/inputs/Dropdown.vue";
import BooleanInput from "../CompanyForms/inputs/BooleanInput.vue";

export default {
  components: {
    Dropdown,
    BooleanInput,
  },
  data: () => ({
    truthfulStatement:
      "Declaro que todas as informações fornecidas são verdadeiras",
    allowingStatement:
      "Permito que os dados informados sejam utilizados no Hub USP Inovação",
    interests: [
      "Não tenho interesse",
      "Tenho pouco interesse",
      "Tenho interesse",
    ],
  }),
  computed: {
    ...mapGetters({
      interest: "skill_form/interest",
      allow: "skill_form/allow",
      truthful: "skill_form/truthful",
    }),
  },
  methods: {
    ...mapActions({
      setInterest: "skill_form/setInterest",
      setAllow: "skill_form/setAllow",
      setTruthful: "skill_form/setTruthful",
    }),
  },
};
</script>
