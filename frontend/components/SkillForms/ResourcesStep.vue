<template>
  <v-container>
    <v-row>
      <v-col>
        <h2>Competências</h2>
        <MultipleInputs
          :value="skills"
          @input="setSkills"
          component="ShortTextInput"
          inputLabel="Competência"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <h2>Equipamentos</h2>
        <MultipleInputs
          :value="equipments"
          @input="setEquipments"
          component="ShortTextInput"
          inputLabel="Equipamento"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <h2>Serviços Tecnológicos</h2>
        <MultipleInputs
          :value="services"
          @input="setServices"
          component="ShortTextInput"
          inputLabel="Serviço"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import MultipleInputs from "../CompanyForms/inputs/MultipleInputs.vue";
import ShortTextInput from "../CompanyForms/inputs/ShortTextInput.vue";

export default {
  components: {
    MultipleInputs,
    ShortTextInput,
  },
  computed: {
    ...mapGetters({
      skills: "skill_form/skills",
      equipments: "skill_form/equipments",
      services: "skill_form/services",
    }),
  },
  methods: {
    ...mapActions({
      setSkills: "skill_form/setSkills",
      setEquipments: "skill_form/setEquipments",
      setServices: "skill_form/setServices",
    }),
  },
};
</script>