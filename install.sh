#!/bin/bash

set -e

echo "🚀 Installing FileCrypt..."

# Step 1: Download the latest JAR release to /usr/local/bin
sudo curl -L -o /usr/local/bin/filecrypt.jar \
  https://github.com/manithmahadevuni/FileCompressionEncryption/releases/latest/download/FileCompressionEncryption.jar

# Step 2: Create the executable launcher
echo '#!/bin/sh
java -jar /usr/local/bin/filecrypt.jar "$@"' | sudo tee /usr/local/bin/filecrypt > /dev/null

# Step 3: Make it executable
sudo chmod +x /usr/local/bin/filecrypt

echo "FileCrypt installed successfully!"
echo "You can now run it the command: filecrypt"
