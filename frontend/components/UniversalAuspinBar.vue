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
      <v-container>
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
        <v-spacer></v-spacer>
      </v-container>
    </v-app-bar>
    <div class="bar filler" :style="{ height: barHeight + 'px' }"></div>
  </div>
</template>

<script>
export default {
  data: () => ({
    items: [
      { title: 'AUSPIN', href: 'http://www.inovacao.usp.br/' },
      {
        title: 'Portal do Inventor',
        href: 'https://patente.inovacao.usp.br/inteumweb/inventorportal/'
      },
      { title: 'Hub USP Inovação', href: '/' },
    ]
  }),
  computed: {
    barHeight() {
      return 50;
    }
  },
}
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css?family=Ubuntu&display=swap');

.bar {
  border-bottom: 1px solid #fff !important;
  box-sizing: border-box;
  box-shadow: 0 0 5px rgba(0,0,0,0.2),
    0 1px 0 rgba(255,255,255,0.15) !important;
}
.filler {
  background-color: #000428;
  box-shadow: none !important;
}
.page-list {
  font-family: 'Ubuntu', 'Arial', sans-serif;
}
.page-list .page {
  padding: 14px 15px;
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
</style>
