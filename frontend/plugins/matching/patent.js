const matchesFilter = (patent, { primary, secondary, terciary }) => {
  let primaryMatch = true;
  let secondaryMatch = true;
  let terciaryMatch = true;

  if (primary.length != 0) {
    primaryMatch =
      primary.includes(patent.classification.primary.cip.substr(0, 1)) ||
      primary.includes(patent.classification.secondary?.cip.substr(0, 1));
  }

  if (primaryMatch && secondary.length != 0) {
    secondaryMatch =
      secondary.includes(patent.classification.primary.subarea) ||
      secondary.includes(patent.classification.secondary?.subarea);
  }
  const status = terciary[0];
  if (primaryMatch && secondaryMatch && status)
    terciaryMatch = status === patent.status;

  return primaryMatch && secondaryMatch && terciaryMatch;
};

export default (_, inject) => {
  inject("patentMatchesFilter", matchesFilter);
};
