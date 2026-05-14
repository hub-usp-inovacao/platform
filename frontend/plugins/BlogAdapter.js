function BlogAdapter(axios, fetchCmsUrl, publicCmsUrl) {
  const apiUrl = `${fetchCmsUrl}/api`;

  return {
    async requestPosts() {
      try {
        const query = "select[content]=false&select[content_html]=false";
        const { docs } = await axios.$get(`${apiUrl}/posts?${query}`);
        return docs || [];
      } catch (error) {
        return [];
      }
    },
    async requestPostById(id) {
      try {
        return await axios.$get(`${apiUrl}/posts/${id}`);
      } catch (error) {
        return null;
      }
    },
    async getMediaThumbnailURL(mediaID) {
      try {
        const result = await axios.$get(`${apiUrl}/media/${mediaID}`);
        return `${publicCmsUrl}${result.sizes.thumbnail.url}` || "";
      } catch (error) {
        return "";
      }
    }
  }
}

export default (context, inject) => {
  const publicCmsUrl = process.env.CMS_URL || "http://localhost:3002";
  let fetchCmsUrl = publicCmsUrl;
  
  if (process.server) {
    // For SSR inside docker, the CMS might be on a different internal URL.
    fetchCmsUrl = process.env.INTERNAL_CMS_URL || "http:payload:3000//";
  }

  const adapter = new BlogAdapter(context.$axios, fetchCmsUrl, publicCmsUrl);
  inject("BlogAdapter", adapter); 
}
