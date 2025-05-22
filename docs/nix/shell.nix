{
  mkShell,
  yarn,
  typescript-language-server,
  vscode-langservers-extracted,
}:
mkShell {
  name = "hub-usp-docs-devshell";
  buildInputs = [
    yarn
    typescript-language-server
    vscode-langservers-extracted
  ];
}
