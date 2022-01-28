<template>
  <LastUpdated :label="label" :last-updated="lastUpdated">
    <v-file-input
      chips
      :label="label"
      :value="value"
      :hint="hint"
      persistent-hint
      accept="image/*"
      @change="handleImage"
    ></v-file-input>
    <v-img
      v-if="showImage"
      :src="imageUrl"
      max-width="250"
      max-height="250"
    ></v-img>
  </LastUpdated>
</template>

<script>
import LastUpdated from "@/components/CompanyForms/inputs/LastUpdated.vue";

export default {
  components: { LastUpdated },
  model: {
    prop: "value",
    event: "change",
  },
  props: {
    label: {
      type: String,
      required: true,
    },
    // eslint-disable-next-line vue/require-prop-types
    value: {
      required: false,
      default: undefined,
    },
    hint: {
      type: String,
      required: false,
      default: () => "",
    },
    lastUpdated: {
      type: String,
      required: false,
      default: undefined,
    },
  },
  data: () => ({
    image: undefined,
    imageUrl: undefined,
  }),
  computed: {
    showImage() {
      return this.image;
    },
  },
  methods: {
    handleImage(newImage) {
      this.image = newImage;
      this.imageUrl = newImage ? URL.createObjectURL(this.image) : undefined;
      this.$emit("input", this.image);
    },
  },
};
</script>
