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
            label="Nome fantasia da empresa"
            hint="A empresa será listada no Hub USPInovação a partir do nome fantasia da mesma."
            @input="setName"
          />
          <ShortTextInput
            class="mb-4"
            :value="corporateName"
            label="Razão social da empresa"
            @input="setCorporateName"
          />
          <MaskInput
            class="mb-4"
            :value="year"
            mask="####"
            label="Ano de fundação"
            @input="setYear"
          />
          <MaskInput
            :value="cnpj"
            label="CNPJ"
            mask="##.###.###/####-##"
            @input="setCnpj"
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
            label="CNAE"
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
            component="ShortTextInput"
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
          <p class="body-2">Cidades sede</p>
          <MultipleInputs
            :value="city"
            input-label="Cidade sede"
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
            label="CEP"
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

export default {
  components: {
    MaskInput,
    MultipleInputs,
    ShortTextInput,
    Dropdown,
  },
  data: () => ({
    allStates: [],
  }),
  computed: {
    ...mapGetters({
      name: "company_forms/name",
      corporateName: "company_forms/corporateName",
      year: "company_forms/year",
      cnpj: "company_forms/cnpj",
      cnae: "company_forms/cnae",
      phones: "company_forms/phones",
      emails: "company_forms/emails",
      address: "company_forms/address",
      venue: "company_forms/venue",
      neighborhood: "company_forms/neighborhood",
      city: "company_forms/city",
      state: "company_forms/state",
      cep: "company_forms/cep",
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
      setCnpj: "company_forms/setCnpj",
      setCnae: "company_forms/setCnae",
      setPhones: "company_forms/setPhones",
      setEmails: "company_forms/setEmails",
      setVenue: "company_forms/setVenue",
      setNeighborhood: "company_forms/setNeighborhood",
      setCity: "company_forms/setCity",
      setState: "company_forms/setState",
      setCep: "company_forms/setCep",
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
  },
};
</script>
