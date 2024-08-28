REPORTER = nyan
TESTS = tests/*/**/*.js
SERVER_SRC = server.js modules/*

install:
	@mkdir logs
	@mkdir public
	@touch logs/error.log
	@touch logs/forever.log
	@npm install --production
	@cd public; ln -s ../../root/ root

install-dev:
	@mkdir logs
	@mkdir public
	@touch logs/error.log
	@touch logs/forever.log
	@npm install
	@cd public; ln -s ../../root/ root

test:
	@node node_modules/mocha/bin/mocha \
        	--reporter $(REPORTER) \
        	$(TESTS)

lint:
	@node_modules/jshint/bin/jshint $(SERVER_SRC)

.PHONY: install install-dev test lint
