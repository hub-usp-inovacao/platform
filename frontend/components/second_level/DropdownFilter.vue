<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <p class="font-weight-medium">Filtros:</p>
      </v-col>
    </v-row>

    <v-row class="hidden-sm-and-down">
      <v-col v-for="({ label, items }, i) of groups" :key="i" :cols="colSize">
        <v-autocomplete
          v-model="selected[i]"
          :no-data-text="noData"
          menu-props="auto"
          :label="label"
          :items="items"
          clearable
          :filter="customFilter"
          autocomplete="disable"
        >
          <template v-if="label == 'Porte'" #prepend>
            <v-tooltip top>
              <template v-slot:activator="{ on, attrs }">
                <v-btn icon v-bind="attrs" v-on="on">
                  <v-icon>info</v-icon>
                </v-btn>
              </template>
              <table>
                <tr>
                  <th>Porte</th>
                  <th>Indústria de Transformação</th>
                  <th>Outros</th>
                </tr>
                <tr>
                  <td>Microempresas</td>
                  <td>&le; 19 pessoas</td>
                  <td>&le; 9 pessoas</td>
                </tr>
                <tr>
                  <td>Pequena Empresa</td>
                  <td>de 20 a 99 pessoas</td>
                  <td>de 10 a 49 pessoas</td>
                </tr>
                <tr>
                  <td>Média Empresa</td>
                  <td>de 100 a 499 pessoas</td>
                  <td>de 50 a 99 pessoas</td>
                </tr>
                <tr>
                  <td>Grande Empresa</td>
                  <td>&ge; 500 pessoas</td>
                  <td>&ge; 100 pessoas</td>
                </tr>
              </table>
            </v-tooltip>
          </template>
        </v-autocomplete>
      </v-col>
    </v-row>

    <v-row
      v-for="({ label, items }, i) of groups"
      :key="i"
      class="hidden-md-and-up"
    >
      <v-col cols="12">
        <v-select
          v-model="selected[i]"
          :no-data-text="noData"
          menu-props="auto"
          :label="label"
          :items="items"
          clearable
        ></v-select>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { removeAccent } from "@/lib/format";

export default {
  props: {
    groups: {
      type: Array,
      required: true,
      validator(value) {
        return (
          value instanceof Array &&
          value.reduce(
            (acc, el) =>
              acc && el.label && el.items && el.items.length !== undefined,
            true
          )
        );
      },
    },
  },
  data: () => ({
    selected: [],
    noData: "Indisponível",
  }),
  computed: {
    colSize() {
      const nGroups = this.groups.length;

      return nGroups > 4 ? Math.floor(12 / nGroups) : 3;
    },
  },
  watch: {
    selected(list) {
      console.log(list);
      this.$emit("select", list);
    },
  },
  beforeMount() {
    this.groups.forEach(({ preSelected }, i) => {
      if (preSelected) {
        this.selected[i] = preSelected;
      }
    });

    if (this.selected.length > 0) {
      this.$emit("select", this.selected);
    }
  },
  methods: {
    customFilter(_item, queryText, itemText) {
      if (itemText.indexOf(queryText) > -1) return true;

      const inspectQuery = removeAccent(queryText.toLowerCase());
      const inspectItem = removeAccent(itemText.toLowerCase());
      return inspectItem.indexOf(inspectQuery) > -1;
    },
  },
};
</script>
<style scoped>
table {
  text-align: center;
}
th,
td {
  padding: 10px;
}
</style>
