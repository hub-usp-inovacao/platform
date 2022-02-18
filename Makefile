DEVFILE = dev-compose.yaml
TESTFILE = dev-compose.yaml
PRODFILE = prod-compose.yaml

DC = docker-compose
BG_FLAG = -d

BUILD_SUBCMD = build
BUILD_NO_CACHE_SUBCMD = build --no-cache
RUN_SUBCMD = up
STOP_SUBCMD = down

DCPROD = docker-compose -f prod-compose.yaml

.PHONY: build_dev build_prod dev prod stop_dev stop_prod test

## DEVELOPMENT TARGETS

build_dev:
	$(DC) -f $(DEVFILE) $(BUILD_SUBCMD)

rebuild_dev:
	$(DC) -f $(DEVFILE) $(BUILD_NO_CACHE_SUBCMD)

dev:
	$(DC) -f $(DEVFILE) $(RUN_SUBCMD)

attach_front:
	$(DC) -f $(DEVFILE) run --rm frontend sh

attach_back:
	$(DC) -f $(DEVFILE) run --rm backend sh

stop_dev:
	$(DC) -f $(DEVFILE) $(STOP_SUBCMD)


## PRODUCTION TARGETS

build_prod:
	$(DC) -f $(PRODFILE) $(BUILD_SUBCMD)

rebuild_prod:
	$(DC) -f $(PRODFILE) $(BUILD_NO_CACHE_SUBCMD)

prod:
	$(DC) -f $(PRODFILE) $(RUN_SUBCMD) $(BG_FLAG)

stop_prod:
	$(DC) -f $(PRODFILE) $(STOP_SUBCMD)

attach_prod_front:
	$(DC) -f $(PRODFILE) run --rm frontend sh

attach_prod_back:
	$(DC) -f $(PRODFILE) run --rm backend sh

fetch_prod:
	docker-compose -f prod-compose.yaml run --rm backend rake fetch:no_report

deploy:
	git pull
	make build_prod
	make stop_prod
	make prod

setup_proxy:
	caddy run --config ./Caddyfile &
