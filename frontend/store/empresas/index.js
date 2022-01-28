const indexingKeys = ["name", "description.long", "services", "technologies"];

export const state = () => ({
  companies: [],
  isLoading: false,
  keys: indexingKeys.map((k) => `inspect.${k}`),
});

export const getters = {
  dataStatus: (s) => (s.isLoading ? "loading" : "ok"),
  companies: (s) => s.companies,
  isEmpty: (s) => s.companies.length === 0,
  searchKeys: (s) => s.keys,
  incubators: (s) => {
    return s.companies
      .reduce((incubators, company) => {
        const newIncubators = company.ecosystems.filter(
          (incub) => !incubators.includes(incub)
        );

        return incubators.concat(newIncubators);
      }, [])
      .sort((a, b) => a.toLowerCase().localeCompare(b.toLowerCase()));
  },
  cities: (s) => {
    const cities = s.companies.reduce((all, company) => {
      return all.concat(
        company.address.city.filter((city) => {
          return city != "N/D" && city != "n/d";
        })
      );
    }, []);

    const citiesSet = cities
      .map((city) => city.trim())
      .filter((city) => city.length > 0)
      .reduce((set, city) => {
        if (!set[city]) {
          set[city] = city;
        }

        return set;
      }, {});

    return Object.keys(citiesSet).sort((a, b) => a.localeCompare(b));
  },
};

export const mutations = {
  setLoadingStatus: (s) => (s.isLoading = true),
  unsetLoadingStatus: (s) => (s.isLoading = false),
  setCompanies: (s, newCompanies) => (s.companies = newCompanies),
};

export const actions = {
  fetchSpreadsheets: async function (ctx) {
    ctx.commit("setLoadingStatus");

    const { companies } = await this.$fetchCompanies();
    const indexed = this.$indexer("companies", companies);
    ctx.commit("setCompanies", indexed);

    ctx.commit("unsetLoadingStatus");
  },
};
