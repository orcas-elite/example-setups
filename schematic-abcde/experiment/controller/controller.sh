#!/bin/bash

services=("a1" "a2" "b1" "b2" "c1" "c2")
hystrix=(false false false false false false) # Default hystrix configuration
injectionpoints=("b1" "c1" "c2" "d1" "e1" "e2")
faulttypes=("delay" "abort")
hystrixconfig="/var/www/html/hystrixconfig"

mkdir experiments

# Deploy abcde
echo "Deploying abcde..."
ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/abcde.yml"
ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/routing/abcde-routing-default.yml"
echo "Waiting 30s for abcde to be ready..."
sleep 30
echo "Deploying abcde... done"

# Warm up abcde to prevent first few calls from failing
echo "Warming up abcde..."
echo "Starting Locust..."
ssh chaos-loaddriver screen -S locust -d -m "locust -f ~/example-setups/schematic-abcde/workload/locustfile.py --host http://chaos-kube:31380"
echo "Waiting 10s for locust to be ready..."
sleep 10
echo "Starting Locust... done"
echo "Starting locust workload..."
curl -X POST --data "locust_count=100&hatch_rate=10" http://chaos-loaddriver:8089/swarm
echo "Starting locust workload... done"
sleep 10
echo "Stopping locust..."
curl http://chaos-loaddriver:8089/stop
echo "Stopping locust... done"
echo "Cleaning up..."
ssh chaos-loaddriver screen -X -S locust quit
ssh chaos-loaddriver 'rm /tmp/response.log'
sleep 10
echo "Cleaning up... done"
echo "Warming up abcde... done"

# Start experiments
# Iterate through all combinations ( 2^no. of methods with hystrix)
for ((i=0; i<$((2 ** ${#services[@]})); i++))
do
	# Derive from value of $i about which methods should have hystrix enabled.
	# This uses bitwise AND operation to mask bits in the binary value of $i and determines if hystrix of each method should be enabled.
	# This uses decrementing for loop because an incrementing for loop with $((x)) creates a bash subshell which does not allow the value of hystrix array to be modified from in side the loop.
	for ((j=$((${#services[@]} - 1)); j>=0; j--))
	do
		if [ $(( $i & $(( 2 ** $j)) )) -eq $(( 2 ** $j)) ]; then
			hystrix[$j]=true
		else
			hystrix[$j]=false
		fi
	done

	dirname=""
	for ((j=0; j<${#services[@]}; j++))
	do
		dirname=$dirname${services[$j]}${hystrix[$j]}-
	done
	dirname=${dirname%?} # Remove trailing "-"
	dirname=experiments/${dirname}
	echo $dirname
	mkdir $dirname

	# Set hystrix configurations for all methods
	truncate -s 0 $hystrixconfig # Delete file content
	for ((j=0; j<${#services[@]}; j++))
	do
		if ${hystrix[$j]};
		then
			echo hystrix.command.${services[$j]}.execution.timeout.enabled=true >> $hystrixconfig
		else
			echo hystrix.command.${services[$j]}.execution.timeout.enabled=false >> $hystrixconfig
		fi
		echo hystrix.command.${services[$j]}.execution.isolation.thread.timeoutInMilliseconds=2000 >> $hystrixconfig
	done

	echo "###################### Starting experiment no. $i ######################"
	date --iso-8601=s

	# TODO: for loop for all 7*2 faults to be injected
	for ((j=0; j<${#injectionpoints[@]}; j++))
	do
		for ((k=0; k<${#faulttypes[@]}; k++))
		do
			# Reset routing to default value before injecting new fault
			ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/routing/abcde-routing-default.yml"

			echo "#### Injecting ${faulttypes[$k]} at ${injectionpoints[$j]} ####"
			ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/experiment/inject-fault-${injectionpoints[$j]}-${faulttypes[$k]}.yml"

			# Start locust in screen
			echo "Starting Locust..."
			ssh chaos-loaddriver screen -S locust -d -m "locust -f ~/example-setups/schematic-abcde/workload/locustfile.py --host http://chaos-kube:31380"
			echo "Waiting 10s for locust to be ready..."
			sleep 10
			echo "Starting Locust... done"

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
			scp chaos-loaddriver:/tmp/response.log $dirname/.
			echo "Copying results... done"

			echo "Cleaning up..."
			# Stop locust screen
			ssh chaos-loaddriver screen -X -S locust quit
			ssh chaos-loaddriver 'rm /tmp/response.log'
			sleep 10
			echo "Cleaning up... done"
		done
	done

	echo "###################### Experiment no. $i completed ######################"
	date --iso-8601=s
done

echo "Deleting abcde..."
ssh chaos-kube "kubectl delete -f ~/example-setups/schematic-abcde/istio/kube/abcde.yml"
ssh chaos-kube "kubectl delete -f ~/example-setups/schematic-abcde/istio/kube/routing/abcde-routing-default.yml"
echo "Deleting abcde... done"
echo "All experiments completed"
