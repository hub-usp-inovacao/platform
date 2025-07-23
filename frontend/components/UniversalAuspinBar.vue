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
        <div class="page-list left">
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
        <div class="search-area">
          <transition name="expand">
            <v-text-field
                v-if="searchActive"
                v-model="search"
                placeholder="Pesquise e aperte enter"
                class="search-input"
                flat
                solo
                dense
                prepend-icon
                @keydown.enter="searchQuery"
                @blur="searchActive = false"
                ref="searchField"
            >
              <template #prepend-inner>
                <v-icon class="search" small>mdi-magnify</v-icon>
              </template>
            </v-text-field>
          </transition>
          <v-btn
              class="search-icon no-background-hover"
              icon
              :ripple="false"
              @click="toggleSearch"
          >
            <v-icon class="search">mdi-magnify</v-icon>
          </v-btn>
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
    ],
    search: '',
    searchActive: false
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
    },
    toggleSearch() {
      this.searchActive = !this.searchActive;
      this.$nextTick(() => {
        if (this.searchActive) {
          this.$refs.searchField.$el.querySelector('input').focus();
        }
      });
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
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
}

.filler {
  background-color: #000428;
  box-shadow: none !important;
  border-bottom: 1px solid white;
}

.page-list.left {
  margin-right: auto;
  display: flex;
  align-items: center;
  gap: 8px;
  padding-left: 8px;
  font-family: 'Ubuntu', 'Arial', sans-serif;
}

.page-list a {
  color: rgba(255, 255, 255, 0.7) !important;
  text-decoration: none !important;
  font-weight: 400;
  font-size: 16px;
  transition: color 0.2s ease;
  padding: 0 8px;
}

.page-list a.active,
.page-list a:hover {
  color: rgba(255, 255, 255, 1) !important;
}

.page-list .page {
  padding: 14px 0 15px 0;
}

.search-area {
  margin-left: auto;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: auto;
  position: relative;
  padding-right: 8px;
}

.search-icon {
  margin-left: 10px;
}

.expand-enter-active {
  animation: expand-in 0.3s;
}

.expand-leave-active {
  animation: expand-in 0.3s reverse;
}

@keyframes expand-in {
  0% {
    width: 0;
    opacity: 0;
    transform: translateX(20px);
  }
  100% {
    width: 240px;
    opacity: 1;
    transform: translateX(0);
  }
}

.search-input {
  width: 240px !important;
  margin-right: 8px;
  border-radius: 0px !important;
  font-family: 'Ubuntu', 'Arial', sans-serif;
  font-size: 14px !important;
  border: 2px solid #ddd;
  height: 42px !important;
}

/* Ajustes para manter a cor original da barra de pesquisa */
.search-input >>> .v-input__slot {
  background-color: #000428 !important;
}

.search-input >>> .v-text-field__slot input {
  color: white !important;
}

.search-input >>> .v-input__prepend-inner .v-icon {
  color: white !important;
}

.no-background-hover::before {
  background-color: transparent !important;
}
</style>