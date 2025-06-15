{
  mkShell,
  jdk,
  gradle,
  kotlin-language-server,
}:
mkShell {
  name = "hub-usp-server-devshell";
  buildInputs = [
    jdk
    gradle

    # TODO: kotlin-lsp
    kotlin-language-server
  ];
  JAVA_HOME = "${jdk.home}";
}
