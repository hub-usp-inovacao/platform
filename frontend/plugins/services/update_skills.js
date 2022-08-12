export default ({ $axios }, inject) => {
  inject("updateSkills", async (skillUpdate) => {
    try {
      await $axios.$patch(process.env.BACKEND_URL + "/skills", skillUpdate);
      return { success: true };
    } catch (error) {
      return {
        success: false,
        error:
          "Falha na comunicação com o servidor. Tente novamente mais tarde.",
      };
    }
  });
};
