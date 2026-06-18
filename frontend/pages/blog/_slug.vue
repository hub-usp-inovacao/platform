<template>
  <div style="min-height: 100vh">
    <div class="background">
      <Panel
        :title="post ? post.title : 'Blog'"
        :description="post ? post.summary : ''"
        no-search
      />
    </div>

    <Background class="absolute" />

    <v-container class="mt-8">
      <v-row v-if="loading" justify="center" align="center" style="min-height: 40vh">
        <v-progress-circular indeterminate color="secondary" size="64"></v-progress-circular>
      </v-row>

      <v-row v-else-if="post">
        <v-col cols="12" md="8" offset-md="2">
          <v-card flat color="transparent">
            <v-img
              v-if="thumbnailURL"
              :src="thumbnailURL"
              height="400px"
              class="rounded-lg mb-6"
            ></v-img>

            <div class="d-flex align-center mb-4 grey--text text--darken-1">
              <v-icon small class="mr-2">mdi-account</v-icon>
              <span class="mr-6">{{ post.author }}</span>
              <v-icon small class="mr-2">mdi-calendar</v-icon>
              <span>{{ formattedDate }}</span>
            </div>

            <div class="blog-content" v-html="post.content_html"></div>

            <v-divider class="my-8"></v-divider>

            <v-btn text color="secondary" to="/blog" exact prefetch>
              <v-icon left>mdi-arrow-left</v-icon>
              Voltar para o Blog
            </v-btn>
          </v-card>
        </v-col>
      </v-row>

      <v-row v-else justify="center" align="center" style="min-height: 40vh">
        <v-col class="text-center">
          <v-icon size="64" color="grey lighten-1">mdi-alert-circle-outline</v-icon>
          <p class="text-h6 mt-4 grey--text">Post não encontrado.</p>
          <v-btn color="secondary" to="/blog" class="mt-4">Ver todos os posts</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Panel from "@/modules/Catalog/components/Panel.vue";
import Background from "@/modules/Catalog/components/Background.vue";

export default {
  components: {
    Panel,
    Background,
  },
  async asyncData({ params, $BlogAdapter }) {
    try {
      const post = await $BlogAdapter.requestPostBySlug(params.slug);
      return { post, loading: false };
    } catch (error) {
      console.error("Erro ao carregar o post:", error);
      return { post: null, loading: false };
    }
  },
  data: () => ({
    post: null,
    loading: true,
    thumbnailURL: null,
    cmsBaseUrl: process.env.CMS_URL || "",
    pageBaseUrl: process.env.BACKEND_URL || "",
  }),
  computed: {
    formattedDate() {
      if (!this.post || !this.post.createdAt) return "";
      const date = new Date(this.post.createdAt);
      return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
      }).format(date);
    },
  },
  async created() {
    if (this.post && this.post.thumbnail) {
      if (typeof this.post.thumbnail === "string") {
        this.thumbnailURL = await this.$BlogAdapter.getMediaThumbnailURL(
          this.post.thumbnail
        );
      } else if (
        this.post.thumbnail.sizes &&
        this.post.thumbnail.sizes.full
      ) {
        this.thumbnailURL = this.$BlogAdapter.getAbsoluteImageUrl(this.post.thumbnail.sizes.full.url);
      } else if (this.post.thumbnail.url) {
        this.thumbnailURL = this.$BlogAdapter.getAbsoluteImageUrl(this.post.thumbnail.url);
      }
    }
  },
  head() {
    return {
      title: this.post ? `${this.post.title} - Blog Hub USPInovação` : "Blog - Hub USPInovação",
    };
  },
};
</script>

<style scoped>
.absolute {
  position: absolute;
}
.blog-content {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #333;
}
.blog-content >>> h1, .blog-content >>> h2, .blog-content >>> h3 {
  margin-top: 2rem;
  margin-bottom: 1rem;
  color: #108CB3;
}
.blog-content >>> p {
  margin-bottom: 1.5rem;
}
.blog-content >>> img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 2rem 0;
}
</style>
