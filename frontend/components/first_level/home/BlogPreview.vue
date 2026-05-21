<template>
  <v-container v-if="posts.length > 0" class="py-12">
    <v-row>
      <v-col cols="12">
        <h2
          :class="$breakpoint.smAndDown ? 'headline' : 'display-1'"
          class="font-weight-medium mb-8"
	>
          Posts selecionados
        </h2>
      </v-col>
    </v-row>

    <v-slide-group
      class="py-4"
      show-arrows
    >
      <v-slide-item
        v-for="post in posts"
        :key="post.id"
      >
        <div class="mx-4 my-2" style="width: 300px">
          <PostCard :post="post" />
        </div>
      </v-slide-item>
    </v-slide-group>
  </v-container>
</template>

<script>
import PostCard from "@/components/blog/PostCard.vue";

export default {
  components: {
    PostCard,
  },
  data: () => ({
    posts: [],
    loading: true,
  }),
  async beforeMount() {
    try {
      this.posts = await this.$BlogAdapter.requestSelectedPosts();
    } catch (error) {
      console.error("Error fetching featured posts:", error);
    } finally {
      this.loading = false;
    }
  },
};
</script>
