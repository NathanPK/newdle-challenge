#!/bin/bash

# If there isn't already a docker container called kafka, create one
if [ "$(docker ps -q -f name=kafka)" ]; then
    echo "Kafka container already exists."
else
    echo "Creating Kafka container..."
    docker-compose -f ./scripts/kafka-compose up -d
    # Wait for Kafka to be ready
    echo "Waiting for Kafka to start..."
    sleep 30
fi

# Create the topics
echo "Creating logs-ingest topic..."
docker exec kafka kafka-topics --create --topic logs-ingest --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

echo "Creating metrics-ingest topic..."
docker exec kafka kafka-topics --create --topic metrics-ingest --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# List topics to verify creation
echo "Listing all topics:"
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

echo "Topics created successfully!"