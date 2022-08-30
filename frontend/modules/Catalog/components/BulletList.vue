<template>
  <v-list>
    <v-subheader>{{ title }}</v-subheader>
    <v-list-item v-for="(item, index) in items" :key="index">
      <v-list-item-icon class="mr-2">
        <v-icon v-text="'mdi-circle-small'"></v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <NDText classes="mb-0" :text="item"/>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
import NDText from "../components/NDText.vue";

export default {
  components: {
    NDText,
  },
  props: {
    title: {
      type: String,
      required: true,
    },
    items: {
      type: Array,
      default: () => [],
    },
  },
  computed: {
    groupDataCY() {
      return `${this.title}-bullet-list`;
    },
  },
};
</script>
