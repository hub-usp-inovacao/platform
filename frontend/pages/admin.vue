<template>
  <v-container class="mt-8" style="max-width: 600px">
    <h1 class="text-h4 mb-6">Admin - Data Management</h1>

    <v-card>
      <v-card-title>Refresh Spreadsheet Data</v-card-title>
      <v-card-text>
        <p>
          Reload all entity data (Companies, PDIs, Disciplines, Researchers,
          Initiatives, Patents) from Google Sheets into the database.
        </p>
        <p class="caption grey--text">
          The refresh runs in the background. Validation errors are emailed to
          the dev team automatically.
        </p>
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="primary"
          :loading="loading"
          :disabled="loading"
          @click="refreshAll"
        >
          Refresh All Data
        </v-btn>
      </v-card-actions>
    </v-card>

    <v-snackbar v-model="snackbar" :color="snackbarColor" :timeout="5000">
      {{ snackbarText }}
    </v-snackbar>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    loading: false,
    snackbar: false,
    snackbarText: "",
    snackbarColor: "success",
  }),
  methods: {
    async refreshAll() {
      this.loading = true;
      try {
        const url = process.env.BACKEND_URL + "/catalog/refresh";
        const resp = await fetch(url, { method: "POST" });
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        this.snackbarColor = "success";
        this.snackbarText =
          "Refresh started! Data will be updated in the background.";
      } catch (error) {
        console.error("Refresh failed:", error);
        this.snackbarColor = "error";
        this.snackbarText = "Failed to start refresh. Check the server logs.";
      } finally {
        this.loading = false;
        this.snackbar = true;
      }
    },
  },
};
</script>
