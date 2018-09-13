#!/bin/bash

declare -a exp=("exp1" "exp2" "exp3")

# Deploy abcde
echo "Deploying abcde..."
ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/abcde.yml"
ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/routing/abcde-routing-default.yml"
echo "Waiting 30s for abcde to be ready..."
sleep 30
echo "Deploying abcde... done"

for i in "${exp[@]}"
do
	echo "###################### Starting $i ######################"
	date --iso-8601=s

	# Start locust in screen
	echo "Starting Locust..."
	ssh chaos-loaddriver screen -S locust -d -m "locust -f ~/example-setups/schematic-abcde/workload/locustfile.py --host http://chaos-kube:31380"
	echo "Waiting 10s for locust to be ready..."
	sleep 10
	echo "Starting Locust... done"

	# Set hystrix configurations
	# TODO: set apache files

	# Set istio fault injection
	# TODO:

	# Run experiment
	echo "Starting locust workload..."
	curl -X POST --data "locust_count=100&hatch_rate=10" http://chaos-loaddriver:8089/swarm
	echo "Starting locust workload... done"
	sleep 30
	echo "Stopping locust..."
	curl http://chaos-loaddriver:8089/stop
	echo "Stopping locust... done"

	# Save locust log
	echo "Copying results..."
	mkdir $i
	scp chaos-loaddriver:/tmp/response.log $i/.
	echo "Copying results... done"

	echo "Cleaning up..."
	# Stop locust screen
	ssh chaos-loaddriver screen -X -S locust quit
	ssh chaos-loaddriver 'rm /tmp/response.log'
	sleep 10
	echo "Cleaning up... done"

	echo "###################### $i completed ######################"
	date --iso-8601=s
done

echo "Deleting abcde..."
ssh chaos-kube "kubectl delete -f experiments/$i/abcde.yml"
echo "Deleting abcde... done"
