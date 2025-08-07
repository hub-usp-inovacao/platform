// TODO: apiGetCompany (get_company_data)
// TODO: apiPatchCompany (update_company)
export default (_app, inject) => {
  inject("apiPostCompany", import("./company/apiPostCompany.js"));
};
