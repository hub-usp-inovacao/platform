<template>
  <v-card
    class="mx-auto flex-grow-1 d-flex flex-column"
    max-width="300"
    min-height="400"
    hover
    nuxt
    :to="`/blog/${post.id}`"
  >
    <img
      v-if="thumbnailURL"
      :src="thumbnailURL"
      width="100%"
      class="white--text align-end"
    >
    </img>
    <img
      v-else
      :src="require('@/static/base_company_picture.png')"
      height="200px"
    >
    </img>

    <v-card-subtitle class="pb-0 secondary--text font-weight-bold">
      {{ formattedDate }}
    </v-card-subtitle>

    <v-card-title class="pt-2 headline font-weight-bold break-word">
      {{ post.title }}
    </v-card-title>

    <v-card-text class="text--primary flex-grow-1">
      <div class="mb-2 grey--text text--darken-1">
        <v-icon small class="mr-1">mdi-account</v-icon>
        {{ post.author }}
      </div>
      <div class="summary-text">{{ post.summary }}</div>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-btn color="secondary" text font-weight-bold>
        Ler mais
      </v-btn>
      <v-spacer></v-spacer>
      <v-icon color="secondary">mdi-chevron-right</v-icon>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: {
    post: {
      type: Object,
      required: true,
    },
  },
  computed: {
    thumbnailURL() {
      if(!this.post.thumbnail) return null;
      return `http://localhost:3002${this.post.thumbnail.sizes.thumbnail.url}`;
    },
    formattedDate() {
      if (!this.post.createdAt) return "";
      const date = new Date(this.post.createdAt);
      return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
      }).format(date);
    },
  },
};
</script>

<style scoped>
.break-word {
  word-break: break-word;
  line-height: 1.2;
}
.summary-text {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
