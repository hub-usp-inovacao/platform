{
  mkShell,
  yarn,
  typescript-language-server,
  vscode-langservers-extracted,
  python2,
}:
mkShell {
  name = "hub-usp-frontend-devshell";
  buildInputs = [
    yarn
    typescript-language-server
    vscode-langservers-extracted

    python2
  ];
}
