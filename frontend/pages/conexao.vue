<template>
  <div class="background">
    <v-row style="background: white">
      <v-col>
        <v-img
          class="mx-auto"
          width="300"
          height="128"
          :src="require('@/vectors/conexao.svg')"
          alt="logo conexao"
        />
      </v-col>
    </v-row>
    <div>
      <Panel
        title="Conexão USP"
        description="O programa Conexão USP é uma iniciativa da Agência USP de Inovação, que tem por objetivo intermediar o contato de empresas, entidades sem fins lucrativos e órgãos governamentais com os pesquisadores da Universidade de São Paulo, visando estabelecer parcerias nas áreas de pesquisa e inovação.
Para participar, cadastre sua demanda no formulário abaixo, e com base nas informações fornecidas nossa equipe irá buscar na Universidade as competências técnico-científicas que melhor atendam suas necessidades. Todas as informações inseridas neste formulário serão utilizadas única e exclusivamente para uso interno da AUSPIN, e não serão divulgadas em nenhum meio público."
        no-search
      />
    </div>

    <v-container class="pa-10">
      <v-form ref="form">
        <div class="my-2 text-h5 font-weight-regular">
          Dados Pessoais
          <v-divider />
        </div>
        <v-container>
          <v-text-field
            v-model="conexao.personal.email"
            label="E-mail"
            placeholder="Seu e-mail"
            :rules="rules.value"
          />
          <v-text-field
            v-model="conexao.personal.name"
            label="Nome Completo"
            :rules="rules.value"
          />
          <div>
            <v-radio-group
              v-model="conexao.personal.represent"
              label="Você representa uma:"
              :rules="rules.value"
              @change="enableOtherOption('personal', 'represent')"
            >
              <v-radio
                v-for="(option, i) of radioButtonData[0]"
                :key="i"
                :value="option"
                :label="option"
              />
              <v-radio label="Outro, qual?" value="Outro" />
            </v-radio-group>
            <v-row v-if="isOtherRpresentEnable">
              <v-col class="mt-n5 pt-0" cols="6">
                <v-text-field
                  v-model="conexao.personal.representOther"
                  :rules="rules.value"
                  placeholder="Outro, qual?"
                  autofocus
                />
              </v-col>
            </v-row>
          </div>
        </v-container>

        <div class="my-2 text-h5 font-weight-regular">
          Organização
          <v-divider />
        </div>
        <v-container>
          <v-text-field
            v-model="conexao.org.name"
            label="Razão social"
            :rules="rules.value"
          />
          <MaskInput
            v-model="conexao.org.cnpj"
            label="CNPJ"
            mask="##.###.###/####-##"
            :rule="/^\d{2}\.\d{3}\.\d{3}\/\d{4}\-\d{2}$/"
          />
          <v-row>
            <v-col sm="12" md="6">
              <v-radio-group
                v-model="conexao.org.size"
                label="Qual o porte da organização:"
                :rules="rules.value"
              >
                <v-radio
                  v-for="(option, i) of radioButtonData[1]"
                  :key="i"
                  :value="option"
                  :label="option"
                />
              </v-radio-group>
            </v-col>
            <v-col sm="12" md="6">
              <table>
                <tr>
                  <th>Porte</th>
                  <th>Indústria de Transformação</th>
                  <th>Outros</th>
                </tr>
                <tr>
                  <td>Microempresas</td>
                  <td>&le; 19 pessoas</td>
                  <td>&le; 9 pessoas</td>
                </tr>
                <tr>
                  <td>Pequena Empresa</td>
                  <td>de 20 a 99 pessoas</td>
                  <td>de 10 a 49 pessoas</td>
                </tr>
                <tr>
                  <td>Média Empresa</td>
                  <td>de 100 a 499 pessoas</td>
                  <td>de 50 a 99 pessoas</td>
                </tr>
                <tr>
                  <td>Grande Empresa</td>
                  <td>&ge; 500 pessoas</td>
                  <td>&ge; 100 pessoas</td>
                </tr>
              </table>
            </v-col>
          </v-row>
          <v-text-field
            v-model="conexao.org.phone"
            label="Telefone de Contato"
            :rules="rules.value"
          />
          <v-text-field
            v-model="conexao.org.address"
            label="Endereço"
            :rules="rules.value"
          />
          <v-text-field
            v-model="conexao.org.city"
            label="Cidade/UF"
            :rules="rules.value"
          />
          <v-text-field
            v-model="conexao.org.site"
            label="Site da organização"
            :rules="rules.value"
          />
        </v-container>

        <div class="my-2 text-h5 font-weight-regular">
          Demanda
          <v-divider />
        </div>
        <v-container>
          <div>
            <legend text class="legendColor">
              Em qual das atividades econômicas sua demanda se encaixa?
            </legend>
            <v-row>
              <v-col>
                <v-select
                  v-model="conexao.demand.cnae.major"
                  label="Área Primária"
                  :items="cnaeMajors"
                  :rules="rules.value"
                  clearable
                />
              </v-col>
              <v-col>
                <v-select
                  v-model="conexao.demand.cnae.minor"
                  label="Área Secundária"
                  :items="cnaeMinors(conexao.demand.cnae.major)"
                  no-data-text="Selecione uma área primária antes"
                  :rules="rules.value"
                  clearable
                />
              </v-col>
            </v-row>
          </div>
          <div>
            <legend class="legendColor">
              Faça um breve resumo de sua demanda (Descreva o seu desafio e/ou
              problema para o qual busca uma solução) e cite qual o objetivo de
              sua demanda. Informe o objetivo para o qual pretende utilizar os
              resultados. Solicitamos que nenhuma informação de natureza
              sigilosa seja compartilhada neste campo
            </legend>
            <v-textarea
              v-model="conexao.demand.description"
              clearable
              dense
              filled
              auto-grow
              :rows="textAreaSize"
              hint="Máximo 500 palavras"
              :rules="rules.textarea"
            ></v-textarea>
          </div>
          <!--
          <v-file-input
            multiple
            chips
            label="Caso necessário, coloque arquivos relacionadas a sua demanda"
            @change="uploadImage"
          ></v-file-input>
          -->
          <div>
            <v-radio-group
              v-model="conexao.demand.expectation"
              label="Indique sua principal expectativa em relação à solução da
                demanda:"
              :rules="rules.value"
              @change="enableOtherOption('demand', 'expectation')"
            >
              <v-radio
                v-for="(option, i) of radioButtonData[2]"
                :key="i"
                :value="option"
                :label="option"
              />
              <v-radio label="Outro, qual?" value="Outro" />
            </v-radio-group>
            <v-row v-if="isOtherExpectationEnable">
              <v-col class="mt-n5 pt-0" cols="6">
                <v-text-field
                  v-model="conexao.demand.expectationOther"
                  :rules="rules.value"
                  placeholder="Outro, qual?"
                  autofocus
                />
              </v-col>
            </v-row>
          </div>
          <v-row>
            <v-col>
              <legend class="legendColor">
                Informe a área de conhecimento / especialidade do pesquisador
                que melhor atenda às suas necessidade
              </legend>
              <v-select
                v-model="conexao.demand.wantedProfile"
                :items="cnpqAreas"
                label="Escolha um perfil"
                :rules="rules.value"
                clearable
                multiple
              >
              </v-select>
            </v-col>
          </v-row>
          <div>
            <legend class="legendColor">
              Qual a sua necessidade em relação a esses pesquisadores?
            </legend>
            <v-combobox
              v-model="conexao.demand.necessity"
              :items="radioButtonData[3]"
              label="Escolha as necessidades, ou digite outras caso necessário"
              multiple
              :rules="rules.value"
              chips
            ></v-combobox>
          </div>

          <div>
            <legend class="legendColor">
              Como ficou sabendo do Hub USP Inovação ? (opcional)
            </legend>
            <v-combobox
              v-model="conexao.demand.knownForm"
              :items="radioButtonData[4]"
              label="Escolha entre as opções disponíveis, ou digite outras caso necessário"
              multiple
              chips
            ></v-combobox>
          </div>
        </v-container>
        <v-checkbox
          v-model="confirmation"
          label="Concordo com as diretrizes de uso e funcionamento do Programa Conexão USP."
          :rules="rules.confirmation"
        >
          <template v-slot:label>
            <div>
              Concordo com as
              <a
                target="_blank"
                href="http://www.inovacao.usp.br/conexaousp/"
                @click.stop
              >
                diretrizes
              </a>
              de uso e funcionamento do Programa Conexão USP..
            </div>
          </template>
        </v-checkbox>
        <v-row>
          <v-col class="text-center">
            <v-btn
              color="tertiary"
              width="100"
              text
              :loading="loading"
              @click="submit"
            >
              Enviar
            </v-btn>
          </v-col>
        </v-row>
      </v-form>
    </v-container>
  </div>
