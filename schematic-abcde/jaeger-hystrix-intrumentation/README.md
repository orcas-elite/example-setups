# Schematic Example 'abcde' with jaeger instrumentation

## Requirements
 * Docker
 * Docker-compose
 * Maven

## Scale Services
It is possible to run more than one service instance per service by changing the *scale* parameter of the service in the *docker-compose.yml* file.

**Note:** Make sure that enough memory is available to Docker, as running multiple services instances can consume a lot of memory. You can change the memory available to Docker in the preferences of the docker client.

## Usage
 * Run the 'build-and-run.sh'
 * Open http://localhost:80/a1 or http://localhost:80/a2 in your browser
 * Open http://localhost:16686 in your browser to explore the traces