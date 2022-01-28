<template>
  <v-container class="mt-10" width="90%">
    <v-row>
      <v-col cols="1">Linha</v-col>
      <v-col cols="3">Nome</v-col>
      <v-col cols="8">Possíveis problemas</v-col>
    </v-row>
    <div class="overflow">
      <v-row
        v-for="({ id, line, errors }, i) in errors"
        :key="id"
        :style="rowStyle(i)"
      >
        <v-col cols="1">{{ line }}</v-col>
        <v-col cols="3">{{ id }}</v-col>
        <v-col cols="3">
          <ul>
            <li v-for="e in errors" :key="e">{{ e }}</li>
          </ul>
        </v-col>
      </v-row>
    </div>
  </v-container>
</template>

<script>
export default {
  props: {
    errors: {
      type: Array,
      default: () => [],
    },
  },
  methods: {
    rowStyle(index) {
      return index % 2 == 0
        ? { backgroundColor: "#e0e0e0" }
        : { backgroundColor: "#fafafa" };
    },
  },
};
</script>

<style scoped>
.overflow {
  max-height: 80vh;
  overflow-y: auto;
}
</style>
