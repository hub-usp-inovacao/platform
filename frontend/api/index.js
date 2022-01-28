const bodyParser = require("body-parser");
const app = require("express")();
const fs = require("fs");
const path = require("path");

let globalData;

function readDB() {
  if (!globalData) {
    const fileName = "uspmulti.json";
    const filePath = path.resolve(__dirname, fileName);
    const fileBuffer = fs.readFileSync(filePath);
    globalData = JSON.parse(fileBuffer);
  }

  return globalData;
}

app.use(bodyParser.json());

app.get("/centrais", (req, res) => {
  const { centrais } = readDB();

  res.json([...centrais]);
});

app.get("/servicos", (req, res) => {
  const { servicos } = readDB();

  res.json([...servicos]);
});

module.exports = app;
