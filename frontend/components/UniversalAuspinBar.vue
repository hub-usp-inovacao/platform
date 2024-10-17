<template>
  <div>
    <v-app-bar
      color="#000428"
      dark
      fixed
      flat
      :height="barHeight + 'px'"
      class="custom-hidden-md-and-down bar"
    >
      <div class="bar-container">
        <div class="d-inline-flex align-center page-list">
          <a
            v-for="item in items"
            :key="item.title"
            :href="item.href"
            target="_blank"
            :class="{ active: item.href == '/' }"
          >
            <div class="page">
              {{ item.title }}
            </div>
          </a>
        </div>
        <div class="d-flex">
          <div class="search-divider"></div>
          <v-menu
            rounded="0"
            offset-y
            left
            nudge-left="-4"
            :close-on-content-click="false"
          >
            <template v-slot:activator="{ attrs, on }">
              <v-btn
                class="search-icon no-background-hover"
                icon
                v-bind="attrs"
                v-on="on"
                :ripple="false"
              >
                <v-icon class="search">mdi-magnify</v-icon>
              </v-btn>
            </template>

            <div class="search-field">
              <v-text-field
                placeholder="Pesquise e aperte enter"
                class="search-input"
                flat
                solo
                dense
                v-model="search"
                prepend-icon
                @keydown.enter="searchQuery"
              >
                <template #prepend-inner>
                  <v-icon class="search" small>mdi-magnify</v-icon>
                </template>
              </v-text-field>
            </div>
          </v-menu>
        </div>
      </div>
    </v-app-bar>
    <div class="bar filler" :style="{ height: (barHeight + 1) + 'px' }"></div>
  </div>
</template>

<script>
export default {
  data: () => ({
    items: [
      { title: 'AUSPIN', href: 'http://www.inovacao.usp.br/' },
      {
        title: 'Portal do Inventor',
        href: 'https://usp.inteum.com/usp/InventorPortal'
      },
      { title: 'Hub USP Inovação', href: '/' },
    ]
  }),
  computed: {
    barHeight() {
      return 49;
    }
  },
  methods: {
    searchQuery() {
      const encodedQuery = encodeURIComponent(this.search.trim());
      const searchUrl = `/busca?q=${encodedQuery}`;
      window.location.href = searchUrl;
    }
  }
}
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css?family=Ubuntu&display=swap');

.bar {
  box-sizing: border-box;
  box-shadow: 0 0 5px rgba(0,0,0,0.2),
    0 1px 0 rgba(255,255,255,0.15) !important;
}

.bar-container {
  width: 1029px;
  padding: 0 0px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.filler {
  background-color: #000428;
  box-shadow: none !important;
  border-bottom: 1px solid white;
}
.page-list {
  font-family: 'Ubuntu', 'Arial', sans-serif;
}
.page-list .page {
  padding: 14px 14px 15px 14px;
}
.page-list a {
  color: rgba(255, 255, 255, 0.7) !important;
  text-decoration: none !important;
  transition: color 0.2s ease;
  font-weight: 400;
  font-size: 16px;
}
.page-list a:hover {
  color: rgba(255, 255, 255, 1) !important;
}
.page-list a.active {
  color: rgba(255, 255, 255, 1) !important;
}
.search-icon {
  margin: 0 10px 0 5px;
}
.search-divider {
  width: 1px;
  height: 50px;
  background-color: rgba(255, 255, 255, 0.10);
}
.search-field {
  width: 340px;
  height: 68px;
  background-color: #000428;
  padding: 15px;
  color: #fff;
}
.search-input {
  border-radius: 0px !important;
  font-family: 'Ubuntu', 'Arial', sans-serif;
  font-size: 14px !important;
  border: 2px solid #ddd;
  height: 42px !important;
}
.no-background-hover::before {
   background-color: transparent !important;
}
</style>
