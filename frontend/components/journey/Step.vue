<template>
  <main class="bg-gray">
    <Header :title="title" :next-color="nextColor" :color="color" />
    <v-container class="hidden-sm-and-down">
      <v-row>
        <v-col cols="4">
          <v-row>
            <v-col class="description-lines">
              <p v-for="item in description" :key="item">{{ item }}</p>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <JourneyNav
                :next="nextLink"
                :previous="previousLink"
                :next-color="nextColor"
                :previous-color="previousColor"
              />
            </v-col>
          </v-row>
        </v-col>
        <v-col cols="8">
          <slot name="PrimaryButtons"></slot>
          <slot name="SecondaryButtons"></slot>
          <ListJourney :items="items" />
        </v-col>
      </v-row>
    </v-container>

    <v-container class="hidden-md-and-up">
      <v-row>
        <v-col class="description-lines">
          <p v-for="item in description" :key="item">{{ item }}</p>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <slot name="PrimaryButtons"></slot>
          <slot name="SecondaryButtons"></slot>
          <ListJourney :items="items" />
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <JourneyNav
            :next="nextLink"
            :previous="previousLink"
            :next-color="nextColor"
            :previous-color="previousColor"
          />
        </v-col>
      </v-row>
    </v-container>
  </main>
</template>

<script>
import JourneyNav from "@/components/journey/JourneyNav.vue";
import Header from "@/components/journey/Header.vue";
import ListJourney from "@/components/journey/ListJourney.vue";

export default {
  components: {
    Header,
    JourneyNav,
    ListJourney,
  },
  props: {
    items: {
      type: Array,
      required: true,
      default: () => [],
    },
    title: {
      type: String,
      required: true,
    },
    description: {
      type: Array,
      required: true,
    },
    color: {
      type: String,
      required: true,
    },
    buttons: {
      required: true,
      validator: (prop) => {
        return prop instanceof Array || prop instanceof Object;
      },
    },
    next: {
      type: String,
      required: true,
    },
    previous: {
      type: String,
      default: "",
    },
    nextColor: {
      type: String,
      default: "#212121",
    },
    previousColor: {
      type: String,
      default: "#212121",
    },
  },

  computed: {
    nextLink() {
      if (this.next) {
        return { label: "Avançar", to: this.next };
      }

      return { label: "Voltar ao menu", to: "/jornada" };
    },

    previousLink() {
      if (this.previous) {
        return { label: "Voltar", to: this.previous };
      }

      return { label: "Voltar ao menu", to: "/jornada" };
    },
  },
};
</script>

<style scoped>
.description-lines {
  overflow: auto;
  height: 600px;
}

.bg-gray {
  background-color: #ececec;
}
</style>
