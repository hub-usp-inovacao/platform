DEVFILE = dev-compose.yaml
PRODFILE = prod-compose.yaml
TESTFILE = test-compose.yaml
BRANCH_NAME = hub

DC = docker-compose
BG_FLAG = -d

BUILD_SUBCMD = build
BUILD_NO_CACHE_SUBCMD = build --no-cache
RUN_SUBCMD = up
STOP_SUBCMD = down


.PHONY: build_dev build_prod dev prod stop_dev stop_prod test

deploy:
	git pull
	make build_prod
	make stop_prod
	make prod

## DEVELOPMENT TARGETS

build_dev:
	$(DC) -f $(DEVFILE) $(BUILD_SUBCMD)

rebuild_dev:
	$(DC) -f $(DEVFILE) $(BUILD_NO_CACHE_SUBCMD)

dev:
	$(DC) -f $(DEVFILE) $(RUN_SUBCMD)

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


## TEST TARGETS

build_test:
	$(DC) -f $(TESTFILE) $(BUILD_SUBCMD)

test:
	$(DC) -f $(TESTFILE) $(RUN_SUBCMD)

stop_test:
	$(DC) -f $(TESTFILE) $(STOP_SUBCMD)
