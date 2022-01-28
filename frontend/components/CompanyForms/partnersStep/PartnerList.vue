<template>
  <div v-if="hasPartners" class="d-flex flex-wrap">
    <v-card
      v-for="(partner, index) in formattedPartners"
      :key="partner.nusp"
      class="mx-2 my-2"
    >
      <v-card-title>{{ partner.name }}</v-card-title>
      <v-card-subtitle>NUSP: {{ partner.nusp }}</v-card-subtitle>
      <v-card-text>
        Email: {{ partner.email }}<br />
        Telefone: {{ partner.phone }}<br />
        Vínculo: {{ partner.bond }}<br />
        Unidade: {{ partner.unity }}
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darker-1" text @click="updatePartner(index)"
          >Atualizar</v-btn
        >
        <v-btn color="red darker-1" text @click="removePartner(index)"
          >Excluir</v-btn
        >
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </div>
  <p v-else class="body-2">Não existem sócios cadastrados.</p>
</template>

<script>
export default {
  props: {
    partners: {
      type: Array,
      required: true,
    },
  },
  computed: {
    hasPartners() {
      return this.partners.length > 0;
    },
    formattedPartners() {
      return this.partners.map((partner) => {
        const phone = this.formatPartnerPhone(partner.phone);
        const nusp = this.formatPartnerNusp(partner.nusp);
        return { ...partner, nusp, phone };
      });
    },
  },
  methods: {
    formatPartnerPhone(phone) {
      const phoneSplitted = phone.split("");
      return phoneSplitted
        .map((element, index) => {
          const isNumber = " -()+".split("").every((char) => element !== char);

          if (index < phoneSplitted.length - 3 && isNumber) return "*";

          return element;
        })
        .join("");
    },
    formatPartnerNusp(nusp) {
      const nuspSplitted = nusp.split("");
      return nuspSplitted
        .map((element, index) =>
          index < nuspSplitted.length - 3 ? "*" : element
        )
        .join("");
    },
    updatePartner(index) {
      this.$emit("update", index);
    },
    removePartner(index) {
      this.$emit("remove", index);
    },
  },
};
</script>
