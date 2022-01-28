import { PDI } from "@/lib/classes/pdi";
import { findErrors } from "@/lib/errors/pdi";
import { columnValue } from "@/lib/sheets";
import { fetchCentrals } from "./fetch_centrais";

async function fetchData() {
  try {
    const url = process.env.PDI_DATA_SOURCE_URL;
    const resp = await fetch(url);

    const { values } = await resp.json();
    return values;
  } catch (error) {
    console.log("error occuried while fetching...");
    console.log(error);
    return undefined;
  }
}

const spaceRgx = /( )/;
const dotRgx = /.+\..+/; // match "bsa.legal" which is a valid url

const checkUrl = (url) => !url.match(spaceRgx) && url.match(dotRgx);

function beginNewPDI(row, $campi) {
  const name = columnValue(row, "B");
  const category = columnValue(row, "A");
  let campus = columnValue(row, "D");
  const unity = columnValue(row, "E");
  const description = {
    short: columnValue(row, "K"),
    long: columnValue(row, "L"),
  };
  const coordinator = columnValue(row, "F");

  if (campus == undefined || campus == "") {
    campus = $campi.find((c) => c.unities.find((u) => u == unity))?.name;
  }

  return new PDI(name, category, campus, unity, description, coordinator);
}

function addURL(base, row) {
  if (checkUrl(row[6])) base.url = columnValue(row, "G");
}

function addKeywords(base, row) {
  base.keywords = columnValue(row, "O");
}

function addEmail(base, row) {
  base.email = columnValue(row, "H");
}

function addPhone(base, row) {
  base.phone = columnValue(row, "I");
}

function pdiGenerator(row, $campi) {
  const base = beginNewPDI(row, $campi);

  addURL(base, row);
  addKeywords(base, row);
  addEmail(base, row);
  addPhone(base, row);

  return base;
}

export default ({ isDev, $campi }, inject) => {
  inject("fetchPDIs", async () => {
    const values = await fetchData();
    if (values == undefined) return { pdis: [], errors: [] };

    const pdis = values
      .slice(1)
      .map((row, i) => {
        let pdi;
        try {
          pdi = pdiGenerator(row, $campi);
        } catch (e) {
          console.log(`[PDI Exception] failed for row ${i + 2}`);
          pdi = null;
        }

        return pdi;
      })
      .filter((p) => p !== null);

    const errors = findErrors(Object.assign([], pdis));

    const centrais = await fetchCentrals(isDev);

    const all = pdis
      .concat(centrais)
      .sort((a, b) => a.name.toLowerCase().localeCompare(b.name.toLowerCase()));

    return { pdis: all, errors };
  });
};
