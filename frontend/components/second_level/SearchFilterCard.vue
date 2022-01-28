<template>
  <div>
    <v-card class="hidden-sm-and-down" min-height="18rem">
      <v-card-title>Filtros:</v-card-title>
      <v-card-text>
        <v-chip-group v-model="selected" :column="true" :multiple="true">
          <v-chip
            v-for="category in categories"
            :key="category"
            :value="category"
            filter
            outlined
            >{{ category }}</v-chip
          >
        </v-chip-group>
      </v-card-text>
    </v-card>
    <v-card class="hidden-md-and-up mb-4">
      <v-card-title>Filtros:</v-card-title>
      <v-card-text>
        <v-chip-group v-model="selected" :column="true" :multiple="true">
          <v-chip
            v-for="category in categories"
            :key="category"
            :value="category"
            filter
            outlined
            >{{ category }}</v-chip
          >
        </v-chip-group>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  props: {
    categories: {
      type: Array,
      default: () => [],
    },
  },
  data: () => ({
    selected: [],
  }),
  watch: {
    selected() {
      this.$emit("select", this.selected);
    },
  },
};
</script>
