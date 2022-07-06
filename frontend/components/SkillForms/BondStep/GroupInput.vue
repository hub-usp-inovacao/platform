<template>
  <v-container>
    <v-row>
      <v-col cols="8">
        <ShortTextInput :value="name" @input="setName" label="Nome do grupo" />
      </v-col>
      <v-col cols="4">
        <ShortTextInput
          :value="initials"
          @input="setInitials"
          label="Iniciais"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <Dropdown
          :value="bond"
          @input="setBond"
          label="Qual seu vínculo com o grupo?"
          :options="bonds"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <Dropdown
          :value="category"
          @input="setCategory"
          label="Em qual categoria este grupo se encaixa?"
          :options="categories"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <URLInput :value="url" @input="setUrl" label="Qual o site do grupo?" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import ShortTextInput from "../../CompanyForms/inputs/ShortTextInput.vue";
import Dropdown from "../../CompanyForms/inputs/Dropdown.vue";
import URLInput from "../../CompanyForms/inputs/URLInput.vue";

export default {
  components: {
    ShortTextInput,
    Dropdown,
    URLInput,
  },

  props: ["value"],

  data: () => ({
    bonds: ["Coordenador(a)", "Pesquisador(a)", "Outro"],
    categories: [
      "CEPID",
      "EMBRAPII",
      "INCT",
      "Laboratório",
      "Centro de Engenharia FAPESP",
      "Outro",
    ],
  }),

  computed: {
    name() {
      return this.value.name;
    },
    initials() {
      return this.value.initials;
    },
    bond() {
      return this.value.bond;
    },
    category() {
      return this.value.category;
    },
    url() {
      return this.value.url;
    },
  },

  methods: {
    setName(newName) {
      this.$emit("input", { ...this.value, name: newName });
    },
    setInitials(newInitials) {
      this.$emit("input", { ...this.value, initials: newInitials });
    },
    setBond(newBond) {
      this.$emit("input", { ...this.value, bond: newBond });
    },
    setCategory(newCategory) {
      this.$emit("input", { ...this.value, category: newCategory });
    },
    setUrl(newUrl) {
      this.$emit("input", { ...this.value, url: newUrl });
    },
  },
};
</script>