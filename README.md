# File Compression and Encryption Utility

This Java console application compresses files using GZIP and encrypts the compressed data with AES-256. It can also reverse the process to recover the original file. The solution is designed for secure storage or transfer of files with minimal footprint.

## Features

- **Compression:** Streams file data through `GZIPOutputStream` to reduce size with minimal memory usage.
- **Encryption:** Uses AES/CBC mode with 256-bit keys derived from passwords via PBKDF2 (HMAC-SHA-256, 65,536 iterations) and random salt/IV per file.
- **Decompression & Decryption:** Restores original files by reversing the pipeline while validating header metadata.
- **Command-Line Menu:** Interactive CLI for selecting operations, entering file paths, and providing passwords securely.

## Project Structure

```
FileCompressionEncryption/
├── src/
│   ├── FileCompressor.java
│   ├── FileEncryptor.java
│   ├── FileProcessor.java
│   └── Main.java
├── input/    # Place test files here (optional)
├── output/   # Processed files are written here (optional)
└── README.md
```

## Build Instructions

Compile sources into the `bin` directory:

```bash
javac -d bin src/*.java
```

Run the CLI application:

```bash
java -cp bin Main
```

## Usage Workflow

1. **Compress & Encrypt**
   - Choose option `1`.
   - Provide path to the source file (any format).
   - Enter destination path for the encrypted output (e.g., `output/sample.enc`).
   - Supply a strong password (masked when possible).

2. **Decrypt & Decompress**
   - Choose option `2`.
   - Provide path to the encrypted file created earlier.
   - Enter destination path for the restored file.
   - Use the same password that was used during encryption.

3. **Exit**
   - Choose option `3` to terminate the application.

## Security Considerations

- Password-derived keys use random salts and IVs stored in the encrypted file header to prevent replay and rainbow table attacks.
- CBC mode with PKCS#5 padding ensures compatibility with arbitrary file sizes.
- Temporary files used during processing are deleted when operations complete.
- Avoid hardcoding passwords and consider integrating with secure key stores for production use.

## Testing Tips

- Test with various file types (text, images, PDFs) to ensure integrity.
- Compare checksums (e.g., `sha256sum`) of original files and decrypted outputs to verify correctness.
- Intentionally use an incorrect password to confirm decryption failure handling.

## Extensibility Ideas

- Add batch processing for multiple files.
- Provide compression level selection or alternative algorithms (e.g., ZIP, Deflater).
- Integrate a GUI using Swing or JavaFX.
- Store metadata (timestamps, permissions) alongside encrypted payloads.
