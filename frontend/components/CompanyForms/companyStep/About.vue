<template>
  <v-container>
    <v-form>
      <div class="mt-5 text-h6 font-weight-regular">
        Descrição da empresa
        <v-divider />
        <v-container>
          <LongTextInput
            :value="descriptionLong"
            label="Insira uma breve descrição da empresa"
            clearable
            hint="Descreva seu negócio, levando em consideração qual tipo de solução a mesma traz para o mercado. Busque deixar claro o posicionamento da mesma no mercado. Essa informação será divulgada."
            @input="setDescriptionLong"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Logo
        <v-divider />
        <v-container>
          <v-alert
            dense
            v-if="!isLogoInternal"
            class="mb-8"
            type="info"
            color="primary"
          >
            A empresa possui uma logotipo, mas ela está hospedada em um link externo que não conseguimos recuperar.
            Envie novamente, por favor.
          </v-alert>
          <ImageUploader
            class="mb-4"
            :value="logoFile"
            ref="logoUploader"
            label="Logotipo da empresa"
            hint="Selecione uma imagem em boa qualidade."
            @input="handleLogoUpload"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Tecnologias
        <v-divider />
        <v-container>
          <p class="body-2">
            Caso a sua empresa atue no desenvolvimento de tecnologias, indique
            as 5 principais. Não indique tecnologias utilizadas, apenas aquelas
            desenvolvidas internamente.
          </p>
          <MultipleInputs
            :value="technologies"
            input-label="Tecnologia"
            @input="setTechnologies"
          />
        </v-container>
      </div>
      <div class="mt-5 text-h6 font-weight-regular">
        Produtos/serviços
        <v-divider />
        <v-container>
          <MultipleInputs
            :value="services"
            input-label="Produto/Serviço"
            @input="setServices"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Sua empresa está alinhada a algum dos Objetivos de Desenvolvimento
        Sustentável (ODS)? Se sim, indique qual(is).
        <v-divider />
        <v-container>
          <Dropdown
            :value="ods"
            :options="odsList"
            multiple-option
            label="Objetivos de Desenvolvimento Sustentável"
            @input="setOds"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Site
        <v-divider />
        <v-container>
          <URLInput
            :value="url"
            label="Site"
            @input="setUrl"
          />
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Redes sociais
        <v-divider />
        <v-container>
          <MultipleInputs
            :value="socialMedias"
            input-label="Rede social"
            component="URLInput"
            @input="setSocialMedias"
          />
        </v-container>
      </div>
    </v-form>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import MultipleInputs from "@/components/CompanyForms/inputs/MultipleInputs.vue";
import LongTextInput from "@/components/CompanyForms/inputs/LongTextInput.vue";
import URLInput from "@/components/CompanyForms/inputs/URLInput.vue";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import ImageUploader from "@/components/CompanyForms/inputs/ImageUploader.vue";

export default {
  components: {
    MultipleInputs,
    LongTextInput,
    URLInput,
    Dropdown,
    ImageUploader,
  },
  data: () => ({
    odsList: [
      "1 - Erradicação da Pobreza",
      "2 - Fome Zero",
      "3 - Saúde e Bem Estar",
      "4 - Educação de Qualidade",
      "5 - Igualdade de Gênero",
      "6 - Água Potável e Saneamento",
      "7 - Energia Limpa e Acessível",
      "8 - Trabalho Decente e Crescimento Econômico",
      "9 - Indústria, Inovação e Infraestrutura",
      "10 - Redução das Desigualdades",
      "11 - Cidades e Comunidades Sustentáveis",
      "12 - Consumo e Produção Responsáveis",
      "13 - Ação Contra a Mudança Global do Clima",
      "14 - Vida na Água",
      "15 - Vida Terrestre",
      "16 - Paz, Justiça e Instituições Eficazes",
      "17 - Parcerias e Meios de Implementação",
    ],
    isLogoInternal: true,
  }),
  computed: {
    ...mapGetters({
      cnpj: "company_forms/cnpj",
      name: "company_forms/name",
      descriptionLong: "company_forms/descriptionLong",
      technologies: "company_forms/technologies",
      services: "company_forms/services",
      ods: "company_forms/odss",
      socialMedias: "company_forms/socialMedias",
      url: "company_forms/site",
      logo: "company_forms/logo",
      logoFile: "company_forms/logoFile",
    }),
  },
  created() {
    this.getExistentLogo();
  },
  methods: {
    ...mapActions({
      setDescriptionLong: "company_forms/setDescriptionLong",
      setTechnologies: "company_forms/setTechnologies",
      setServices: "company_forms/setServices",
      setOds: "company_forms/setOds",
      setSocialMedias: "company_forms/setSocialMedias",
      setUrl: "company_forms/setSite",
      setLogo: "company_forms/setLogo",
      setLogoFile: "company_forms/setLogoFile",
    }),
    async getExistentLogo() {
      if (!this.logo) return;
      try {
        const response = await fetch(this.logo);
        const blob = await response.blob();
        const type = blob.type.split("/")[1];
        const file = new File([blob], this.name + "." + type, { type: blob.type });

        this.setLogoFile(file);
        this.$refs.logoUploader.handleImage(file);
      } catch (error) {
        this.isLogoInternal = false;
        console.error("Error getting logo", error);
      }
    },
    async handleLogoUpload(logoImage) {
      if (!logoImage) {
        this.setLogoFile(null);
        this.setLogo("");
        return;
      }
      this.setLogoFile(logoImage);

      let cnpj_without_punctuation = this.cnpj.replace(/[^\d]+/g, '');
      let fileName = cnpj_without_punctuation + '.' + logoImage.name.split(".").pop();
      this.setLogo(fileName);
    },
  },
};
</script>
