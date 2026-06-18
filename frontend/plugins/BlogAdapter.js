function BlogAdapter(axios, fetchCmsUrl, publicCmsUrl) {
  const apiUrl = `${fetchCmsUrl}/api`;

  return {
    async requestPosts(page = 1, limit = 5) {
      try {
        const query = `select[content]=false&select[content_html]=false&page=${page}&limit=${limit}`;
        const response = await axios.$get(`${apiUrl}/posts?${query}`);
        return response;
      } catch (error) {
        return { docs: [], totalPages: 0, page: 1 };
      }
    },
    async requestPostById(id) {
      try {
        return await axios.$get(`${apiUrl}/posts/${id}`);
      } catch (error) {
        return null;
      }
    },
    async requestPostBySlug(slug) {
      try {
        const query = `where[slug][equals]=${slug}`;
        const response = await axios.$get(`${apiUrl}/posts?${query}`);
        if (response.docs && response.docs.length > 0) {
          return response.docs[0];
        }
        return null;
      } catch (error) {
        return null;
      }
    },
    getAbsoluteImageUrl(path) {
      if (!path) return "";
      if (path.startsWith('http')) return path;
      try {
        const urlObj = new URL(publicCmsUrl);
        if (urlObj.pathname !== '/' && path.startsWith(urlObj.pathname)) {
          return `${urlObj.origin}${path}`;
        }
      } catch (e) {}
      return `${publicCmsUrl}${path}`;
    },
    async getMediaThumbnailURL(mediaID) {
      try {
        const result = await axios.$get(`${apiUrl}/media/${mediaID}`);
        return this.getAbsoluteImageUrl(result.sizes?.thumbnail?.url || result.url);
      } catch (error) {
        return "";
      }
    },
    async requestSelectedPosts() {
      try {
        const fields = [
          "select[featuredPosts]=true",
          "populate[posts][title]=true",
          "populate[posts][author]=true",
          "populate[posts][summary]=true",
          "populate[posts][createdAt]=true",
          "populate[posts][thumbnail][sizes][thumbnail][url]=true",
          "populate[posts][slug]=true",
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
    fetchCmsUrl = process.env.INTERNAL_CMS_URL || "http:payload:3000//";
  }

  const adapter = new BlogAdapter(context.$axios, fetchCmsUrl, publicCmsUrl);
  inject("BlogAdapter", adapter); 
}
