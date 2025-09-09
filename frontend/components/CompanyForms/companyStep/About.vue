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
          <URLInput
            class="mb-4"
            :value="logo"
            label="URL do logotipo da empresa"
            hint="Cole aqui o link direto para o logotipo da empresa (ex: https://exemplo.com/logo.png)"
            @input="setLogo"
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

          <!-- Campos de Tecnologia -->
          <div v-for="(tech, index) in localTechnologies" :key="`tech-${index}`" class="d-flex align-center mb-2">
            <TextInputFormatted
              :value="tech"
              :label="`Tecnologia ${index + 1}`"
              @input="updateTechnology(index, $event)"
              class="flex-grow-1 mr-2"
            />
            <v-btn
              icon
              small
              color="error"
              @click="removeTechnology(index)"
              :disabled="localTechnologies.length <= 1"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <v-btn
            small
            color="primary"
            @click="addTechnology"
            :disabled="localTechnologies.length >= 10"
          >
            <v-icon left>mdi-plus</v-icon>
            Adicionar tecnologia
          </v-btn>
        </v-container>
      </div>

      <div class="mt-5 text-h6 font-weight-regular">
        Produtos/serviços
        <v-divider />
        <v-container>
          <!-- Campos de Produtos/Serviços -->
          <div v-for="(service, index) in localServices" :key="`service-${index}`" class="d-flex align-center mb-2">
            <TextInputFormatted
              :value="service"
              :label="`Produto/Serviço ${index + 1}`"
              @input="updateService(index, $event)"
              class="flex-grow-1 mr-2"
            />
            <v-btn
              icon
              small
              color="error"
              @click="removeService(index)"
              :disabled="localServices.length <= 1"
            >
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </div>

          <v-btn
            small
            color="primary"
            @click="addService"
            :disabled="localServices.length >= 10"
          >
            <v-icon left>mdi-plus</v-icon>
            Adicionar produto/serviço
          </v-btn>
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
          <div class="social-media-inputs">
            <!-- LinkedIn -->
            <div class="social-media-field mb-4">
              <div class="d-flex align-center mb-2">
                <v-icon color="#0077B5" class="mr-2">mdi-linkedin</v-icon>
                <span class="font-weight-medium">LinkedIn</span>
              </div>
              <URLInputValidated
                ref="linkedinInput"
                :value="linkedin"
                label="URL do perfil LinkedIn"
                placeholder="https://www.linkedin.com/company/sua-empresa"
                hint="Cole aqui o link completo do perfil da empresa no LinkedIn"
                url-type="linkedin"
                @input="setLinkedin"
              />
            </div>

            <!-- Instagram -->
            <div class="social-media-field mb-4">
              <div class="d-flex align-center mb-2">
                <v-icon color="#E4405F" class="mr-2">mdi-instagram</v-icon>
                <span class="font-weight-medium">Instagram</span>
              </div>
              <URLInputValidated
                ref="instagramInput"
                :value="instagram"
                label="URL do perfil Instagram"
                placeholder="https://www.instagram.com/sua-empresa"
                hint="Cole aqui o link completo do perfil da empresa no Instagram"
                url-type="instagram"
                @input="setInstagram"
              />
            </div>

            <!-- Facebook -->
            <div class="social-media-field mb-4">
              <div class="d-flex align-center mb-2">
                <v-icon color="#1877F2" class="mr-2">mdi-facebook</v-icon>
                <span class="font-weight-medium">Facebook</span>
              </div>
              <URLInputValidated
                ref="facebookInput"
                :value="facebook"
                label="URL da página Facebook"
                placeholder="https://www.facebook.com/sua-empresa"
                hint="Cole aqui o link completo da página da empresa no Facebook"
                url-type="facebook"
                @input="setFacebook"
              />
            </div>

            <!-- YouTube -->
            <div class="social-media-field mb-4">
              <div class="d-flex align-center mb-2">
                <v-icon color="#FF0000" class="mr-2">mdi-youtube</v-icon>
                <span class="font-weight-medium">YouTube</span>
              </div>
              <URLInputValidated
                ref="youtubeInput"
                :value="youtube"
                label="URL do canal YouTube"
                placeholder="https://www.youtube.com/c/sua-empresa"
                hint="Cole aqui o link completo do canal da empresa no YouTube"
                url-type="youtube"
                @input="setYoutube"
              />
            </div>
          </div>
        </v-container>
      </div>
    </v-form>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import LongTextInput from "@/components/CompanyForms/inputs/LongTextInput.vue";
