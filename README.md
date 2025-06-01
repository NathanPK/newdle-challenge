## Overview

## Runbook

To start the project:
./gradlew run

When you're finished, run:
docker-compose down

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