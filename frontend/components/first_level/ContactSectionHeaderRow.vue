<template>
  <v-row class="ma-0 mt-12" align="center" justify="space-around" no-gutters>
    <v-col>
      <v-row class="ma-0" justify="center">
        <v-col cols="12" sm="9">
          <p class="display-1 font-weight-regular">
            {{ section }}
          </p>
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  props: {
    section: {
      type: String,
      required: true,
    },
  },
};
</script>
