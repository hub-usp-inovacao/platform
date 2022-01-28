const matchesFilter = (skill, { primary, secondary, terciary }) => {
  let primaryMatch = true;
  let secondaryMatch = true;
  let terciaryMatch = true;

  if (primary.length > 0) {
    primaryMatch = skill.area.major.some((major) => primary.includes(major));
  }

  if (secondary.length > 0) {
    secondaryMatch = skill.area.minors.some((minor) =>
      secondary.includes(minor)
    );
  }

  const [campus, unity, bond] = terciary;

  if (campus) {
    terciaryMatch = skill.campus === campus;
  }

  if (unity) {
    terciaryMatch = terciaryMatch && skill.unities.includes(unity);
  }

  if (bond) {
    terciaryMatch = terciaryMatch && skill.bond === bond;
  }

  return primaryMatch && secondaryMatch && terciaryMatch;
};

export default (_, inject) => {
  inject("skillMatchesFilter", matchesFilter);
};
