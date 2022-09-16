<template>
  <div class="bg-gray">
    <v-container>
      <header>
        <h1>Jornada do Empreendedorismo USP</h1>
        <p v-for="p in headers" :key="p">{{ p }}</p>
      </header>
    </v-container>
    <main>
      <v-container class="hidden-sm-and-down roadmap">
        <v-row v-for="({ title, color, caption, to }, i) in rows" :key="i">
          <v-col cols="4" :offset="i % 2 ? 2 : 0">
            <ColorfulCard :title="title" :color="color" :index="i + 1" />
          </v-col>
          <v-col cols="6" :offset="i % 2 ? 0 : 2">
            <CallToAction :color="color" :caption="caption" :to="to" />
          </v-col>
        </v-row>
      </v-container>

      <v-container class="hidden-md-and-up">
        <div
          v-for="({ title, color, caption, to }, i) in rows"
          :key="i"
          class="mb-12"
        >
          <ColorfulCard
            class="mb-n4"
            :title="title"
            :color="color"
            :index="i + 1"
          />
          <CallToAction :color="color" :caption="caption" :to="to" />
        </div>
      </v-container>
    </main>
  </div>
</template>

<script>
import ColorfulCard from "@/components/journey/ColorfulCard.vue";
import CallToAction from "@/components/journey/CallToAction.vue";

export default {
  components: {
    ColorfulCard,
    CallToAction,
  },
  data: () => ({
    headers: [
      `Na jornada do empreendedorismo há diversos desafios. Se você está no início dela, é importante começar com o aprendizado sobre inovação e empreendedorismo, acessando o tópico “Processo de aprendizagem”. Porém, se você se sente pronto para exercitar o empreendedorismo, pode acessar o tópico “Engajamento e começando a exercitar o empreendedorismo”.`,
      `Se você já está empreendendo e precisa de uma rede de apoio, consulte o tópico “Processo de Incubação, criando minha própria empresa”, onde você poderá se conectar com outras startups, além de encontrar um local para instalar sua equipe. Para quem já está estabelecido, é interessante acessar o tópico “Aprimorando tecnologias, fortalecendo a empresa”, onde você poderá fortalecer sua empresa e melhorar seu produto. Por fim, para aqueles que buscam formas de captar recursos, o tópico ideal é o “Buscando financiamento”.`,
    ],
    rows: [
      {
        title: "Processo de Aprendizagem",
        caption: `O passo inicial na sua jornada. 
                  Busque aprender sobre inovação
                  e empreendedorismo, cursando
                  as disciplinas oferecidas pela USP.`,
        color: "rgba(192, 22, 27, 1)",
        to: "/jornada/aprenda",
      },
      {
        title: "Engajamento e começando a exercitar o empreendedorismo",
        caption: `Saiba tudo sobre entidades estudantis,
                  empresas júniores e também espaços
                  de convivência: aproveite ao máximo 
                  o que a comunidade USP oferece.`,
        color: "rgba(228, 105, 38, 1)",
        to: "/jornada/pratica",
      },
      {
        title: "Processo de incubação, criando minha própria empresa",
        caption: `Existe uma rede de ambientes possíveis
                  para que seu negócio se desenvolva,
                  tanto dentro quanto fora da USP.
                  Saiba mais sobre as incubadoras!`,
        color: "rgba(244, 196, 30, 1)",
        to: "/jornada/criar",
      },
      {
        title: "Aprimorando tecnologias, fortalecendo a empresa",
        caption: `Conte com a competência de especialistas
                  da USP para formar parcerias, fortalecer sua empresa
                  e desenvolver o seu produto.`,
        color: "rgba(51, 140, 33, 1)",
        to: "/jornada/aprimorar",
      },
      {
        title: "Buscando financiamento",
        caption: `Descubra os primeiros passos
                  desta próxima jornada, sabendo
                  como se preparar para as principais
                  maneiras de captação de recursos.`,
        color: "rgba(33, 78, 140, 1)",
        to: "/jornada/financiamento",
      },
    ],
  }),
};
</script>

<style scoped>
.bg-gray {
  background-color: #ececec;
}

.roadmap {
  background-image: url("/road.png");
}
</style>
