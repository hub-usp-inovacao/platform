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
    },
    async requestSelectedPosts() {
      try {
        // We use depth=2 to reach the thumbnail object inside featuredPosts
        // Multi-level select ensures we only get the fields required for the cards
        const fields = [
          "select[featuredPosts]=true",
          "populate[posts][title]=true",
          "populate[posts][author]=true",
          "populate[posts][summary]=true",
          "populate[posts][createdAt]=true",
          "populate[posts][thumbnail][sizes][thumbnail][url]=true",
          "depth=2"
        ].join("&");
        
        const { featuredPosts } = await axios.$get(`${apiUrl}/globals/home-settings?${fields}`);
        return featuredPosts || [];
      } catch (error) {
        console.error("Error fetching selected posts:", error);
        return [];
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
  
  // TODO: Create a method for getting the selected posts. They are within a payloadCMS global named HomeConfig. The file example_selected_posts.json has an example of the returned json from the url http://localhost:3002/api/globals/home-settings?depth=2&draft=false&locale=undefined&trash=false. Select only the needed information from this endpoint using url queries

  const adapter = new BlogAdapter(context.$axios, fetchCmsUrl, publicCmsUrl);
  inject("BlogAdapter", adapter); 
}
