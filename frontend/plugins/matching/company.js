const matchesFilter = (company, { primary, secondary, terciary }) => {
  let primaryMatch = true;
  let secondaryMatch = true;
  let terciaryMatch = true;

  if (primary.length > 0) {
    primaryMatch = primary == company.classification.major;
  }

  if (secondary.length > 0) {
    secondaryMatch = secondary == company.classification.minor;
  }

  const city = terciary[0];
  const incubator = terciary[1];
  const size = terciary[2];

  if (city) {
    terciaryMatch = company.city.includes(city);
  }

  if (incubator) {
    terciaryMatch = terciaryMatch && company.ecosystems.includes(incubator);
  }

  if (size) {
    terciaryMatch = terciaryMatch && company.companySize.includes(size);
  }

  return primaryMatch && secondaryMatch && terciaryMatch;
};

export default (_, inject) => {
  inject("companyMatchesFilter", matchesFilter);
};
