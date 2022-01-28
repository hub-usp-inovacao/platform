<template>
  <v-carousel
    hide-delimiters
    cycle
    continuous
    interval="2500"
    :show-arrows="false"
    :height="carouselHeight"
  >
    <v-carousel-item
      v-for="(nowDisplaying, i) in displayCompanies.nowDisplaying"
      :key="i"
    >
      <v-container>
        <v-row>
          <v-col
            v-for="{ name, src } in nowDisplaying"
            :key="name"
            :cols="displayCompanies.cols"
          >
            <v-img
              eager
              class="mx-auto"
              width="20rem"
              height="auto"
              :alt="name"
              :src="src"
            ></v-img>
          </v-col>
        </v-row>
      </v-container>
    </v-carousel-item>
  </v-carousel>
</template>

<script>
export default {
  data: () => ({
    items: [
      {
        src:
          "https://drive.google.com/uc?export=view&id=1sQ0wMMH0F3r-JoYppcalOfJ4Hw2nMOG8",
        name: "99",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1rMAz-9GHSxWDdJ1FEn577YBNqInIh2Xp",
        name: "adespec",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=11MNH5PD3vd4MVpFZawHHJYAAuiZtjdQZ",
        name: "agrosmart",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1aPL4QTrg80qQ0cx50nTqhp01dl_eS1dj",
        name: "baumer",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1Xe1M7Vqw9EJ5oETs2-9zXSI0URTR67yq",
        name: "biolinker",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1xetNNTq5sCt1mwfvzU2RbT4qXVYz44Xd",
        name: "biópolis",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1GscGYTabeitXwUx3qYuurtNe5NS7O2DR",
        name: "biopolix",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1cbW8UgjOiWt8D8PUGQKvKScljcavonJ5",
        name: "buscapé",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1ACxLLpSYb4j7TnXOr5pcjWFwr1aDQPFV",
        name: "carefy",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1Ag4TH6NgCFsm9VM2iNb3Nx_kNE1LXlBe",
        name: "cargoBR",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=124p55LN7ED9y_MufTSnPUZvAFCHpOyIH",
        name: "cosan",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1rIZM2dAbLhScvhZ4dmqcrv-g2zcBZAi7",
        name: "electrocell",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1hUz5g3P6YPUPGovfagSyxm7WDDj693R6",
        name: "gaia",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1K40b7RTHs-wEy_HJxY7sEFWGXhGqLlst",
        name: "gympass",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1uUrpWiXsi8Nv3vEaOT2aUmbdDzlgrVdJ",
        name: "iclinic",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1SdOdo1bKdWlu7NlU7l0C0AucONuXnsvt",
        name: "ifood",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1Rgm1G953QSJEZvzF-uOhYbVHIL7b8-CJ",
        name: "kidopi",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1tIoyAUxhzqRCYUPewFLfOxyd9oxLCRWv",
        name: "lasertools",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1EepHH_KRRbjCqP_d0tNDA1v8bzWSpVRG",
        name: "lychnoflora",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1X4jgMi1hz2rj3NVQ1fthtlHs5oYjTY1I",
        name: "narcissus",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=16KjqRr3Djf28k5Bzt-S8590S3XoXdDJN",
        name: "nativus",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1sneKf-CAZheW8Zd0AOT1pHyhlabCIVAj",
        name: "nubank",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1RQY8TmO-D0XdII2MWwkqvZXIGqaHCpGt",
        name: "onkos",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1cMJpjs1QiLMhQDhIbMg2vqupenByho1M",
        name: "pluricell",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1zlmrxRA4SuFOgpY_apLJ_d2DeIOh7WEg",
        name: "sociedade_do_sol",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1x_C7O6TRguGY52YsTDM1ErpU1L71lI9A",
        name: "totvs",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1-OeSNxr5HMyoVo6P47-aOXXi3lTPX7Uw",
        name: "verdartis",
      },
      {
        src:
          "https://drive.google.com/uc?export=view&id=1RCHtxEHt36-ARL1Li8dikOHTvnwARzFh",
        name: "vexpense$",
      },
    ],
  }),
  computed: {
    displayCompanies() {
      const cpp = ["xs", "sm"].includes(this.$vuetify.breakpoint.name) ? 1 : 3;

      const nowDisplaying = this.items.reduce((matrix, el, i) => {
        return matrix.concat([(i % cpp == 0 ? [] : matrix.pop()).concat(el)]);
      }, []);

      return {
        nowDisplaying,
        cols: Math.ceil(12 / cpp),
      };
    },
    carouselHeight() {
      let val;
      switch (this.$vuetify.breakpoint.name) {
        case "xs":
          val = "20rem";
          break;
        case "sm":
          val = "20rem";
          break;
        case "md":
          val = "25rem";
          break;
        case "lg":
          val = "30rem";
          break;
        case "xl":
          val = "30rem";
          break;
      }

      return val;
    },
  },
};
</script>
