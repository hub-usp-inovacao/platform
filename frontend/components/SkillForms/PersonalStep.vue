<template>
  <v-container>
    <h2>Dados Pessoais</h2>
    <v-row>
      <v-col cols="4">
        <ShortTextInput :value="nusp" label="Nº USP" @input="setNusp" />
      </v-col>
      <v-col cols="8">
        <ShortTextInput :value="name" label="Nome" @input="setName" />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <Dropdown
          :value="bond"
          label="Vínculo com a Universidade"
          :options="bonds"
          @input="setBond"
        />
      </v-col>
      <v-col v-if="isTemp">
        <ShortTextInput
          :value="limitDate"
          label="Data limite do vínculo"
          @input="setLimitDate"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <ShortTextInput
          :value="phone"
          label="Telefone institucional"
          @input="setPhone"
        />
      </v-col>
      <v-col>
        <ShortTextInput
          :value="personalPhone"
          label="Telefone pessoal (não será divulgado no Hub)"
          @input="setPersonalPhone"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <URLInput
          :value="lattes"
          label="Link do curriculo lattes"
          @input="setLattes"
        />
      </v-col>
    </v-row>

    <h2>Atuação</h2>
    <v-row>
      <v-col>
        <Dropdown
          :value="major"
          label="Grande área (CNPq)"
          :options="majors"
          @input="setMajor"
        />
      </v-col>
      <v-col>
        <Dropdown
          :value="minors"
          label="Area (CNPq)"
          :options="allMinors"
          :multiple-option="true"
          @input="setMinors"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <MultipleInputs
          component="ShortTextInput"
          input-label="palavra-chave"
          :value="keywords"
          :limit="5"
          @input="setKeywords"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import ShortTextInput from "../CompanyForms/inputs/ShortTextInput.vue";
import Dropdown from "../CompanyForms/inputs/Dropdown.vue";
import MultipleInputs from "../CompanyForms/inputs/MultipleInputs.vue";
import URLInput from "../CompanyForms/inputs/URLInput.vue";

export default {
  components: {
    ShortTextInput,
    Dropdown,
    MultipleInputs,
    URLInput,
  },

  data: () => ({
    bonds: [
      "Docente",
      "Docente Sênior",
      "Funcionário",
      "Professor Contratado",
      "Pesquisador (Pós-Doutorando)",
      "PART (Programa de Atração e Retenção de Talentos)",
      "Aluno de Mestrado",
      "Aluno de Doutorado",
    ],
    areas: [],
  }),

  computed: {
    ...mapGetters({
      name: "skill_form/name",
      nusp: "skill_form/nusp",
      bond: "skill_form/bond",
      limitDate: "skill_form/limitDate",
      phone: "skill_form/phone",
      personalPhone: "skill_form/personalPhone",
      major: "skill_form/areaMajor",
      minors: "skill_form/areaMinors",
      keywords: "skill_form/keywords",
      lattes: "skill_form/lattes",
    }),
    isTemp() {
      return [
        "Professor Contratado",
        "Pesquisador (Pós-Doutorando)",
        "PART (Programa de Atração e Retenção de Talentos)",
        "Aluno de Mestrado",
        "Aluno de Doutorado",
      ].includes(this.bond);
    },
    majors() {
      return this.areas.map(({ name }) => name);
    },
    allMinors() {
      return this.areas.reduce((acc, { subareas }) => acc.concat(subareas), []);
    },
  },

  async beforeMount() {
    try {
      const { areas } = await this.$axios.$get(
        process.env.BACKEND_URL + "/areas"
      );
      this.areas = areas;
    } catch (error) {
      console.log(error);
    }
  },

  methods: {
    ...mapActions({
      setName: "skill_form/setName",
      setNusp: "skill_form/setNusp",
      setBond: "skill_form/setBond",
      setLimitDate: "skill_form/setLimitDate",
      setPhone: "skill_form/setPhone",
      setPersonalPhone: "skill_form/setPersonalPhone",
      setMajor: "skill_form/setAreaMajor",
      setMinors: "skill_form/setAreaMinors",
      setKeywords: "skill_form/setKeywords",
      setLattes: "skill_form/setLattes",
    }),
  },
};
</script>
