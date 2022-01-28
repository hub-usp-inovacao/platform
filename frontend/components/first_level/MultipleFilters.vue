<template>
  <v-container>
    <v-row>
      <v-col>
        <CardButton
          :pre-selected-tabs="preSelectedTabs"
          :tabs="tabs"
          :color="colors.base"
          :active="colors.active"
          @select="selected.tabs = $event"
        />
      </v-col>
    </v-row>
    <v-row v-if="needSubfilters">
      <v-col>
        <ChipSubfilter
          :subareas="availableSubareas"
          @subfilter-change="selected.subfilters = $event"
        />
      </v-col>
    </v-row>
    <v-row v-if="needDropdown">
      <v-col>
        <DropdownFilter
          :groups="groups"
          @select="selected.dropdownFilters = $event"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import CardButton from "@/components/second_level/CardButton.vue";
import ChipSubfilter from "@/components/second_level/ChipSubfilter.vue";
import DropdownFilter from "@/components/second_level/DropdownFilter.vue";

export default {
  components: {
    CardButton,
    ChipSubfilter,
    DropdownFilter,
  },
  props: {
    tabs: {
      type: Array,
      required: true,
      validator(givenTabs) {
        return (
          givenTabs instanceof Array &&
          givenTabs.reduce((acc, tab) => {
            const tabKeys = Object.keys(tab);

            if (!tabKeys.includes("subareas"))
              return acc && tabKeys.includes("name");

            return (
              acc &&
              tabKeys.includes("name") &&
              tabKeys.includes("subareas") &&
              tab.subareas instanceof Array &&
              tab.subareas.reduce(
                (acc, sa) => acc && typeof sa === "string",
                true
              )
            );
          }, true)
        );
      },
    },
    groups: {
      type: Array,
      default: () => [],
      validator(givenGroups) {
        return (
          givenGroups instanceof Array &&
          givenGroups.reduce(
            (acc, { label, items }) =>
              label &&
              typeof label === "string" &&
              items instanceof Array &&
              items.every((item) => typeof item === "string"),
            true
          )
        );
      },
    },
    colors: {
      type: Object,
      required: true,
      validator(colors) {
        const { base, active } = colors;
        return (
          base !== undefined &&
          typeof base === "string" &&
          active !== undefined &&
          typeof active === "string"
        );
      },
    },
    preSelectedTabs: {
      type: Array,
      required: false,
      default: () => [],
    },
  },
  data: () => ({
    selected: {
      tabs: [],
      subfilters: [],
      dropdownFilters: [],
    },
  }),
  computed: {
    availableSubareas() {
      return this.tabs
        .filter(({ name }) => this.selected.tabs.includes(name))
        .reduce((acc, { subareas }) => acc.concat(subareas), []);
    },
    needSubfilters() {
      return this.tabs.some((tab) => tab.subareas !== undefined);
    },
    needDropdown() {
      return this.groups !== undefined && this.groups.length > 0;
    },
    sTabs() {
      return this.selected.tabs;
    },
    sSubfilters() {
      return this.selected.subfilters;
    },
    sDropdownFilters() {
      return this.selected.dropdownFilters;
    },
    context() {
      return {
        primary: this.selected.tabs,
        secondary: this.selected.subfilters,
        terciary: this.selected.dropdownFilters,
      };
    },
  },
  watch: {
    sTabs() {
      this.$emit("select", this.context);
    },
    sSubfilters() {
      this.$emit("select", this.context);
    },
    sDropdownFilters() {
      this.$emit("select", this.context);
    },
  },
};
</script>
