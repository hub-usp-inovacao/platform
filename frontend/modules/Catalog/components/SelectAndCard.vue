<template>
  <v-row justify="center" class="ma-0">
    <v-col cols="11" sm="10">
      <v-card>
        <v-container>
          <v-select
            v-model="current_item"
            flat
            rounded
            filled
            hide-details
            menu-props="auto"
            color="#37474F"
            :items="items.map((it, i) => ({ text: it.name, value: i }))"
            no-data-text="Não encontramos nada"
            :label="`Selecione - ${groupName}`"
          >
            <template v-slot:selection="{ item, index }">
              <slot name="selection" :item="item" :index="index"></slot>
            </template>
          </v-select>
        </v-container>

        <div v-if="current_item >= 0">
          <slot name="item" :item="selectedItem"></slot>

          <v-card-actions>
            <slot name="buttons" :item="selectedItem">
              <v-spacer />
              <v-btn
                depressed
                dark
                color="rgb(255, 167, 38)"
                :href="selectedItem.url"
                >Saiba mais</v-btn
              >
              <v-spacer />
            </slot>
          </v-card-actions>
        </div>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  props: {
    items: {
      type: Array,
      required: true,
    },
    groupName: {
      type: String,
      default: () => "",
    },
    selected: {
      type: Object,
      default: undefined,
    },
  },
  data: () => ({
    current_item: -1,
  }),
  computed: {
    selectedItem: function () {
      if (this.current_item < 0) return null;
      return this.items[this.current_item];
    },
  },
  watch: {
    items() {
      this.current_item = -1;
    },
  },
  beforeMount() {
    if (this.selected) {
      this.current_item = this.items.findIndex(
        (item) => item.id === this.selected.id
      );
    }
  },
};
</script>
