<template>
  <div>
    <div v-if="displayLastUpdated" class="mb-8 mt-2" @click="touched = true">
      <p>
        <span>{{ label }}:</span> {{ lastUpdatedMessage }}
      </p>
    </div>
    <div v-else>
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    label: {
      type: String,
      required: true,
    },
    lastUpdated: {
      type: String,
      default: undefined,
    },
  },

  data: () => ({
    touched: false,
  }),

  computed: {
    displayLastUpdated() {
      return this.lastUpdated !== undefined && !this.touched;
    },
    lastUpdatedMessage() {
      return `Última atualização em ${this.lastUpdated}`;
    },
  },
};
</script>
