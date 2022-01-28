<template>
  <div>
    <LastUpdated :label="label" :last-updated="lastUpdated">
      <v-text-field
        v-mask="mask"
        :label="label"
        type="text"
        :rules="rules.input"
        :value="value"
        :disabled="disabled"
        :hint="hint"
        persistent-hint
        @input="$emit('input', $event)"
      />
    </LastUpdated>
  </div>
</template>

<script>
import LastUpdated from "@/components/CompanyForms/inputs/LastUpdated.vue";

export default {
  components: {
    LastUpdated,
  },

  props: {
    label: {
      type: String,
      required: true,
    },
    rule: {
      type: RegExp,
      required: false,
      default: () => /.*/,
    },
    mask: {
      type: String,
      default: "",
    },
    value: {
      type: String,
    },
    disabled: {
      type: Boolean,
      required: false,
      default: () => false,
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

  computed: {
    rules() {
      return {
        input: [(f) => this.rule.test(f) || "Formato inválido"],
      };
    },
  },
};
</script>
