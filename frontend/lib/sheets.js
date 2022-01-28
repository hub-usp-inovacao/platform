export const columnValue = (row, colLetter) => {
  const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
  const map = letters
    .concat(...letters.map((l) => `A${l}`))
    .concat(...letters.map((l) => `B${l}`));

  return row[map.findIndex((x) => x === colLetter)];
};