import URLInput from "@/components/CompanyForms/inputs/URLInput.vue";
import TextInputFormatted from "@/components/CompanyForms/inputs/TextInputFormatted.vue";
import Dropdown from "@/components/CompanyForms/inputs/Dropdown.vue";
import URLInputValidated from "@/components/CompanyForms/inputs/URLInputValidated.vue";

export default {
  components: {
    LongTextInput,
    URLInput,
    TextInputFormatted,
    Dropdown,
    URLInputValidated,
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
    localTechnologies: [''],
    localServices: [''],
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
      linkedin: "company_forms/linkedin",
      instagram: "company_forms/instagram",
      facebook: "company_forms/facebook",
      youtube: "company_forms/youtube",
    }),
  },
  watch: {
    technologies: {
      handler(newValue) {
        if (Array.isArray(newValue) && newValue.length > 0) {
          this.localTechnologies = [...newValue];
        } else {
          this.localTechnologies = [''];
        }
      },
      immediate: true
    },
    services: {
      handler(newValue) {
        if (Array.isArray(newValue) && newValue.length > 0) {
          this.localServices = [...newValue];
        } else {
          this.localServices = [''];
        }
      },
      immediate: true
    }
  },
  methods: {
    ...mapActions({
      setDescriptionLong: "company_forms/setDescriptionLong",
      setTechnologies: "company_forms/setTechnologies",
      setServices: "company_forms/setServices",
      setOds: "company_forms/setOdss",
      setSocialMedias: "company_forms/setSocialMedias",
      setUrl: "company_forms/setSite",
      setLogo: "company_forms/setLogo",
      setLogoFile: "company_forms/setLogoFile",
      setLinkedin: "company_forms/setLinkedin",
      setInstagram: "company_forms/setInstagram",
      setFacebook: "company_forms/setFacebook",
      setYoutube: "company_forms/setYoutube",
    }),

    updateTechnology(index, value) {
      this.localTechnologies[index] = value;
      this.saveTechnologies();
    },

    addTechnology() {
      if (this.localTechnologies.length < 10) {
        this.localTechnologies.push('');
      }
    },

    removeTechnology(index) {
      if (this.localTechnologies.length > 1) {
        this.localTechnologies.splice(index, 1);
        this.saveTechnologies();
      }
    },

    saveTechnologies() {
      const filteredTechnologies = this.localTechnologies
        .filter(tech => tech && tech.toString().trim() !== '')
        .map(tech => tech.toString().trim());
      this.setTechnologies(filteredTechnologies);
    },

    updateService(index, value) {
      const sanitizedValue = value ? value.toString().trim() : '';
      this.localServices[index] = sanitizedValue;
      this.saveServices();
    },

    addService() {
      if (this.localServices.length < 10) {
        this.localServices.push('');
      }
    },

    removeService(index) {
      if (this.localServices.length > 1) {
        this.localServices.splice(index, 1);
        this.saveServices();
      }
    },

    saveServices() {
      const filteredServices = this.localServices
        .filter(service => service && service.toString().trim() !== '')
        .map(service => service.toString().trim());
      this.setServices(filteredServices);
    },

    validateStep() {
      const errors = [];

      if (this.$refs.linkedinInput && !this.$refs.linkedinInput.validate()) {
        errors.push('LinkedIn: URL inválida');
      }
      if (this.$refs.instagramInput && !this.$refs.instagramInput.validate()) {
        errors.push('Instagram: URL inválida');
      }
      if (this.$refs.facebookInput && !this.$refs.facebookInput.validate()) {
        errors.push('Facebook: URL inválida');
      }
      if (this.$refs.youtubeInput && !this.$refs.youtubeInput.validate()) {
        errors.push('YouTube: URL inválida');
      }

      return {
        isValid: errors.length === 0,
        errors: errors
      };
    },
  },
};
</script>
