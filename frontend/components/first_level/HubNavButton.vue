<template>
  <nuxt-link to="/" class="d-flex align-center hub-nav-logo text-h6 font-weight-bold" style="color: #0c71b7;">
    HUB USP INOVAÇÃO
  </nuxt-link>
</template>

<script>
export default {
  name: "HubNavButton",
  props: {
    margin: { type: Boolean, default: true },
    background: { type: Boolean, default: true },
  },
};
</script>

<style scoped>
.hub-nav-logo {
  text-decoration: none;
  cursor: pointer;
  padding: 8px;
}
</style>
