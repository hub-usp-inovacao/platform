<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="5">
        <List v-model="selected" :selected="selected" :items="items" />
      </v-col>
      <v-col cols="7">
        <DetailsCard :selected="selected" :items="items">
          <template v-slot:itemTitle>
            <slot name="itemTitle" :item="selected"></slot>
          </template>
          <template v-slot:content>
            <slot name="content" :item="selected"></slot>
          </template>
          <template v-slot:buttons>
            <slot name="buttons" :item="selected"></slot>
          </template>
        </DetailsCard>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import List from "@/components/deeper_levels/List.vue";
import DetailsCard from "@/components/deeper_levels/DetailsCard.vue";

export default {
  components: {
    List,
    DetailsCard,
  },
  props: {
    items: {
      type: Array,
      default: () => [],
    },
    selected: {
      type: Object,
      default: undefined,
    },
  },
  watch: {
    items() {
      if (this.items.length == 1) this.selected = this.items[0];
    },
  },
};
</script>
