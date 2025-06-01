import json
import time
import random
from datetime import datetime, timedelta
from kafka import KafkaProducer
from faker import Faker

# Initialize Faker
fake = Faker()

# Initialize Kafka producer
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8'),
    key_serializer=lambda k: k.encode('utf-8') if k else None
)

# Model IDs as specified
MODEL_IDS = ["jons-law-practice", "chris-retweets"]

def generate_inference_log():
    """Generate an inference log entry matching the InferenceLog schema"""

    # Pick a random model
    model_id = random.choice(MODEL_IDS)

    # Generate a conversation ID (UUID format)
    conversation_id = fake.uuid4()

    # Generate realistic inference duration
    # Most inferences are quick (100ms-2s), some take longer (2s-10s)
    if random.random() < 0.85:  # 85% quick inferences
        duration_ms = random.randint(100, 2000)
    else:  # 15% slower inferences
        duration_ms = random.randint(2000, 10000)

    # Calculate start and end times
    end_time = datetime.utcnow()
    start_time = end_time - timedelta(milliseconds=duration_ms)

    # Create the log entry matching the protobuf schema
    inference_log = {
        "modelId": model_id,
        "conversationId": conversation_id,
        "startTime": start_time.isoformat() + 'Z',
        "endTime": end_time.isoformat() + 'Z',
        "index": random.randint(0, 1000),  # Random index for variety
    }

    return inference_log

def main():
    print("Starting inference log producer...")
    print("Sending logs to 'logs-ingest' topic")
    print("Press Ctrl+C to stop")

    try:
        while True:
            # Generate and send inference log
            log_entry = generate_inference_log()

            # Use modelId as partition key for better distribution
            key = log_entry['modelId']

            # Send to Kafka 'logs' topic
            producer.send('logs-ingest', key=key, value=log_entry)

            # Calculate duration for display
            start_dt = datetime.fromisoformat(log_entry['startTime'].replace('Z', '+00:00'))
            end_dt = datetime.fromisoformat(log_entry['endTime'].replace('Z', '+00:00'))
            duration_ms = int((end_dt - start_dt).total_seconds() * 1000)

            # Print to console for debugging
            print(f"[{log_entry['endTime']}] {log_entry['modelId']} - Conversation: {log_entry['conversationId'][:8]}... Duration: {duration_ms}ms")

            # Random delay between inference logs (0.5 to 3 seconds)
            time.sleep(random.uniform(0.5, 3.0))

    except KeyboardInterrupt:
        print("\nStopping inference log producer...")
    finally:
        producer.close()

if __name__ == "__main__":
    main()