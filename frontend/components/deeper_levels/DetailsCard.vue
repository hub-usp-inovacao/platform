<template>
  <div>
    <v-card v-if="hasItem" height="35rem" class="d-flex flex-column">
      <v-card-title>
        <slot name="itemTitle"></slot>
      </v-card-title>
      <v-card-subtitle>
        <v-row v-if="selected.keywords && selected.keywords.length > 0">
          <v-col>
            <v-chip
              v-for="kw in selected.keywords"
              :key="kw"
              class="mr-2 mb-2"
              >{{ kw }}</v-chip
            >
          </v-col>
        </v-row>
      </v-card-subtitle>
      <v-card-text style="overflow-y: auto">
        <slot name="content"></slot>
      </v-card-text>
      <v-card-actions class="d-flex justify-space-around">
        <slot name="buttons"></slot>
      </v-card-actions>
    </v-card>
    <v-card v-else height="35rem" class="d-flex flex-column justify-center">
      <NotSelected :size="items.length"></NotSelected>
    </v-card>
  </div>
</template>

<script>
import NotSelected from "@/components/deeper_levels/NotSelected.vue";

export default {
  components: {
    NotSelected,
  },
  props: {
    selected: {
      type: Object,
      default: undefined,
    },
    items: {
      type: Array,
      default: () => [],
    },
  },
  computed: {
    hasItem() {
      return this.selected !== null && this.selected !== undefined;
    },
  },
};
</script>
