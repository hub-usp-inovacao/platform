<template>
  <v-container>
    <h2>Institucional</h2>
    <v-row>
      <v-col>
        <Dropdown
          :multiple-option="true"
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
    <MultipleInputs
      component="GroupInput"
      input-label="grupo de pesquisa"
      :value="groups"
      :limit="3"
      @input="setGroups"
    />
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import Dropdown from "../CompanyForms/inputs/Dropdown.vue";
import MultipleInputs from "../CompanyForms/inputs/MultipleInputs.vue";

export default {
  components: {
    Dropdown,
    MultipleInputs,
  },
  data: () => ({
    allUnities: [],
    allCampi: [],
  }),

  computed: {
    ...mapGetters({
      unities: "skill_form/unities",
      campus: "skill_form/campus",
      groups: "skill_form/groups",
    }),
  },

  beforeMount() {
    this.loadUnities();
    this.loadCampi();
  },

  methods: {
    ...mapActions({
      setUnities: "skill_form/setUnities",
      setCampus: "skill_form/setCampus",
      setGroups: "skill_form/setGroups",
    }),
    async loadUnities() {
      try {
        const { unities } = await this.$axios.$get(
          process.env.BACKEND_URL + "/unities"
        );
        this.allUnities = unities;
      } catch (error) {
        console.log(error);
      }
    },
    async loadCampi() {
      try {
        const { campi } = await this.$axios.$get(
          process.env.BACKEND_URL + "/campi"
        );
        this.allCampi = campi;
      } catch (error) {
        console.log(error);
      }
    },
  },
};
</script>
