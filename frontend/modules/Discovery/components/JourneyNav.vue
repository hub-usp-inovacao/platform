<template>
  <div class="d-flex justify-space-around">
    <nuxt-link :to="previous.to" class="icon-link" :style="previousStyle">
      <v-icon :color="previousColor">{{ previousIcon }}</v-icon>
      {{ previous.label }}
    </nuxt-link>
    <nuxt-link :to="next.to" class="icon-link" :style="nextStyle">
      <v-icon :color="nextColor">{{ nextIcon }}</v-icon>
      {{ next.label }}
    </nuxt-link>
  </div>
</template>

<script>
export default {
  props: {
    previous: {
      type: Object,
      required: true,
    },
    next: {
      type: Object,
      required: true,
    },
    nextColor: {
      type: String,
      default: undefined,
    },
    previousColor: {
      type: String,
      default: undefined,
    },
  },

  computed: {
    previousIcon() {
      return this.previous.to === "/jornada"
        ? "mdi-circle-outline"
        : "mdi-arrow-left";
    },
    nextIcon() {
      return this.next.to === "/jornada"
        ? "mdi-circle-outline"
        : "mdi-arrow-right";
    },
    nextStyle() {
      return {
        color: this.nextColor,
      };
    },
    previousStyle() {
      return {
        color: this.previousColor,
      };
    },
  },
};
</script>

<style scoped>
.icon-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
}
</style>
