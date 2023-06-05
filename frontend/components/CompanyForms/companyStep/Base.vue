<template>
  <v-container>
    <v-form>
      <div class="mt-5 text-h6 font-weight-regular">
        Dados básicos
        <v-divider />
        <v-container>
          <ShortTextInput
            class="mb-8"
            :value="name"
            label="Nome fantasia da empresa *"
            hint="A empresa será listada no Hub USPInovação a partir do nome fantasia da mesma."
            @input="setName"
          />
          <ShortTextInput
            class="mb-4"
            :value="corporateName"
            label="Razão social da empresa *"
            @input="setCorporateName"
          />
          <MaskInput
            class="mb-4"
            :value="year"
            mask="####"
            label="Ano de fundação *"
            @input="setYear"
          />
          <MaskInput
            :value="cnpj"
            label="CNPJ *"
            mask="##.###.###/####-##"
            @input="setCnpj"
            :disabled="true"
          />
          <Dropdown
            :value="registry_status"
            label="Situação cadastral"
            :options="allRegistryStatus"
            @input="setRegistryStatus"
          />
          <Dropdown
            :value="size"
            :options="sizeList"
            label="Porte da empresa"
            hint="Escolha de acordo com o Comprovante de Inscrição e de Situação Cadastral"
            @input="setSize"
          />
        </v-container>
      </div>

      <div class="mt-8 text-h6 font-weight-regular">
        Código e Descrição da Natureza Jurídica da empresa
        <v-divider />
        <v-container>
          <legend class="body-2 mb-5">
            Para consultar o código e a natureza jurídica da empresa, cole o CNPJ da empresa no
            <a
              href="http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Solicitacao.asp?cnpj"
              target="_blank"
              >site da receita</a
            >. Depois copie o CÓDIGO e a DESCRIÇÃO DA NATUREZA JURÍDICA e cole
            como resposta nos campos abaixo. Insira apenas o código no formato
            "000-0" e a descrição no formato de texto. A lista de códigos e naturezas jurídicas
            pode ser encontrada na
            <a
              href="https://concla.ibge.gov.br/images/concla/documentacao/CONCLA-TNJ2021-EstruturaDetalhada.pdf"
              target="_blank"
              >tabela de naturezas jurídicas do IBGE</a
            >.
          </legend>
          <PairOfNumberAndText
            :value="company_nature"
            labelOne="Código"
            labelTwo="Natureza Jurídica"
            placeholderOne="000-0"
            separator=" - "
            @input="setCompanyNature"
            @changeOne="matchCompanyNature('one', $event)"
            @changeTwo="matchCompanyNature('two', $event)"
            ref="companyNatureField"
          />
        </v-container>
      </div>

      <div class="mt-8 text-h6 font-weight-regular">
        CNAE (Classificação Nacional de Atividades Econômicas) da empresa
        <v-divider />
        <v-container>
          <legend class="body-2 mb-5">
            Para consultar o CNAE cole o CNPJ da empresa no
            <a
              href="http://servicos.receita.fazenda.gov.br/Servicos/cnpjreva/Cnpjreva_Solicitacao.asp?cnpj"
              target="_blank"
              >site da receita</a
            >. Depois copie o CÓDIGO DA ATIVIDADE ECONÔMICA PRINCIPAL e cole
            como resposta desta pergunta. Insira apenas o código no formato
            00.00-0-00. Essa informação é essencial para a categorização da
            empresa.
          </legend>
          <MaskInput
            :value="cnae"
            label="CNAE *"
            mask="##.##-#-##"
            :rule="/^\d{2}\.\d{2}-\d(-\d{2})?$/"
            @input="setCnae"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Contato
        <v-divider />
        <v-container>
          <p class="body-2">Telefones comerciais</p>
          <MultipleInputs
            :value="phones"
            input-label="Telefone comercial"
            component="MaskInput"
            mask="(##) #########"
            @input="setPhones"
          />
          <p class="body-2 mt-5">Emails</p>
          <MultipleInputs
            :value="emails"
            input-label="Email"
            @input="setEmails"
          />
          <ShortTextInput
            class="mt-8"
            :value="venue"
            label="Endereço"
            @input="setVenue"
          />
          <ShortTextInput
            :value="neighborhood"
            label="Bairro"
            @input="setNeighborhood"
          />
          <ShortTextInput
            :value="city"
            label="Cidade sede"
            @input="setCity"
          />
          <Dropdown
            class="mt-8"
            :value="state"
            label="Estado"
            :options="allStates"
            @input="setState"
          />
          <MaskInput
            :value="cep"
            label="CEP *"
            mask="#####-###"
            :rule="/\d{5}-\d{3}/"
            @input="setCep"
          />
        </v-container>
      </div>
    </v-form>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import MultipleInputs from "@/components/CompanyForms/inputs/MultipleInputs.vue";
