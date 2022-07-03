<template>
  <v-container>
    <h2>Institucional</h2>
    <v-row>
      <v-col>
        <Dropdown
          :multipleOption="true"
          :options="allUnities"
          label="Vinculado a quais unidades?"
          :value="unities"
          @input="setUnities"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <Dropdown
          :options="allCampi"
          label="Vinculado a qual campus?"
          :value="campus"
          @input="setCampus"
        />
      </v-col>
    </v-row>

    <h2>Grupos</h2>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import Dropdown from "../CompanyForms/inputs/Dropdown.vue";

export default {
  components: {
    Dropdown,
  },
  data: () => ({
    allUnities: [],
    allCampi: [],
  }),

  computed: {
    ...mapGetters({
      unities: "skill_form/unities",
      campus: "skill_form/campus",
    }),
  },

  methods: {
    ...mapActions({
      setUnities: "skill_form/setUnities",
      setCampus: "skill_form/setCampus",
    }),
    async loadUnities() {
      try {
        const { unities } = await this.$axios.$get(
          "http://localhost:3001/unities"
        );
        this.allUnities = unities;
      } catch (error) {
        console.log(error);
      }
    },
    async loadCampi() {
      try {
        const { campi } = await this.$axios.$get("http://localhost:3001/campi");
        this.allCampi = campi;
      } catch (error) {
        console.log(error);
      }
    },
  },

  beforeMount() {
    this.loadUnities();
    this.loadCampi();
  },
};
</script>