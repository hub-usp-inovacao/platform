<template>
  <PairOfShortTextInput
    label-one="Nome"
    label-two="Código"
    @input="$emit('input', $event)"
  />
</template>

<script>
import PairOfShortTextInput from "./PairOfShortTextInput.vue";

export default {
  components: {
    PairOfShortTextInput,
  },
};
</script>
