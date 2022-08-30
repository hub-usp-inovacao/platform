<template>
  <div>
    <p v-if="!textIsEmpty" :class="classes">
      <strong v-if="label">{{ label }}:</strong>
      {{ text }}
    </p>

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
    label: {
      type: String,
      default: undefined,
    }
  },
  watch: {
    text() {
      if (this.textIsEmpty) 
        this.$emit("empty")
    }
  },
  computed: {
    textIsEmpty() {
      return this.text == "N/D"
    }
  }
};
</script>