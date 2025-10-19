<template>
  <v-dialog v-model="show" persistent max-width="500">
    <v-card>
      <v-card-title class="text-h5">
        {{ title }}
      </v-card-title>
      <v-card-text>
        {{ message }}
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          :disabled="disableActions"
          color="green darken-1"
          text
          @click="$emit('ok-click')"
        >
          Ok!
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    show: Boolean,
    success: Boolean,
    title: {
      type: String,
      default: "",
    },
    message: {
      type: String,
      default: "",
    },
    disableActions: {
      type: Boolean,
      default: false,
    },
  },
};
</script>
