<template>
  <v-container>
    <div class="mt-5 text-h6 font-weight-regular">
      Marca DNA USP
      <v-divider />
      <v-container>
        <p class="body-2">
          A utilização da marca somente pode ocorrer após a autorização da
          Agência USP de Inovação. Os dados da empresa serão validados e,
          posteriormente, entraremos em contato para tratar da autorização de
          uso da marca DNA USP. Também enviaremos o logotipo em alta resolução e
          o manual de uso da marca.
        </p>
        <div class="d-flex justify-center">
          <img :src="require('@/vectors/dnausp.svg')" alt="DNA USP" />
        </div>
        <div class="mt-8">
          <p class="body-2">Sua empresa gostaria de receber a marca DNA USP?</p>
          <BooleanInput :value="wantsDna" label="sim" @input="setWantsDna" />

          <div v-if="wantsDna">
            <p class="body-2">
              Por qual email podemos entrar em contato para tratar da marca DNA
              USP?
            </p>

            <ShortTextInput
              :value="dnaContactEmail"
              label="Email de contato"
              hint="Este dado não será publicado."
              @input="setDnaContactEmail"
            />

            <p class="body-2 mt-8">
              Qual o nome do responsável por este email?
            </p>
            <ShortTextInput
              :value="dnaContactName"
              label="Nome"
              hint="Este dado não será publicado."
              @input="setDnaContactName"
            />
          </div>
        </div>
      </v-container>
    </div>

    <div class="mt-5 text-h6 font-weight-regular">
      Declaração final
      <v-divider></v-divider>
      <v-container>
        <p class="body-2">
          Declaro que as informações fornecidas são verdadeiras e que a empresa
          atende aos critérios estabelecidos
        </p>
        <BooleanInput
          :value="truthfulInformations"
          label="Estou de acordo com tal afirmação"
          @input="setTruthfulInformations"
        />

        <p class="body-2">
          Selecione as opções com as quais a empresa está de acordo
        </p>
        <div>
          <v-checkbox
            v-for="(option, index) in permissionOptions"
            :key="option"
            :label="option"
            :value="option"
            @change="onCheckboxClicked($event, index)"
          ></v-checkbox>
        </div>
        <p class="body-2">
          Informações sensíveis (como: dados pessoais, valores de investimento,
          faturamento e número de colaboradores) NÃO serão divulgadas por nenhum
          meio. Tais informações sensíveis serão utilizadas APENAS internamente
          e de forma agregada, sendo mantidas em sigilo.
        </p>
      </v-container>
    </div>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import BooleanInput from "@/components/CompanyForms/inputs/BooleanInput.vue";
import ShortTextInput from "@/components/CompanyForms/inputs/ShortTextInput.vue";

export default {
  components: {
    BooleanInput,
    ShortTextInput,
  },
  data: () => ({
    permissionOptions: [
      "Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa",
      "Permito a divulgação das informações públicas na plataforma Hub USPInovação",
      "Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP",
    ],
  }),
  computed: {
    ...mapGetters({
      wantsDna: "company_forms/wantsDna",
      dnaContactName: "company_forms/dnaContactName",
      dnaContactEmail: "company_forms/dnaContactEmail",
      truthfulInformations: "company_forms/truthfulInformations",
      permission: "company_forms/permission",
    }),
  },
  methods: {
    ...mapActions({
      setWantsDna: "company_forms/setWantsDna",
      setDnaContactName: "company_forms/setDnaContactName",
      setDnaContactEmail: "company_forms/setDnaContactEmail",
      setTruthfulInformations: "company_forms/setTruthfulInformations",
      setPermission: "company_forms/setPermission",
    }),
    onCheckboxClicked(item, index) {
      if (!item) {
        const option = this.permissionOptions[index];
        this.setPermission(
          this.permission.filter((element) => element !== option)
        );
      } else {
        this.setPermission([...this.permission, item]);
      }
    },
  },
};
</script>
