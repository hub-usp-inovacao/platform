{
  mkShell,
  bundler,
}:
mkShell {
  name = "hub-usp-backend-devshell";
  buildInputs = [ bundler ];
}
