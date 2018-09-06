#!/bin/bash

declare -a exp=("exp1" "exp2" "exp3")

# Start locust manually before running this script!

# Deploy abcde

for i in "${exp[@]}"
do
	echo "Starting $i"
	date --iso-8601=s

	# Set configurations

	# Run experiment
	curl -X POST --data "locust_count=10&hatch_rate=1" http://10.0.20.75:8089/swarm
	sleep 30
	curl http://10.0.20.75:8089/stop

	# Save locust log

	echo "Stopping $i"
	date --iso-8601=s
done
