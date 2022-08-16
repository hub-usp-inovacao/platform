<template>
  <v-container>
    <v-row>
      <v-col offset="1" cols="10">
        <v-card>
          <v-card-title class="title font-weight-bold mb-0"
            >Subáreas:</v-card-title
          >
          <v-card-subtitle>Você pode fazer múltiplas seleções</v-card-subtitle>
          <v-card-text class="d-flex flex-wrap justify-center">
            <v-chip-group v-model="selected" multiple :column="true">
              <v-chip
                v-for="(sub, i) of subareas"
                :key="i"
                outlined
                filter
                :value="sub"
                >{{ sub }}</v-chip
              >
            </v-chip-group>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  props: {
    subareas: {
      type: Array,
      required: true,
      validator(value) {
        return (
          value instanceof Array &&
          value.reduce((acc, el) => acc && typeof el === "string", true)
        );
      },
    },
  },
  data: () => ({
    selected: [],
  }),
  watch: {
    selected(s) {
      this.$emit("subfilter-change", s);
    },
  },
};
</script>
