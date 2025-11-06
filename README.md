# FileCrypt — File Compression & Encryption Utility

[![Release](https://img.shields.io/github/v/release/Manith29/FileCompressionEncryption?label=release)](https://github.com/Manith29/FileCompressionEncryption/releases/latest)
![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/Java-8%2B-blue)

A lightweight **Java CLI tool** that compresses files using **GZIP** and securely encrypts them with **AES-256**. Can also decrypt and decompress to restore original files — ideal for secure storage or transfer.

---

## 🚀 Quick Install (one command)

Install **FileCrypt** globally with a single command (downloads and installs the latest release):

```bash
curl -s https://raw.githubusercontent.com/Manith29/FileCompressionEncryption/main/install.sh | bash
```
After installation , run :
filecrypt 

## How the Release Works

This repository’s **Releases** include a precompiled JAR file (`FileCompressionEncryption.jar`) that contains the complete application.  
The accompanying `install.sh` script automates installation for end users.

### Installation Process

1. The script downloads the latest `.jar` file from the GitHub **Releases** section.  
2. It copies the JAR into `/usr/local/bin/filecrypt.jar`.  
3. It creates a small launcher script at `/usr/local/bin/filecrypt` that executes the JAR using Java.  
4. It sets executable permissions on the launcher, allowing the `filecrypt` command to work system-wide.

This setup means that users do not need to clone the repository or compile the project.  
They can install and use the tool with a single command.

javac -d bin src/*.java
jar cfe FileCompressionEncryption.jar Main -C bin .
java -jar FileCompressionEncryption.jar