import MaskInput from "@/components/CompanyForms/inputs/MaskInput.vue";
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import PairOfNumberAndText from "@/components/CompanyForms/inputs/PairOfNumberAndText.vue";

export default {
  components: {
    MaskInput,
    MultipleInputs,
    ShortTextInput,
    Dropdown,
    PairOfNumberAndText,
  },
  data: () => ({
    allStates: [],
    allRegistryStatus: [
      "Ativa",
      "Ativa Não Regular",
      "Baixada",
      "Inapta",
      "Nula",
      "Suspensa"
    ],
    sizeList: ["MEI", "ME", "EPP", "DEMAIS"]
  }),
  computed: {
    ...mapGetters({
      name: "company_forms/name",
      corporateName: "company_forms/corporateName",
      year: "company_forms/year",
      size: "company_forms/size",
      cnpj: "company_forms/cnpj",
      cnae: "company_forms/cnae",
      registry_status: "company_forms/registry_status",
      phones: "company_forms/phones",
      emails: "company_forms/emails",
      address: "company_forms/address",
      venue: "company_forms/venue",
      neighborhood: "company_forms/neighborhood",
      city: "company_forms/city",
      state: "company_forms/state",
      cep: "company_forms/cep",
      company_nature: "company_forms/company_nature",
    }),
  },
  created() {
    this.getStates();
  },
  methods: {
    ...mapActions({
      setName: "company_forms/setName",
      setCorporateName: "company_forms/setCorporateName",
      setYear: "company_forms/setYear",
      setSize: "company_forms/setSize",
      setCnpj: "company_forms/setCnpj",
      setCnae: "company_forms/setCnae",
      setRegistryStatus: "company_forms/setRegistryStatus",
      setPhones: "company_forms/setPhones",
      setEmails: "company_forms/setEmails",
      setVenue: "company_forms/setVenue",
      setNeighborhood: "company_forms/setNeighborhood",
      setCity: "company_forms/setCity",
      setState: "company_forms/setState",
      setCep: "company_forms/setCep",
      setCompanyNature: "company_forms/setCompanyNature",
    }),
    async getStates() {
      const response = await fetch(
        "https://servicodados.ibge.gov.br/api/v1/localidades/estados"
      );
      const ufs = await response.json();
      this.allStates = ufs
        .map(({ nome, sigla }) => `${nome} (${sigla})`)
        .sort();
    },
    findReverse(association, value) {
      const key = Object.keys(association).find(
        (key) => association[key].localeCompare(value) === 0
      );
      return key;
    },
    matchCompanyNature(field, value) {
      const association = this.$company_nature;

      let complement_value = undefined;
      if (field === "one") {
        complement_value = association[value];
      } else {
        complement_value = this.findReverse(association, value);
      }

      const reverse_field = field === "one" ? "two" : "one";
      if (complement_value) {
        this.$refs.companyNatureField.set(reverse_field, complement_value);
      }
    },
  },
};
</script>
