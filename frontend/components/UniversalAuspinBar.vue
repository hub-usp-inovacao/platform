<template>
  <div class="text-center d-flex align-center barra custom-hidden-md-and-down" >
        <v-btn
          color="#000428"
          dark
          href="http://www.inovacao.usp.br/"
          target="_blank"
          class="botao1"
        >
          AUSPIN
        </v-btn>

        <v-btn
          color="#000428"
          dark
          href="https://patente.inovacao.usp.br/inteumweb/inventorportal/"
          target="_blank"
          class="botao"
        >
          Portal do Inventor
        </v-btn>

        <v-btn color="#000428" dark href="/" class="botao" > Hub USP Inovação </v-btn>
      </div>
</template>

<script>
export default {

}
</script>


<style>
.barra {
  background-color: #000428;
  height: 3.1em;
  justify-content: flex-start;
}

.v-btn {
    text-transform: unset !important;
}

.botao1 {
  margin-left: 30em;
}
</style>