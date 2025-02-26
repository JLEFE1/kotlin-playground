db:
	- cd ops/db && docker compose up -d

db-down:
	cd ops/db && docker compose down