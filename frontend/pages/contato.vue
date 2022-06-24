<template>
  <div>
    <v-app>
      <v-container fluid>
        <v-row
          class="ma-0 mt-12"
          align="center"
          justify="space-around"
          no-gutters
        >
          <v-col>
            <v-row class="ma-0" justify="center">
              <v-col cols="12" sm="9">
                <p class="display-2 font-weight-regular">Contato</p>
              </v-col>
            </v-row>
          </v-col>
          <v-col cols="11" sm="5"></v-col>
        </v-row>

        <v-row class="mx-8">
          <v-col
            v-for="{ label, url, description } of icons"
            :key="label"
            cols="12"
            md="4"
          >
            <v-card
              :href="url"
              elevation="0"
              class="
                mb-4
                d-flex
                flex-column
                justify-center
                align-center
                flex-lg-row
              "
            >
              <v-icon size="60px" icon dark color="black">{{ label }}</v-icon>
              <v-card-title>{{ description }}</v-card-title>
            </v-card>
          </v-col>
        </v-row>

        <v-row
          class="ma-0 mt-12"
          align="center"
          justify="space-around"
          no-gutters
        >
          <v-col>
            <v-row class="ma-0" justify="center">
              <v-col cols="12" sm="9">
                <p class="display-2 font-weight-regular">Créditos</p>
              </v-col>
            </v-row>
          </v-col>
          <v-col cols="11" sm="5"></v-col>
        </v-row>
        <div v-for="{ title, people, notes } of sections" :key="title">
          <ContactSectionHeaderRow :section="title"></ContactSectionHeaderRow>
          <PeopleCardsRow :people="people" :notes="notes"></PeopleCardsRow>
        </div>
      </v-container>
    </v-app>
  </div>
</template>

<script>
import PeopleCardsRow from "@/components/first_level/PeopleCardsRow.vue";
import ContactSectionHeaderRow from "@/components/first_level/ContactSectionHeaderRow.vue";

export default {
  components: {
    PeopleCardsRow,
    ContactSectionHeaderRow,
  },
  data: () => ({
    icons: [
      {
        label: "mdi-facebook",
        description: "/uspinovacao1",
        url: "https://www.facebook.com/uspinovacao1/",
        color: "rgba(59,89,153 ,1 )",
      },
      {
        label: "mdi-instagram",
        description: "@uspinovacao",
        url: "http://www.instagram.com/uspinovacao/",
        color: "#C13584",
      },
      {
        label: "mdi-phone",
        description: "(11) 3091-4166",
        url: "https://inovacao.usp.br",
        color: "#C13584",
      },
      {
        label: "mdi-twitter",
        description: "/uspinovacao",
        url: "https://twitter.com/uspinovacao",
        color: "#00aced",
      },
      {
        label: "mdi-linkedin",
        description: "/uspinovacao",
        url: "https://www.linkedin.com/in/uspinovacao/",
        color: "#0e76a8",
      },

      {
        label: "mdi-email",
        description: "hubusp.inovacao@usp.br",
        url: "mailto:hubusp.inovacao@usp.br",
        color: "#C13584",
      },
    ],

    sections: [
      {
        title: "Universidade de São Paulo",
        people: [
          {
            src: "https://drive.google.com/uc?export=view&id=12wWnBwB562gm8Znk2d_RJ9RKoT0d27V9",
            name: "Prof. Dr. Carlos Carlotti",
            role: "Reitor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1DaYwoRPo062xu_dauhQOvHSMNI4UWI46",
            name: "Prof.ª Dr.ª Maria Arminda",
            role: "Vice-reitora",
          },
        ],
      },
      {
        title: "Agência USP de Inovação - Equipe Hub",
        people: [
          {
            src: "https://drive.google.com/uc?export=view&id=1j7Gjoeg7YGh0qazxliPyPGSE8am7BILj",
            name: "Luiz Henrique Catalani",
            role: "Coordenador",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1GT0L01cWJzllGbMH_bnim6yEQtHLiTmV",
            name: "Emanuel Carrilho",
            role: "Vice-Coordenador",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1AfUI1Db8pm4GylNEnjCxWXM3Wz4Ch5mr",
            name: "Lara Guimarães",
            role: "Hub USP Inovação",
          },
          {
            src: "",
            name: "Neylor Fabiano",
            role: "Hub USP Inovação",
          },
        ],
      },
      {
        title: "CodeLab - CCSL - IME",
        people: [
          {
            src: "https://drive.google.com/uc?export=view&id=17FI5R3H9lZascX-p67TabXXpayvZHeqk",
            name: "Alfredo Goldman",
            role: "Coordenador",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1EsuupsEr2tpR8ExOVSg6_HXtzUG-i_hC",
            name: "João Daniel",
            role: "Desenvolvedor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1C3tzKiG7_B_CZVKOt8dyPUWrwO3uYcE6",
            name: "Luiz Carlos",
            role: "Desenvolvedor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1fBO0461oSTPapARTnfWNYXc5edWsBwH_",
            name: "Briza Mel",
            role: "Desenvolvedora",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1mOcHd5Zzm82P4jADIalg43X8ysJo01ye",
            name: "Marília Dicezare",
            role: "Desenvolvedora",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1kjC07rdhaESqPMQ_XYf1kt9G4PT5Rv9-",
            name: "Erick Santana",
            role: "Desenvolvedor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1pR6n-PoKt0-h4b0O6oQIEUhETnY7IWRx",
            name: "Thiago Guerrero",
            role: "Desenvolvedor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1G-JivuZjuGOh0hWj0iSRYN28d5DLjkss",
            name: "Leonardo Costa",
            role: "Desenvolvedor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1BicPXgkm5JRtcxyKS-hPk78P9J4boyFT",
            name: "Gabriel Brandão",
            role: "Desenvolvedor",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1OEbinkI-1gDBsdrOGBqiVQfk-3Azk6tw",
            name: "Evandro Nakayama",
            role: "Desenvolvedor",
            specialText: "(In Memoriam)",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1FcC5OZjmlMU7ou7sqYoNwAWbreN_OXBm",
            name: "Renato Cordeiro",
            role: "Desenvolvedor",
          },
        ],
      },
    ],
  }),
};
</script>
