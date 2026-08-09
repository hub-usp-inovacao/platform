<template>
  <v-switch
    :v-model="booleanSwitch"
    :label="label"
    :color="color"
    :value="value"
    @change="onChange"
  >
  </v-switch>
</template>

<script>
export default {
  props: {
    value: {
      type: Boolean,
      default: false,
    },
    label: {
      type: String,
      required: true,
    },

    color: {
      type: String,
      default: "blue",
    },
  },

  data: () => ({
    booleanSwitch: false,
  }),

  methods: {
    onChange() {
      this.$emit("input", this.booleanSwitch);
    },
  },
};
</script>
