<template>
  <LastUpdated :label="label" :last-updated="lastUpdated">
    <v-textarea
      :value="value"
      :clearable="clearable"
      rows="2"
      :label="label"
      :hint="hint"
      persistent-hint
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
    clearable: {
      type: Boolean,
      required: false,
      default: true,
    },
    value: {
      type: String,
    },
    label: {
      type: String,
      required: true,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
    lastUpdated: {
      type: String,
      default: undefined,
    },
  },
};
</script>
