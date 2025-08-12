<template>
  <v-dialog persistent max-width="500">
    <v-card>
      <v-card-title class="text-h5">
        {{ title }}
      </v-card-title>
      <v-card-text>
        {{ message }}
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green darken-1" text @click="onClose">
          Ok!
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    title: String,
    message: String,
    success: Boolean,
    onClose: Function
  },
  methods: {
    onClose() {
      if (this.success) {
        this.$router.push("/");
      }
    }
  },
};
</script>
