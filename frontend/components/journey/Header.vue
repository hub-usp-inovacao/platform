<template>
  <v-row>
    <v-col>
      <v-row :class="roadmap">
        <v-col md="6" sm="12">
          <div
            :style="headerStyle"
            class="py-4 text-center white--text font-weight-bold text-h5 rounded-lg"
          >
            {{ title }}
          </div>
        </v-col>
        <template class="hidden-sm-and-down">
          <v-col v-if="nextColor != '#212121'" offset="5" cols="1">
            <div :style="peakabooStyle" class="rounded-lg"></div>
          </v-col>
          <v-col v-else cols="6">
            <Party style="height: 60%; max-height: 60%" />
          </v-col>
        </template>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import Party from "@/components/journey/Party.vue";

export default {
  components: {
    Party,
  },
  props: {
    title: {
      type: String,
      required: true,
    },
    nextColor: {
      type: String,
      required: true,
    },
    color: {
      type: String,
      required: true,
    },
  },
  computed: {
    roadmap() {
      return this.nextColor === "#212121" || this.$breakpoint.smAndDown
        ? []
        : ["roadmap"];
    },

    headerStyle() {
      return {
        backgroundColor: this.color,
      };
    },

    peakabooStyle() {
      return {
        height: "100%",
        backgroundColor: this.nextColor,
      };
    },
  },
};
</script>
<style scoped>
.roadmap {
  background-image: url("/hor-road.png");
  background-position: left center;
  background-repeat: repeat-x;
}
</style>
