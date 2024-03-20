#!/bin/bash
# build-and-deploy.sh

# Step 1: Build the project with Gradle
./gradlew build

# Step 2: Build and start Docker containers
docker build . -t stocksync/stocksync

# Step 3: start the container as specified 
docker compose -f SqlDocker.yml -p stocksync up -d
