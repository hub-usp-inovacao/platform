export default ({ $axios }, inject) => {
  inject("updateSkills", async (skillUpdate) => {
    try {
      await $axios.$patch("http://localhost:3001/skills", skillUpdate)
      return { success: true }
    } catch (error) {
      return { success: false, error: 'failed' }
    }
  })
}