</template>

<script>
import Panel from "@/components/first_level/Panel.vue";
import MaskInput from "@/components/FormsInputs/MaskInput.vue";

export default {
  components: {
    Panel,
    MaskInput,
  },
  data: () => ({
    enabled: false,
    conexao: {
      personal: {
        email: "",
        name: "",
        represent: "",
      },
      org: {
        name: "",
        cnpj: "",
        size: "",
        phone: "",
        address: "",
        city: "",
        site: "",
      },
      demand: {
        cnae: {
          major: "",
          minor: "",
        },
        description: "",
        expectation: "",
        wantedProfile: "",
        necessity: "",
        knownForm: "",
      },
    },
    images: null,
    radioButtonData: [
      [
        "Empresa",
        "Organização sem fins lucatrivos",
        "Órgão governamental",
        "Instituição de Pesquisa",
      ],
      ["Microempresa", "Pequena", "Média", "Grande"],
      [
        "Melhoria de Produto",
        "Melhoria em Processo",
        "Melhoria em Serviço",
        "Melhoria em Tecnologia",
        "Novo produto",
        "Novo processo",
        "Novo serviço",
        "Nova tecnologia",
        "Prestação de serviço e/ou consultoria especializada",
      ],
      [
        "Licenciamento de tecnologias",
        "Identificação de novas tecnologias",
        "Auxílio técnico para validação de equipamentos e produtos",
        "Utilização de laboratórios/equipamentos para ensaios e testes",
        "Desenvolvimento de P&D em parceria",
        "Identificação de startup para investimento ou contratação de serviços",
        "Identificação de especialista para assessoria técnica",
      ],
      [
        "Indicação pessoal",
        "Notícia na imprensa",
        "Evento (palestra, webinar, etc.)",
        "E-mail/newsletter",
        "Linkedin",
        "Facebook",
        "Twitter",
        "Instagram",
        "Material impresso (cartaz, folder, etc.)",
      ],
    ],
    rules: {
      value: [(f) => (f || "").length > 0 || "Campo obrigatório"],
      textarea: [
        (f) => (f || "").length > 0 || "Campo obrigatório",
        (f) => (f || "").split(" ").length < 500 || "Máximo de 500 palavras!",
      ],
      confirmation: [(f) => f || "Você deve aceitar para continuar!"],
    },
    selectedArea: "",
    loading: false,
    confirmation: false,
  }),
  computed: {
    textAreaSize() {
      return this.$breakpoint.smAndDown ? 16 : 5;
    },
    cnaeMajors() {
      return Object.keys(this.$reverseCNAE);
    },
    cnpqAreas() {
      return this.$knowledgeAreas.map((area) => {
        let { name } = area;
        name = name.replace(/Ciências (da )?/, "");
        return name;
      });
    },
    isRadioSelectLabelOn() {
      return (
        this.conexao.demand.necessity ==
        "Identificação de especialista para assessoria técnica"
      );
    },
    isOtherRpresentEnable() {
      return this.conexao.personal.represent == "Outro";
    },
    isOtherExpectationEnable() {
      return this.conexao.demand.expectation == "Outro";
    },
    isOtherDemandEnable() {
      return this.conexao.demand.necessity == "Outro";
    },
  },
  methods: {
    /*
    uploadImage(e) {
      this.images = e;
    },
    */
    enableOtherOption(model, value) {
      if (this.conexao[model][value] == "Outro") {
        this.conexao[model][`${value}Other`] = "";
      } else {
        delete this.conexao[model][`${value}Other`];
      }
    },
    cnaeMinors(major) {
      if (!major) return undefined;
      return this.$reverseCNAE[major].map(({ minor }) => minor);
    },
    dataChecking() {
      if (this.isOtherRpresentEnable) {
        this.conexao.personal.represent = this.conexao.personal.representOther;
        delete this.conexao.personal.representOther;
      }

      if (this.isOtherExpectationEnable) {
        this.conexao.demand.expectation = this.conexao.demand.expectationOther;
        delete this.conexao.demand.expectationOther;
      }

      if (this.isOtherDemandEnable) {
        this.conexao.demand.necessity = this.conexao.demand.necessityOther;
        delete this.conexao.demand.necessityOther;
      }

      if (
        this.conexao.demand.necessity ==
        "Identificação de especialista para assessoria técnica"
      )
        this.conexao.demand.necessity += " na área de " + this.selectedArea;
    },
    /*
    async sendImages() {
      let images = this.images;
      if (images) {
        for (const image in images) {
          let formData = new FormData();

          formData.append("requestId", this.conexao.requestId);
          formData.append("image", image);
          await this.$axios.$post("/conexao/image", formData);
        }
      }
    },
    */

    validate() {
      const personal = Object.keys(this.conexao.personal);
      const org = Object.keys(this.conexao.org);
      const demand = Object.keys(this.conexao.demand);
      const cnae = Object.keys(this.conexao.demand.cnae);
      const text = this.conexao.demand.description;
      let isvalid = false;
      let errors = [];

      personal.forEach((e, index) => {
        if (this.conexao.personal[e] == "") {
          errors.push("personal_" + Object.keys(this.conexao.personal)[index]);
        }
      });

      org.forEach((e, index) => {
        if (this.conexao.org[e] == "") {
          errors.push("organization_" + Object.keys(this.conexao.org)[index]);
        }
      });

      demand.forEach((e, index) => {
        if (e != "cnae") {
          if (this.conexao.demand[e] == "") {
            errors.push("demand_" + Object.keys(this.conexao.demand)[index]);
          }
        }
      });

      cnae.forEach((e, index) => {
        if (this.conexao.demand.cnae[e] == "") {
          errors.push("cnae_" + Object.keys(this.conexao.demand.cnae)[index]);
        }
      });

      if (text.length == "") {
        errors.push("Campo de texto vazio");
      }
      if (text.split(" ").length > 500) {
        errors.push("Limite de 500 caracteres");
      }

      if (errors.length == 0) {
        isvalid = true;
      }
      return { isvalid, errors };
    },
    async submit() {
      const { isvalid, errors } = this.validate();
      this.loading = true;
      this.$refs.form.validate();
      if (isvalid) {
        this.conexao.requestId = self.crypto.randomUUID();
        this.dataChecking();
        try {
          await this.$axios.$post("/conexao", { conexao: this.conexao });
          // await this.sendImages();
          alert(
            "Formulário enviado com sucesso! Em breve a equipe da AUSPIN entrará em contato com você."
          );
          location.reload();
        } catch (error) {
          alert(
            "Ocorreu um erro na submissão. Tente novamente mais tarde ou entre em contato."
          );
        }
      } else {
        alert(
          "Há erros no formulário, por favor corrijia-os e submeta novamente"
        );
      }
      this.loading = false;
    },
  },
};
</script>

<style scoped>
.legendColor {
  color: rgba(0, 0, 0, 0.6);
}
table {
  text-align: center;
}
th,
td {
  padding: 5px;
}
</style>
