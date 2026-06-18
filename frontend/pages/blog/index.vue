<template>
  <div style="min-height: 100vh">
    <div class="background">
      <Panel
        v-model="search"
        title="Blog"
        description="Fique por dentro das novidades, eventos e artigos sobre o ecossistema de inovação e empreendedorismo da USP."
        no-search
      />
    </div>

    <Background class="absolute" />

    <v-container class="d-flex flex-wrap justify-center" style="gap: 16px">
      <div v-if="loading" justify="center" align="center" style="min-height: 40vh">
        <v-progress-circular indeterminate color="secondary" size="64"></v-progress-circular>
      </div>

      <div
        v-else-if="filteredPosts.length > 0"
        v-for="post in filteredPosts"
        :key="post.id"
        class="d-flex"
      >
        <PostCard :post="post" />
      </div>

      <div v-else class="text-center">
        <v-icon size="64" color="grey lighten-1">mdi-post-outline</v-icon>
        <p class="text-h6 mt-4 grey--text">Nenhum post encontrado no momento.</p>
      </div>

      <div class="w-100 d-flex justify-center mt-6 mb-6" v-if="totalPages > 1" style="width: 100%;">
        <v-pagination
          v-model="page"
          :length="totalPages"
          @input="fetchPosts"
          color="secondary"
        ></v-pagination>
      </div>
    </v-container>
  </div>
</template>

<script>
import Panel from "@/modules/Catalog/components/Panel.vue";
import Background from "@/modules/Catalog/components/Background.vue";
import PostCard from "@/components/blog/PostCard.vue";

export default {
  components: {
    Panel,
    Background,
    PostCard,
  },
  data: () => ({
    search: "",
    posts: [],
    loading: true,
    page: 1,
    totalPages: 0,
  }),
  computed: {
    filteredPosts() {
      if (!this.search) return this.posts;
      const term = this.search.toLowerCase();
      return this.posts.filter(
        (post) =>
          post.title.toLowerCase().includes(term) ||
          post.summary.toLowerCase().includes(term) ||
          post.author.toLowerCase().includes(term)
      );
    },
  },
  async beforeMount() {
    await this.fetchPosts();
  },
  methods: {
    async fetchPosts() {
      this.loading = true;
      try {
        const response = await this.$BlogAdapter.requestPosts(this.page, 5);
        this.posts = response.docs || [];
        this.totalPages = response.totalPages || 0;
        this.page = response.page || 1;
      } catch (error) {
        console.error("Erro ao buscar posts:", error);
        this.posts = [];
        this.totalPages = 0;
      } finally {
        this.loading = false;
      }
    },
  },
  head() {
    return {
      title: "Blog - Hub USPInovação",
    };
  },
};
</script>

<style scoped>
.absolute {
  position: absolute;
}
</style>
