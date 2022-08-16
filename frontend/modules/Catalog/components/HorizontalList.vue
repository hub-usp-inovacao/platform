<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <span>{{ title }}</span>
      </v-col>
    </v-row>
    <v-row>
      <v-col v-for="item in items" :key="item" cols="4" md="6">
        <span class="black--text text-caption">{{ item }}</span>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      default: () => [],
    },
    title: {
      type: String,
      required: true,
    },
  },
};
</script>
