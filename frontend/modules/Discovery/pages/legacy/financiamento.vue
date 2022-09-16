<template>
  <div>
    <Step
      title="Financiamento"
      :description="description"
      color="#214E8C"
      :buttons="buttons"
      previous="aprimorar"
      previous-color="#338C21"
      next=""
      :items="filteredItems"
    >
      <template v-slot:SecondaryButtons>
        <v-row justify="center">
          <v-col class="pt-0">
            <div class="d-flex flex-wrap justify-space-around">
              <v-btn
                v-for="{ label } in buttons"
                :key="label"
                class="white px-6 py-6 ma-1 flex-grow-1 button text-capitalize"
                :color="selected == label ? 'grey' : 'white'"
                max-width="100%"
                @click="select"
              >
                {{ label }}
              </v-btn>
            </div>
          </v-col>
        </v-row>
      </template>
    </Step>
  </div>
</template>

<script>
import Step from "@/components/journey/Step.vue";
import { formatURL } from "@/lib/format";

export default {
  components: {
    Step,
  },
  data: () => ({
    description: [
      `A busca por financiamento deve ser uma tarefa contínua do empreendedor ao longo do ciclo de vida de sua empresa.`,
      `Empreendedores com interesse em desenvolver seu negócio a partir de uma tecnologia a ser aplicada em um determinado setor econômico podem se associar a empresas da indústria de aplicação de sua tecnologia e acessar financiamentos de P&D&I da EMBRAPII. Essa fonte de fomento é direcionada para projetos de P&D&I desenvolvidos em parceria entre empresas e ICTs credenciados em diferentes áreas de competência.`,
      `O empreendedor deve incluir no seu playbook, uma lista das fontes de recursos que tem interesse em acessar. Inicialmente o empreendedor busca acessar o capital mais barato com menos exigências contratuais, que constituem-se no capital de fomento. É um financiamento oferecido por organismos públicos para suportar novos negócios com condições mais atrativas que as de mercado. Estes fomentos funcionam em fluxo contínuo (FAPESP) ou por meio de editais (SEBRAE, CNPQ e FINEP)`,
      `O empreendedor também pode acessar o Investimento Anjo, oferecido pelo investidor pessoa física com interesse em investir no estágio inicial da startups, muitas vezes, antes mesmo do modelo de negócio ter sido validado e da primeira venda. Além dos recursos financeiros, o investidor anjo oferece mentoria que, muitas vezes, é tão valiosa quanto o capital financeiro. O Empreendedor pode entrar em contato com as associações de investidores anjo criadas por ex-alunos de algumas unidades da USP ou outras associações de anjos do mercado.`,
      `Em um estágio mais avançado do ciclo de vida da startup, com histórico de vendas e produto validado pelo mercado com interesse em tracionar seu negócio, o empreendedor pode acessar os fundos de investimentos Venture Capital especializados em Early Stage (pre-seed e seed). A melhor forma de acessar esses fundos de investimento é se conectar com as associações de investidores anjos que possuem forte conexão com Venture Capital.`,
    ],
    buttons: [
      { label: "EMBRAPII" },
      { label: "Fomento" },
      { label: "Investidores Anjo" },
      { label: "Early Stage" },
      { label: "Venture Capital" },
    ],

    selected: "",

    items: {
      EMBRAPII: [],
      FOMENTO: [],
      "INVESTIDORES ANJO": [],
      "EARLY STAGE": [],
      "VENTURE CAPITAL": [],
    },

    selectedButtonSecondary: undefined,
  }),

  computed: {
    filteredItems() {
      const secondaryButton = this.selectedButtonSecondary;

      if (secondaryButton != undefined) {
        return this.items[secondaryButton];
      } else {
        return [];
      }
    },
  },

  async beforeMount() {
    const sheetID = "12O4u6P5Ytf9_uXmHu89vC_7oE_orw7GDKRajzSzLh5g";
    const sheetName = "Upload";
    const sheetsAPIKey = process.env.sheetsAPIKey;

    let embrapii = [];
    let fomento = [];
    let investidores = [];
    let earlyStage = [];
    let ventureCapital = [];

    const resp = await fetch(
      `https://sheets.googleapis.com/v4/spreadsheets/${sheetID}/values/'${sheetName}'?key=${sheetsAPIKey}`
    );
    const { values } = await resp.json();

    values.slice(1).forEach((element) => {
      const name = element[0];
      let newUrl = formatURL(element[2]);

      switch (name) {
        case "EMBRAPII":
          embrapii.push({ nome: element[1], url: newUrl });
          break;
        case "Fomento":
          fomento.push({ nome: element[1], url: newUrl });
          break;
        case "Investidores anjo":
          investidores.push({ nome: element[1], url: newUrl });
          break;
        case "Early stage":
          earlyStage.push({ nome: element[1], url: newUrl });
          break;
        case "Venture capital":
          ventureCapital.push({ nome: element[1], url: newUrl });
          break;
      }
    });

    this.items["EMBRAPII"] = embrapii;
    this.items["FOMENTO"] = fomento;
    this.items["INVESTIDORES ANJO"] = investidores;
    this.items["EARLY STAGE"] = earlyStage;
    this.items["VENTURE CAPITAL"] = ventureCapital;
  },

  methods: {
    select({ target }) {
      let text = target.innerText;
      this.selected = text;
      text = text.toUpperCase();
      this.selectedButtonSecondary = text;
    },
  },
};
</script>
