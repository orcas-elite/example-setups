#!/bin/bash

hystrixLocations=("a1" "a2" "b1" "c1")
hystrixEnabled=(false false false false) # Default hystrix configuration
injectionLocations=("b1" "c1" "c2" "d1" "e1" "e2")
faultTypes=("delay" "abort")
hystrixproperties="/var/www/html/hystrix.properties"

experimentDir=experiment-$(date --iso-8601=s)
mkdir $experimentDir

# Deploy abcde
echo "Deploying abcde..."
ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/chaos-controller-egress.yml"
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
curl -X POST --data "locust_count=50&hatch_rate=1" http://chaos-loaddriver:8089/swarm >/dev/null
echo "Starting locust workload... done"
sleep 60
echo "Stopping locust..."
curl http://chaos-loaddriver:8089/stop >/dev/null
echo "Stopping locust... done"
echo "Cleaning up..."
ssh chaos-loaddriver screen -X -S locust quit
ssh chaos-loaddriver 'rm /tmp/response.log'
sleep 10
echo "Cleaning up... done"
echo "Warming up abcde... done"

# Start experiments
# Iterate through all combinations ( 2^no. of methods with hystrix)
for ((i=0; i<$((2 ** ${#hystrixLocations[@]})); i++))
do
	echo "###################### Starting experiment no. $i ######################"
	date --iso-8601=s

	# Derive from value of $i about which methods should have hystrix enabled.
	# This uses bitwise AND operation to mask bits in the binary value of $i and determines if hystrix of each method should be enabled.
	# This uses decrementing for loop because an incrementing for loop with $((x)) creates a bash subshell which does not allow the value of hystrix array to be modified from in side the loop.
	for ((j=$((${#hystrixLocations[@]} - 1)); j>=0; j--))
	do
		if [ $(( $i & $(( 2 ** $j)) )) -eq $(( 2 ** $j)) ]; then
			hystrixEnabled[$j]=true
		else
			hystrixEnabled[$j]=false
		fi
	done

	configurationDir=""
	for ((j=0; j<${#hystrixLocations[@]}; j++))
	do
		configurationDir=$configurationDir${hystrixLocations[$j]}${hystrixEnabled[$j]}-
	done
	configurationDir=${configurationDir%?} # Remove trailing "-"
	configurationDir=experimentDir/${configurationDir}
	mkdir $configurationDir
	echo $configurationDir

	# Set hystrix configurations for all methods
	truncate -s 0 $hystrixproperties # Delete file content
	for ((j=0; j<${#hystrixLocations[@]}; j++))
	do
		if ${hystrixEnabled[$j]};
		then
			echo hystrix.command.${hystrixLocations[$j]}.fallback.enabled=true >> $hystrixproperties
			echo hystrix.command.${hystrixLocations[$j]}.execution.timeout.enabled=true >> $hystrixproperties
			echo hystrix.command.${hystrixLocations[$j]}.circuitBreaker.enabled=true >> $hystrixproperties
			echo hystrix.command.${hystrixLocations[$j]}.circuitBreaker.requestVolumeThreshold=1 >> $hystrixproperties
		else
			echo hystrix.command.${hystrixLocations[$j]}.fallback.enabled=false >> $hystrixproperties
			echo hystrix.command.${hystrixLocations[$j]}.execution.timeout.enabled=false >> $hystrixproperties
			echo hystrix.command.${hystrixLocations[$j]}.circuitBreaker.enabled=false >> $hystrixproperties
			echo hystrix.command.${hystrixLocations[$j]}.circuitBreaker.requestVolumeThreshold=1 >> $hystrixproperties
		fi
	done
	echo "Waiting 60s for hystrix to poll configurations..."
	sleep 60
	echo "Waiting 60s for hystrix to poll configurations... done"

	# Reset routing to default value before injecting new fault
	ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/routing/abcde-routing-default.yml"

	echo "#### Injecting no fault ####"

	# Start locust in screen
	echo "Starting Locust..."
	ssh chaos-loaddriver screen -S locust -d -m "locust -f ~/example-setups/schematic-abcde/workload/locustfile.py --host http://chaos-kube:31380"
	echo "Waiting 10s for locust to be ready..."
	sleep 10
	echo "Starting Locust... done"

	# Run experiment
	echo "Starting locust workload..."
	curl -X POST --data "locust_count=50&hatch_rate=1" http://chaos-loaddriver:8089/swarm >/dev/null
	echo "Starting locust workload... done"
	sleep 60
	echo "Stopping locust..."
	curl http://chaos-loaddriver:8089/stop >/dev/null
	echo "Stopping locust... done"

	# Save locust log
	echo "Copying results..."
	injectionDir=$configurationDir/nofault
	mkdir $injectionDir
	scp chaos-loaddriver:/tmp/response.log $injectionDir/.
	echo "Copying results... done"

	echo "Cleaning up..."
	# Stop locust screen
	ssh chaos-loaddriver screen -X -S locust quit
	ssh chaos-loaddriver 'rm /tmp/response.log'
	sleep 10
	echo "Cleaning up... done"

	# Iterate through all injection locations
	for ((j=0; j<${#injectionLocations[@]}; j++))
	do
		# Iterate through all types of faults to be injected
		for ((k=0; k<${#faultTypes[@]}; k++))
		do
			# Reset routing to default value before injecting new fault
			ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/istio/kube/routing/abcde-routing-default.yml"

			echo "#### Injecting ${faultTypes[$k]} at ${injectionLocations[$j]} ####"
			ssh chaos-kube "kubectl apply -f ~/example-setups/schematic-abcde/experiment/faultinjection/inject-fault-${injectionLocations[$j]}-${faultTypes[$k]}.yml"

			# Start locust in screen
			echo "Starting Locust..."
			ssh chaos-loaddriver screen -S locust -d -m "locust -f ~/example-setups/schematic-abcde/workload/locustfile.py --host http://chaos-kube:31380"
			echo "Waiting 10s for locust to be ready..."
			sleep 10
			echo "Starting Locust... done"

			# Start workload
			echo "Starting locust workload..."
			curl -X POST --data "locust_count=50&hatch_rate=1" http://chaos-loaddriver:8089/swarm >/dev/null
			echo "Starting locust workload... done"
			sleep 60
			# Stop workload
			echo "Stopping locust..."
			curl http://chaos-loaddriver:8089/stop >/dev/null
			echo "Stopping locust... done"

			# Save locust log
			echo "Copying results..."
			injectionDir=$configurationDir/${injectionLocations[$j]}-${faultTypes[$k]}
			mkdir $injectionDir
			scp chaos-loaddriver:/tmp/response.log $injectionDir/.
			echo "Copying results... done"

			echo "Cleaning up..."
			# Stop locust screen
			ssh chaos-loaddriver screen -X -S locust quit
			ssh chaos-loaddriver 'rm /tmp/response.log'
			ssh chaos-kube "kubectl delete -f ~/example-setups/schematic-abcde/experiment/faultinjection/inject-fault-${injectionLocations[$j]}-${faultTypes[$k]}.yml"
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
