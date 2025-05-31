## Overview

## Runbook

#### Setting up Kafka
```bash
# Creates a kafka instance with the 'logs' and 'metrics' topics
./scripts/setup-kafka.sh
```

### Setting up the fake model scripts
```bash
# Create a virtual environment
python -m venv kafka-env
source kafka-env/bin/activate
# Install dependencies
pip install -r scripts/pythonRequirements.txt
# Run the script
python scripts/simulated-logs.py
```