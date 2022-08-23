<template>
  <div>
    <p v-if="text !== 'N/D'" :class="classes">{{ text }}</p>
  </div>
</template>

<script>
export default {
  props: {
    text: {
      type: String,
      default: "",
    },
    classes: {
      type: String,
      default: "",
    },
  },
};
</script>