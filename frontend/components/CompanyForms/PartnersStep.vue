<template>
  <v-container>
    <h1 class="text-h4 mt-8 mb-8">Sócios</h1>
    <PartnerList
      :partners="partners"
      @update="openModalToUpdate"
      @remove="removePartner"
    />

    <NewPartnerModal
      v-model="dialog"
      :updated-partner="updatedPartner"
      @save="includePartner"
      @update="updatePartner"
      @close="dialog = false"
    />

    <v-row justify="center">
      <v-btn
        v-if="!limitReached"
        class="my-8"
        color="primary"
        dark
        @click="openModalToSave"
      >
        Adicionar sócio
      </v-btn>
      <p v-else class="body-2 my-8">É possível cadastrar até 5 sócios.</p>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import PartnerList from "@/components/CompanyForms/partnersStep/PartnerList.vue";
import NewPartnerModal from "@/components/CompanyForms/partnersStep/NewPartnerModal.vue";

export default {
  components: { PartnerList, NewPartnerModal },
  data: () => ({
    updatedPartner: undefined,
    dialog: false,
  }),
  computed: {
    ...mapGetters({
      partners: "company_forms/partners",
    }),
    limitReached() {
      return this.partners.length === 5;
    },
  },
  methods: {
    ...mapActions({
      setPartners: "company_forms/setPartners",
    }),
    openModalToSave() {
      this.updatedPartner = undefined;
      this.dialog = true;
    },
    openModalToUpdate(index) {
      this.updatedPartner = {
        index,
        partner: this.partners[index],
      };
      this.dialog = true;
    },
    includePartner(partner) {
      this.setPartners([...this.partners, partner]);
    },
    updatePartner(payload) {
      const { index, newPartner } = payload;
      const copyPartners = Object.assign([], this.partners);
      copyPartners[index] = newPartner;
      this.setPartners(copyPartners);
    },
    removePartner(index) {
      const end = this.partners.length;
      const head = this.partners.slice(0, index);
      const tail = this.partners.slice(index + 1, end);
      this.setPartners(head.concat(tail));
    },
  },
};
</script>
