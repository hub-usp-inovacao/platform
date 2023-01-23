<template>
  <v-container>
    <v-row>
      <v-col cols="8">
        <ShortTextInput :value="name" label="Nome do grupo" @input="setName" />
      </v-col>
      <v-col cols="4">
        <ShortTextInput
          :value="initials"
          label="Iniciais"
          @input="setInitials"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <Dropdown
          :value="bond"
          label="Qual seu vínculo com o grupo?"
          :options="bonds"
          @input="setBond"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <Dropdown
          :value="category"
          label="Em qual categoria este grupo se encaixa?"
          :options="categories"
          @input="setCategory"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <URLInput :value="url" label="Qual o site do grupo?" @input="setUrl" />
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

  // eslint-disable-next-line vue/require-prop-types
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
