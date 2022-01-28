<template>
  <div>
    <v-card :style="cardStyle">
      <v-card-title
        >Resultados de busca para "{{ searchedTerm }}":</v-card-title
      >
      <v-card-text>
        <v-list v-if="filtered.length > 0">
          <v-list-item-group v-model="selected">
            <v-list-item
              v-for="(item, index) in filtered"
              :key="index"
              three-line
            >
              <v-list-item-content :class="contentMarginStyle">
                <v-list-item-title class="font-weight-bold item-title">{{
                  item.name
                }}</v-list-item-title>
                <v-list-item-subtitle class="hidden-sm-and-down">{{
                  item.description
                }}</v-list-item-subtitle>
              </v-list-item-content>
              <v-list-item-action>
                <v-chip>{{ item.category }}</v-chip>
              </v-list-item-action>
            </v-list-item>
          </v-list-item-group>
        </v-list>
        <p v-else>Nenhum resultado a exibir</p>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  props: {
    searchedTerm: {
      type: String,
      default: () => "",
    },
    filtered: {
      type: Array,
      default: () => [],
    },
  },
  data: () => ({
    selected: null,
  }),
  computed: {
    contentMarginStyle() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
        case "sm":
          return "";
        default:
          return "ml-2";
      }
    },
    cardStyle() {
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
        case "sm":
          return;
        default:
          return {
            overflowY: "auto",
            height: "35rem",
          };
      }
    },
    routeName() {
      const item = this.filtered[this.selected];
      switch (item.category) {
        case "Educação":
          return "educacao";
        case "P&D&I":
          return "inovacao";
        case "Competências":
          return "competencias";
        default:
          return item.category.toLowerCase();
      }
    },
  },
  watch: {
    selected() {
      const item = this.filtered[this.selected];
      this.$router.push({
        name: this.routeName,
        params: { id: item.id },
      });
    },
  },
};
</script>

<style>
.item-title {
  white-space: normal;
}
</style>
