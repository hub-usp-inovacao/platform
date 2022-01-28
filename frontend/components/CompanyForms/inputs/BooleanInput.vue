<template>
  <v-switch
    :v-model="booleanSwitch"
    :label="label"
    :color="color"
    :value="value"
    @change="$emit('input', $event)"
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
};
</script>
