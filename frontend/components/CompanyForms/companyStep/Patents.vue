<template>
  <div>
    <MultipleInputs
      :value="patents"
      component="PatentNameAndCode"
      input-label="patente"
      @input="setPatents"
    />
  </div>
</template>

<script>
import MultipleInputs from "../inputs/MultipleInputs.vue";
import { mapGetters, mapActions } from "vuex";

export default {
  components: {
    MultipleInputs,
  },
  computed: {
    ...mapGetters({
      patents: "company_forms/patents",
    }),
  },
  methods: {
    ...mapActions({
      setPatents: "company_forms/setPatents",
    }),
  },
};
</script>
