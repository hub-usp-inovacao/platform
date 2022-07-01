<template>
  <div>
    <v-app>
      <v-container>
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
        <div v-for="{ title, smaller, people, notes } of sections" :key="title">
          <ContactSectionHeaderRow :section="title"></ContactSectionHeaderRow>
          <PeopleCardsRow
            :people="people"
            :smaller="smaller"
            :notes="notes"
          ></PeopleCardsRow>
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
            src: "https://drive.google.com/uc?export=view&id=1mJm4BeTMRLkh2fZxRNpqxInw0Oj5jXF8",
            name: "Geciane Porto",
            role: "Hub USP Inovação",
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
          {
            src: "https://drive.google.com/uc?export=view&id=1pqpMB5RVTuW9Z8jBFLFQUuCy7mtfRvc4",
            name: "Flávia Prado",
            role: "Agente de Inovação",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1vbNc3zcZYYqYdTrp4hWCQAu60nUsxJzy",
            name: "Fernanda Basso",
            role: "Transferência de tecnologia",
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
            role: "Desenvolvedor - CodeLab",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1C3tzKiG7_B_CZVKOt8dyPUWrwO3uYcE6",
            name: "Luiz Carlos",
            role: "Desenvolvedor - CodeLab",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1fBO0461oSTPapARTnfWNYXc5edWsBwH_",
            name: "Briza Mel",
            role: "Desenvolvedor - CodeLaba",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1mOcHd5Zzm82P4jADIalg43X8ysJo01ye",
            name: "Marília Dicezare",
            role: "Desenvolvedor - CodeLaba",
          },
        ],
      },
      {
        title: "Agradecimentos",
        smaller: true,
        people: [
          {
            src: "https://drive.google.com/uc?export=view&id=16I5lwNE1rgBGb6clZHxljJ_Y30H8uCOh",
            name: "Marcos Martins",
            role: "Ex-Coordenador AUSPIN",
          },

          {
            src: "https://drive.google.com/uc?export=view&id=1eHnS8N448dK0L1Evp9ME3fzgOe7vQDNg",
            name: "Josmar Andrade",
            role: "Ex-Assessor da Coordenação AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1HpmDm9kCIO73lWN4HFuRxhFwBUsDgboc",
            name: "Ronaldo Nina",
            role: "Chefe da Comunicação AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1omkFtI5worTLbUivgPm7G5r90vir_OS0",
            name: "Marcelo Valverde",
            role: "Arte Finalista AUSPIN",
          },

          {
            src: "https://drive.google.com/uc?export=view&id=186cZid7WcI4S4JFQoyL1vcetqe5siGPS",
            name: "Rosemeire Domingos",
            role: "Eventos AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=12Z_LXg1gl2rNs0pe7hT2HUAyKLHUxr1I",
            name: "Livia Moraes",
            role: "Comunicação AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1WgWIKb8mlOSjXCwQfaAPYH1N9mhzCObm",
            name: "Lucas Martins",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1T-Mt9NxkdxRULf-D7fk9CeWxvL4nC7Qs",
            name: "Marcelo Renda",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1MeME1mU9H0ZVtbyJmHfNsm0fveFRG7F7",
            name: "Thaynná Souza",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=16zKodkgpLgVUDSmj8U9Rj5XLC85dSuGy",
            name: "Mariana Arruda",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=19n8RxSEtc5-HABvYMOLc7IXTksA0N9yp",
            name: "Giovanna Kim",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1K1AOJ4A-ZDyk3rlfHXUAo83kfQrma8CN",
            name: "Davi Bastos",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1vCA0Q9vJUk9k1K4toUW0ftI1QZK6VC2G",
            name: "Giovanni Watanabe",
            role: "Estagiário(a) - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1GAPXXL6AnH_JubxcYUcV1fmUhbUkMlJn",
            name: "Heidi Piva",
            role: "Audiovisual AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1iV8Q_ZpLdkcracVhjcZg0tf8EqTfSb7N",
            name: "Liliane Sartorio",
            role: "Bolsista AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1fv6Pd4Vo7ogQnRdEKoufOaWct8gi8kHy",
            name: "Aline Santos",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1PpRlRt0QID3yFcdsIQ6_N-5YJbdzuE-N",
            name: "Ana Gambardella",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=12VpH5g2mZb74E8WvC43Rz_Gb__uTb1Bt",
            name: "André Balico",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1yB2EkXAkqGy4mXY9UGOYVzw6fjd2sgGp",
            name: "Bárbara Contin",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1-4mibeF6RSOGTi20kbwCzXBHVkgOEOt8",
            name: "Caroline",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1k37QFGg4HeV6KlFnwS-1Sq1hlPbgijLQ",
            name: "Renata Lobo",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1kGimqfySLTday-Bp79OAyMBf8voU26Qo",
            name: "Jardel",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1la0pRvq_QEDUO-0gy-6CN1Ech6WtLbKL",
            name: "Isabella Ciarrocchi",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1o-MmmyORlrrW6xJ2a_2E0nJVpAQ16APq",
            name: "Jucelí Andrade",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1TZ-4-5cLeTw5n8fhwDUtc4Kz0w7WJ6O3",
            name: "Maira Luana",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1F4rgEvjrgsVYDDn45eOcFC3Y8dlep2h2",
            name: "Miriam Mangueira",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1xuIdti1reVNEUQ_QrkHxSvIUT4OrJ0l9",
            name: "Pedro Guimarães",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1cnU0t-Lrf0Ngbea1s4m7welGfQfK3kUQ",
            name: "Raul Canestri",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=14QPURb1zpj0j3B-795zUYiNB6MlvL2_U",
            name: "Rodolfo Pereira",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1qt4JIF0qREi-PfnpU7oDGuh4NsMlMklN",
            name: "Samira",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1ilNyFzHDHGPViT4e182auiCqxnOVbSKM",
            name: "Tailane Santos",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1x15BWOJblJ3W9TcKa4NZgROKmAIyBasj",
            name: "Filipe Bernardi",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1yzNqua3sY0OI5038WZHPDTzBzw1UE7fU",
            name: "Ana Bárbara Rodrigues",
            role: "Lev. Competências - AUSPIN",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1kjC07rdhaESqPMQ_XYf1kt9G4PT5Rv9-",
            name: "Erick Santana",
            role: "Desenvolvedor - CodeLab",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1pR6n-PoKt0-h4b0O6oQIEUhETnY7IWRx",
            name: "Thiago Guerrero",
            role: "Desenvolvedor - CodeLab",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1G-JivuZjuGOh0hWj0iSRYN28d5DLjkss",
            name: "Leonardo Costa",
            role: "Desenvolvedor - CodeLab",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1BicPXgkm5JRtcxyKS-hPk78P9J4boyFT",
            name: "Gabriel Brandão",
            role: "Desenvolvedor - CodeLab",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1OEbinkI-1gDBsdrOGBqiVQfk-3Azk6tw",
            name: "Evandro Nakayama",
            role: "Desenvolvedor - CodeLab",
            specialText: "(In Memoriam)",
          },
          {
            src: "https://drive.google.com/uc?export=view&id=1FcC5OZjmlMU7ou7sqYoNwAWbreN_OXBm",
            name: "Renato Cordeiro",
            role: "Desenvolvedor - CodeLab",
          },
        ],
      },
    ],
  }),
};
</script>
