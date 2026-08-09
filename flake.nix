{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  inputs.nixpkgs-nodejs.url = "github:NixOS/nixpkgs/6b5019a48f876f3288efc626fa8b70ad0c64eb46";
  inputs.nixpkgs-bundler.url = "github:NixOS/nixpkgs/b60793b86201040d9dee019a05089a9150d08b5b";
  inputs.nixpkgs-ruby.url = "github:NixOS/nixpkgs/0cb2fd7c59fed0cd82ef858cbcbdb552b9a33465";

  outputs =
    {
      nixpkgs,
      nixpkgs-nodejs,
      nixpkgs-bundler,
      nixpkgs-ruby,
      systems,
      ...
    }:
    let
      inherit (nixpkgs) lib;
      forAllSystems = lib.genAttrs (import systems);
      pkgsFor = forAllSystems (
        system:
        import nixpkgs {
          inherit system;
          config = {
            # TODO: Remove once node-sass is gone
            permittedInsecurePackages = [
              "python-2.7.18.8"
              "gradle-7.6.6"
            ];
            # mongodb
            allowUnfree = true;
          };
        }
      );
      nodePkgsFor = forAllSystems (
        system:
        import nixpkgs-nodejs {
          inherit system;
          config = {
            # TODO: Remove once node-sass is gone
            permittedInsecurePackages = [
              "nodejs-14.21.1"
              "openssl-1.1.1w"
            ];
          };
        }
      );
      server = {
        shell =
          {
            mkShell,
            jdk,
            gradle,
            ktfmt,
          }:
          mkShell {
            name = "hub-usp-server-devshell";
            packages = [
              jdk
              gradle
              ktfmt
            ];
            JAVA_OPTS = "-Xmx4g";
            JAVA_HOME = "${jdk.home}";
          };
      };
      frontend = {
        shell =
          {
            mkShell,
            yarn,
            python2,
          }:
          mkShell {
            name = "hub-usp-frontend-devshell";
            packages = [
              yarn
              python2
            ];
          };
      };
      backend = {
        shell =
          {
            mkShell,
            bundler,
            ruby-lsp,
            ruby,
          }:
          mkShell {
            name = "hub-usp-backend-devshell";
            buildInputs = [
              bundler
              ruby
              ruby-lsp
            ];
            shellHook = ''
              export GEM_HOME=$HOME/.local/share/gem/ruby/3.3.0
              export GEM_PATH=$HOME/.local/share/gem/ruby/3.3.0
              # export BUNDLE_CACHE_PATH=$HOME/.local/share/gem/ruby/3.3.0/cache
              export PATH="$PATH${lib.makeBinPath [ ruby ]}"
            '';
          };
      };
    in
    {
      devShells = forAllSystems (
        system:
        let
          pkgs = pkgsFor.${system};
        in
        {
          backend = pkgs.callPackage backend.shell {
            bundler = nixpkgs-bundler.legacyPackages.${system}.bundler.override {
              ruby = nixpkgs-ruby.legacyPackages.${system}.ruby_3_3;
            };
            ruby = nixpkgs-ruby.legacyPackages.${system}.ruby_3_3;
            ruby-lsp = pkgs.ruby-lsp.override {
              ruby = nixpkgs-ruby.legacyPackages.${system}.ruby_3_3;
            };
          };
          caddy = pkgs.mkShell {
            packages = with pkgs; [
              caddy
            ];
          };
          frontend =
            let
              oldPkgs = nodePkgsFor.${system};
            in
            pkgs.callPackage frontend.shell {
              mkShell = oldPkgs.mkShell;
              yarn = pkgs.yarn.override {
                nodejs = oldPkgs.nodejs-14_x;
              };
            };
          server = pkgs.callPackage server.shell {
            gradle = pkgs.gradle_7-unwrapped;
            jdk = pkgs.jdk17;
          };
          ide = pkgs.mkShell {
            packages = with pkgs; [
              netbeans
              jetbrains.idea-community-bin
              vscodium
              jdk17
            ];
            JAVA_HOME = "${pkgs.jdk17.home}";
          };
        }
      );
      packages = forAllSystems (
        system:
        let
          pkgs = pkgsFor.${system};
        in
        {
          caddy =
            let
              caddyfile = builtins.replaceStrings [ "catalogapp" "backdev" ] [ "" "" ] (
                builtins.readFile ./caddy/Caddyfile.dev
              );
              config = pkgs.writeText "Caddyfile.hubusp" ''
                {
                  persist_config off
                  storage_check off
                }

                ${caddyfile}
              '';
            in
            pkgs.writeShellApplication {
              name = "caddy-wrapped-hub";
              runtimeInputs = with pkgs; [
                caddy
              ];
              text = ''
                exec caddy run --config ${config} --adapter caddyfile
              '';
            };
          mongo = pkgs.writeShellApplication {
            name = "mongodb-wrapped-hub";
            runtimeInputs = with pkgs; [ mongodb ];
            text = ''
              DPATH="''${TEMPDIR:-/tmp}/hub-usp-mongo-db"
              mkdir -p "$DPATH"
              mongod --dbpath "$DPATH" --port 27017
            '';
          };
        }
      );
    };
}
