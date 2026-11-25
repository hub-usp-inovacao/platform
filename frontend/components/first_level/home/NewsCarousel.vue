<template>
  <section class="carousel" aria-label="Notícias do blog">
    <p v-if="loading">Carregando…</p>
    <p v-else-if="error || !posts.length">Não foi possível carregar as notícias.</p>

    <div v-else>
      <a :href="'/blog/' + current.slug" class="slide">
        <img :src="imageUrl(current)" :alt="imageAlt(current)" />
        <div class="caption">
          <h3>{{ current.title }}</h3>
          <p>{{ current.summary }}</p>
        </div>
      </a>

      <button class="nav prev" aria-label="Anterior" @click="prev">‹</button>
      <button class="nav next" aria-label="Próxima" @click="next">›</button>

      <div class="dots">
        <button
          v-for="(p, i) in posts"
          :key="p.id"
          :class="{ active: i === index }"
          :aria-label="'Ir para ' + p.title"
          @click="index = i"
        />
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "NewsCarousel",
  props: {
    limit: { type: Number, default: 5 },
  },
  data() {
    return {
      posts: [],
      loading: true,
      error: null,
      index: 0,
    };
  },
  computed: {
    base() {
      return process.env.CMS_URL || "";
    },
    origin() {
      try {
        return new URL(this.base).origin;
      } catch (e) {
        return "";
      }
    },
    current() {
      return this.posts[this.index];
    },
  },
  async mounted() {
    try {
      const params = new URLSearchParams({
        limit: String(this.limit),
        sort: "-createdAt",
        depth: "1",
      });
      ;['title', 'slug', 'summary', 'author', 'thumbnail', 'createdAt'].forEach((f) =>
        params.set('select[' + f + ']', 'true')
      );
      const url = this.base + "/api/posts?" + params.toString();
      const res = await fetch(url);
      if (!res.ok) throw new Error("HTTP " + res.status);
      const json = await res.json();
      this.posts = json.docs || [];
    } catch (e) {
      this.error = e.message || "Erro ao carregar notícias";
    } finally {
      this.loading = false;
    }
  },
  methods: {
    abs(u) {
      if (!u) return "";
      return u.startsWith("http") ? u : this.origin + u;
    },
    imageUrl(post) {
      const t = post.thumbnail || {};
      const full = t.sizes && t.sizes.full && t.sizes.full.url;
      return this.abs(full || t.url);
    },
    imageAlt(post) {
      return (post.thumbnail && post.thumbnail.alt) || post.title;
    },
    next() {
      this.index = (this.index + 1) % this.posts.length;
    },
    prev() {
      this.index = (this.index - 1 + this.posts.length) % this.posts.length;
    },
  },
};
</script>
<style scoped>
/* ---------- Base (celular primeiro) ---------- */
.carousel {
  position: relative;
  overflow: hidden;
  border-radius: 12px;
}

.slide {
  display: block;
  position: relative;
  color: #fff;
  text-decoration: none;
  min-height: clamp(280px, 45vw, 520px); /* altura cresce com a tela */
}

.slide img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.caption {
  position: absolute;
  inset: 0;
  z-index: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 3.5rem; /* espaço lateral para as setas */
}

.caption h3 {
  font-size: clamp(1.5rem, 5vw, 4rem); /* mín 24px, máx 64px */
  font-weight: 800;
  line-height: 1.1;
  text-transform: uppercase;
  margin-bottom: 0.75rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.9), 0 4px 16px rgba(0, 0, 0, 0.8);
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.caption p {
  font-size: clamp(1rem, 2vw, 1.5rem);
  line-height: 1.4;
  margin: 0;
  max-width: 900px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.9), 0 4px 12px rgba(0, 0, 0, 0.8);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  border: 0;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  font-size: 1.2rem;
  cursor: pointer;
}
.prev { left: 8px; }
.next { right: 8px; }

.dots {
  position: absolute;
  bottom: 8px;
  right: 12px;
  z-index: 2;
  display: flex;
  gap: 6px;
}
.dots button {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: 0;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}
.dots button.active { background: #fff; }

/* ---------- Tablet (≥ 600px) ---------- */
@media (min-width: 600px) {
  .caption { padding: 1.5rem 4.5rem; }
  .caption p { -webkit-line-clamp: 3; }
  .nav { width: 44px; height: 44px; font-size: 1.5rem; }
  .prev { left: 12px; }
  .next { right: 12px; }
  .dots button { width: 12px; height: 12px; }
}

/* ---------- Desktop (≥ 960px) ---------- */
@media (min-width: 960px) {
  .caption { padding: 2rem 5rem; }
  .dots { bottom: 14px; right: 20px; }
}

/* ---------- Telas grandes (≥ 1264px) ---------- */
@media (min-width: 1264px) {
  .caption { padding: 2.5rem 6rem; }
  .nav { width: 52px; height: 52px; font-size: 1.8rem; }
}

/* ---------- Celular na horizontal (pouca altura) ---------- */
@media (max-height: 480px) and (orientation: landscape) {
  .slide { min-height: 260px; }
  .caption h3 { -webkit-line-clamp: 2; }
  .caption p { display: none; }
}
</style>
