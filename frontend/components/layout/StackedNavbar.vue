<template>
  <div class="stacked-nav-container">
    <UniversalAuspinBar />
    <div class="nav-lower">
      <v-toolbar
        class="custom-hidden-sm-and-down bar"
        color="transparent"
        width="100%"
        flat
      >
        <HubNavButton class="ml-4" />
        <v-spacer></v-spacer>
        <div class="d-flex align-center" style="height: 100%;">
          <v-chip
            v-for="(item, i) in navItems"
            :key="i"
            :to="item.to"
            nuxt
            class="white--text mx-1 secondary px-4 subtitle-1"
          >
            {{ item.title }}
            <v-icon v-if="item.new" right>mdi-star</v-icon>
          </v-chip>
        </div>
      </v-toolbar>
    </div>
  </div>
</template>

<script>
import UniversalAuspinBar from "@/components/UniversalAuspinBar.vue";
import HubNavButton from "@/components/first_level/HubNavButton.vue";

export default {
  name: "StackedNavbar",
  components: {
    UniversalAuspinBar,
    HubNavButton,
  },
  props: {
    navItems: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.stacked-nav-container {
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.nav-lower {
  background-color: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}
</style>
