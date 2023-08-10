<template>
  <div>
    <v-text-field
      type="text"
      :label="label"
      :value="value"
      :hint="hint"
      :rules="rules"
      @input="$emit('input', $event)"
    />
  </div>
</template>

<script>
export default {
  props: {
    label: {
      type: String,
      required: true,
    },
    value: {
      type: String,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
  },
  data: () => ({
      rules: [
	(v) => /^\d*$/.test(v) || "Esse campo precisa ser um número",
      ],
  }),
};
</script>

<style>
/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"] {
  -moz-appearance: textfield;
}
</style>

