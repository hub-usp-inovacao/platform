import company from "./company/index.js";

// TODO: move get_company_data to apiGetCompany
// TODO: move update_company to apiPatchCompany
export default (_app, inject) => {
  inject("api", {
    company,
  });
};
