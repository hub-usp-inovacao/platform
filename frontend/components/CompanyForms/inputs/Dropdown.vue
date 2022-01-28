<template>
  <LastUpdated :label="label" :last-updated="lastUpdated">
    <v-select
      :items="options"
      :value="value"
      :label="label"
      clearable
      :disabled="disabled"
      :multiple="multipleOption"
      @input="$emit('input', $event)"
    />
  </LastUpdated>
</template>

<script>
import LastUpdated from "@/components/CompanyForms/inputs/LastUpdated.vue";

export default {
  components: {
    LastUpdated,
  },
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
    multipleOption: {
      type: Boolean,
      default: false,
    },
    options: {
      type: Array,
      required: true,
    },
    value: {
      required: false,
    },
    label: {
      type: String,
      default: "",
    },
    lastUpdated: {
      type: String,
      default: undefined,
    },
  },
};
</script>
