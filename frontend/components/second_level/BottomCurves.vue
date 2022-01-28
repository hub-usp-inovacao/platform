<template>
  <div>
    <svg
      :width="width"
      :height="lightHeight"
      viewBox="0 0 1276 388"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M463 161L0 0V388H1276L878 274L799 228L639 137L463 161Z"
        :fill="lightColor"
        fill-opacity="0.34"
      />
    </svg>

    <svg
      :width="width"
      :height="darkHeight"
      viewBox="0 0 1180 234"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M427 102L0 0V234H1180L843 177L603 66L427 102Z"
        :fill="darkColor"
      />
    </svg>
  </div>
</template>

<script>
export default {
  props: {
    tone: {
      type: String,
      default: "blue",
    },
  },
  data: () => ({
    windowWidth: 0,
  }),
  computed: {
    width: function () {
      return this.windowWidth;
    },
    lightHeight: function () {
      return (388 / 1276) * this.width;
    },
    darkHeight: function () {
      return (234 / 1180) * this.width;
    },
    lightColor() {
      return this.tone == "blue" ? "#4AD4FF" : "#FFB74A";
    },
    darkColor() {
      return this.tone == "blue" ? "#108CB3" : "#B37210";
    },
  },
  mounted() {
    this.windowWidth = window.innerWidth;
    window.addEventListener("resize", () => {
      this.windowWidth = window.innerWidth;
    });
  },
};
</script>

<style scoped>
svg {
  position: absolute;
  bottom: 0;
  display: block;
}
</style>
