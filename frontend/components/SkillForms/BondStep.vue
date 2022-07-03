<template>
  <v-container>
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
  }),

  computed: {
    ...mapGetters({
      unities: "skill_form/unities",
    }),
  },
  methods: {
    ...mapActions({
      setUnities: "skill_form/setUnities",
    }),
  },

  async beforeMount() {
    try {
      const { unities } = await this.$axios.$get(
        "http://localhost:3001/unities"
      );
      this.allUnities = unities;
    } catch (error) {
      console.log(error);
    }
  },
};
</script>