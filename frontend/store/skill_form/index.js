import personal from './personal'
import bond from './bond'
import resources from './resources'
import confirmation from './confirmation'

export const state = () => ({
  // ...personal.state(),
  // ...bond.state(),
  ...resources.state(),
  // ...confirmation.state(),
})

export const getters = {
  // ...personal.getters,
  // ...bond.getters,
  ...resources.getters,
  // ...confirmation.getters,
}

export const mutations = {
  // ...personal.mutations,
  // ...bond.mutations,
  ...resources.mutations,
  // ...confirmation.mutations,
}

export const actions = {
  // ...personal.actions,
  // ...bond.actions,
  ...resources.actions,
  // ...confirmation.actions,

  submitUpdateData: async ({ getters }) => {
    const body = prepareObject(getters)

    console.log(body)
  }
}

const prepareObject = (obj) => ({
  skill: {
    // ...personal.prepareSection(obj),
    // ...bond.prepareSection(obj),
    ...resources.prepareSection(obj),
    // ...confirmation.prepareSection(obj),
  }
})