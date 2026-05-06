function BlogAdapter(axios) {
  const baseURL = process.env.CMS_URL;

  return {
    async requestPosts() {
      try {
        const { docs } = await axios.$get(`${baseURL}/posts`);
        return docs || [];
      } catch (error) {
        return [];
      }
    },
    async getMediaThumbnailURL(mediaID) {
      try {
        const result = await axios.$get(`${process.env.CMS_URL}/media/${mediaID}`);
        return `localhost:3002/${result.thumbnail.url}` || "";
      } catch (error) {
        return "";
      }
    }
  }
}

export default (context, inject) => {
  const adapter = new BlogAdapter(context.$axios);
  inject("BlogAdapter", adapter); 
}
