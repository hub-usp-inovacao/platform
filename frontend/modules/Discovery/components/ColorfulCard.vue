<template>
  <div class="ma-8">
    <div
      class="circle rounded-circle white--text font-weight-black d-flex justify-center align-center ma-n4"
      :style="colorStyle"
    >
      {{ index }}
    </div>
    <v-card
      class="pt-6 d-flex justify-center align-center"
      elevation="10"
      height="8em"
      :style="colorStyle"
    >
      <v-card-title>
        <p
          class="font-weight-black text-h6 text-uppercase white--text text-center"
        >
          {{ title }}
        </p>
      </v-card-title>
    </v-card>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true,
    },
    index: {
      type: Number,
      required: true,
    },
    color: {
      type: String,
      required: true,
    },
  },
  computed: {
    colorStyle() {
      return { backgroundColor: this.color };
    },
  },
};
</script>

<style scoped>
.circle {
  border: 0.25em solid white;

  width: 3em;
  height: 3em;

  z-index: 2;

  position: absolute;
}
</style>
