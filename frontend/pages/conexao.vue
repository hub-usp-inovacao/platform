<template>
  <div class="background">
    <div>
      <Panel
        title="Conexão USP"
        description="Este Programa da Agência USP de Inovação tem por objetivo oferecer a intermediação e o contato entre parceiros (empresas, entidades sem fins lucrativos e governo) e os pesquisadores da Universidade de São Paulo.
                    Neste programa, os parceiros apresentam suas demandas para que identifiquemos pesquisadores na Universidade que tenham a soluções ou propostas de projetos de pesquisa que atendam estas necessidades."
        no-search
      />
    </div>

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
            label="Nome"
            :rules="rules.value"
          />
          <MaskInput
            v-model="conexao.org.cnpj"
            label="CNPJ"
            mask="##.###.###/####-##"
            :rule="/^\d{2}\.\d{3}\.\d{3}\/\d{4}\-\d{2}$/"
          />
          <v-radio-group
            v-model="conexao.org.sensitiveData"
            label="Você deseja manter sigilo em relação ao nome da organização?"
            :rules="rules.value"
          >
            <v-radio value="Sim" label="Sim" />
            <v-radio value="Não" label="Não" />
          </v-radio-group>

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
            v-model="conexao.org.email"
            label="E-mail"
            placeholder="E-mail da organização"
            :rules="rules.value"
          />
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
            label="Cidade"
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
              sua demanda.
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
          <v-file-input
            multiple
            chips
            label="Caso necessário, coloque as fotos relacionadas a sua demanda"
            @change="uploadImage"
          ></v-file-input>
          <div>
            <v-radio-group
              v-model="conexao.demand.expectation"
              label="Indique sua principal expectativa em relação a solução da
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
                Qual o perfil do pesquisador o(a) senhor(a) acredita poder sanar
                suas necessidades? Ou seja, qual deveria ser sua especialização,
                em sua opinião?
              </legend>
              <v-select
                v-model="conexao.demand.wantedProfile"
                :items="cnpqAreas"
                label="Escolha um perfil"
                :rules="rules.value"
                clearable
              >
              </v-select>
            </v-col>
          </v-row>
          <div>
            <v-radio-group
              v-model="conexao.demand.necessity"
              label="Qual a sua necessidade em relação a esses pesquisadores?"
              :rules="rules.value"
              @change="enableOtherOption('demand', 'necessity')"
            >
              <v-radio
                v-for="(option, i) of radioButtonData[3]"
                :key="i"
                :value="option"
                :label="option"
              />
              <v-radio
                value="Identificação de especialista para assessoria técnica"
              >
                <template v-slot:label>
                  <p class="my-auto">
                    Identificação de especialista para assessoria técnica na
                    área de
                  </p>
                  <div class="mb-n3 ml-2">
                    <v-select
                      v-model="selectedArea"
                      :disabled="!isRadioSelectLabelOn"
                      :items="cnpqAreas"
                      label="Escolha uma área"
                      :rules="isRadioSelectLabelOn ? rules.value : []"
                      clearable
                      dense
                    />
                  </div>
                </template>
              </v-radio>
              <v-radio label="Outro, qual?" value="Outro" />
            </v-radio-group>
            <v-row v-if="isOtherDemandEnable">
              <v-col class="mt-n5 pt-0" cols="6">
                <v-text-field
                  v-model="conexao.demand.necessityOther"
                  :rules="rules.value"
                  placeholder="Outro, qual?"
                  autofocus
                />
              </v-col>
            </v-row>
          </div>
        </v-container>
        <v-checkbox
          v-model="confirmation"
          label="Concordo com todas as normas e funcionamento do Programa Conexão USP"
          :rules="rules.confirmation"
        />

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
    conexao: {
      personal: {
        email: "",
        name: "",
        represent: "",
      },
      org: {
        email: "",
        name: "",
        cnpj: "",
        sensitiveData: "",
        size: "",
        phone: "",
        address: "",
        city: "",
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
      },
    },
    images: null,
    radioButtonData: [
      ["Empresa", "Organização sem fins lucatrivos", "Governo", "Consultoria"],
      ["Pequena", "Média", "Grande"],
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
        "Licenciamento de patentes",
        "Identificação de novas tecnologias",
        "Auxílio técnico para validação de equipamentos e produtos",
        "Utilização de laboratórios para ensaios e testes",
        "Desenvolvimento de P&D em parceria",
        "Identificação de startup para investimento ou contratação de serviços",
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
    uploadImage(e) {
      this.images = e;
    },
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
    sendImages() {
      let images = this.images;
      if (images) {
        images.forEach((image) => {
          let formData = new FormData();

          formData.append("requestId", this.conexao.requestId);
          formData.append("image", image);
          this.$axios.$post("/conexao/image", formData);
        });
      }
    },

    async submit() {
      this.loading = true;
      const valid = this.$refs.form.validate();
      if (valid) {
        this.conexao.requestId = self.crypto.randomUUID();
        this.dataChecking();
        try {
          await this.$axios.$post("/conexao", { conexao: this.conexao });
          this.sendImages();
          alert(
            "Formulário enviado com sucesso! Em breve a equipe da AUSPIN entrará em contato com você."
          );
          location.reload();
        } catch (error) {
          console.log(error);
        }
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
