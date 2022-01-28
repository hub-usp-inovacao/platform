<template>
  <v-card
    height="35rem"
    class="d-flex flex-column"
    :class="{ 'justify-center': items.length == 0 }"
  >
    <div v-if="items.length > 0">
      <div class="d-flex justify-center">
        <v-icon large>keyboard_arrow_up</v-icon>
      </div>

      <v-list rounded height="30rem" max-height="100%" style="overflow-y: auto">
        <v-list-item-group>
          <v-list-item
            v-for="i in items"
            :key="i.nome"
            :value="i"
            :href="i.url == 'N/D' ? '' : i.url"
            target="_blank"
          >
            <v-list-item-content>
              <v-list-item-title v-text="i.nome" />
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>

      <div class="d-flex justify-center">
        <v-icon large>keyboard_arrow_down</v-icon>
      </div>
    </div>
    <div v-else>
      <p class="title font-weight-light text-center mb-0">
        Selecione os filtros acima
      </p>
    </div>
  </v-card>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      default: () => {},
    },
  },
};
</script>

<style></style>
