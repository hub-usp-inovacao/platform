<template>
  <div :class="wrapperClasses">
    <div>{{ caption }}</div>
    <v-btn
      class="mt-4 mt-lg-0"
      elevation="0"
      color="transparent"
      height="5em"
      :to="to"
    >
      <svg
        width="5em"
        height="2em"
        viewBox="0 0 82 52"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M80.4749 28.4749C81.8417 27.108 81.8417 24.892 80.4749 23.5251L58.201 1.25126C56.8342 -0.115572 54.6181 -0.115572 53.2513 1.25126C51.8844 2.6181 51.8844 4.83418 53.2513 6.20101L73.0503 26L53.2513 45.799C51.8844 47.1658 51.8844 49.3819 53.2513 50.7487C54.6181 52.1156 56.8342 52.1156 58.201 50.7487L80.4749 28.4749ZM0 29.5H78V22.5H0V29.5Z"
          :fill="color"
        />
      </svg>
    </v-btn>
  </div>
</template>

<script>
export default {
  props: {
    caption: {
      type: String,
      required: true,
    },
    color: {
      type: String,
      required: true,
    },
    to: {
      type: String,
      required: true,
    },
  },

  computed: {
    wrapperClasses() {
      const bpName = this.$vuetify.breakpoint.name;
      const base = [
        "py-8",
        "px-4",
        "d-flex",
        "align-center",
        "flex-column",
        "flex-lg-row",
      ];

      return bpName === "sm" || bpName === "xs"
        ? base.concat(["grayscale-vert"])
        : base.concat(["grayscale-hor"]);
    },
  },
};
</script>

<style scoped>
.grayscale-vert {
  background-image: linear-gradient(to bottom, #ececec, #fcfcfc);
}

.grayscale-hor {
  background-image: linear-gradient(to right, #ececec, #fcfcfc);
}
</style>
