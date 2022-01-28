<template>
  <v-card tile flat>
    <v-container
      style="height: 100%"
      class="d-flex flex-column justify-space-around"
    >
      <nuxt-link :to="item.dest">
        <v-row align="center" class="pb-0">
          <v-col cols="4" lg="3" offset-lg="1">
            <img width="100%" :src="require(`@/vectors/${item.iconSrc}`)" />
          </v-col>
          <v-col>
            <v-row>
              <v-col>
                <v-card-title
                  class="text-left font-weight-regular secondary--text py-0"
                >
                  {{ item.title }}
                </v-card-title>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-card-text
                  class="text-left font-weight-light black--text py-0"
                >
                  {{ item.description }}
                </v-card-text>
              </v-col>
            </v-row>
          </v-col>
        </v-row>
      </nuxt-link>
      <v-row v-if="hasCallToAction" align="start">
        <v-col cols="12" offset-lg="4" lg="8" class="py-0">
          <v-card-actions>
            <v-btn
              rounded
              width="100%"
              class="py-6 font-weight-bold"
              color="secondary"
              target="_blank"
              :href="item.formsURL"
            >
              {{ item.formsCall }}
            </v-btn>
          </v-card-actions>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
  computed: {
    hasCallToAction() {
      const { formsURL, formsCall } = this.item;

      return (
        formsURL !== undefined &&
        formsURL !== "" &&
        formsCall !== undefined &&
        formsCall !== ""
      );
    },
    cardHeight() {
      if (this.hasCallToAction) {
        return "20rem";
      } else {
        return "17rem";
      }
    },
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
}
</style>
