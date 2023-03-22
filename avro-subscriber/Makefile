LIST = $(shell seq -w 1 10)

movies:
	for m in $(LIST); do \
		curl --header "Content-Type: application/json" --request POST --data "{\"title\":\"Movie$$m\",\"year\":20$$m}"  http://localhost:9999/movies ; \
	done
