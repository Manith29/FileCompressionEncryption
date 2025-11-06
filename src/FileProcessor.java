import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

public class FileProcessor {

    private final FileCompressor compressor;
    private final FileEncryptor encryptor;

    public FileProcessor() {
        this(new FileCompressor(), new FileEncryptor());
    }

    public FileProcessor(FileCompressor compressor, FileEncryptor encryptor) {
        this.compressor = compressor;
        this.encryptor = encryptor;
    }

    public void compressAndEncrypt(String inputPath, String outputPath, String password)
            throws IOException, GeneralSecurityException {
        Path tempFile = Files.createTempFile("compressed", ".tmp");
        try {
            compressor.compressFile(inputPath, tempFile.toString());
            encryptor.encryptFile(tempFile.toString(), outputPath, password);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    public void decryptAndDecompress(String inputPath, String outputPath, String password)
            throws IOException, GeneralSecurityException {
        Path tempFile = Files.createTempFile("decrypted", ".tmp");
        try {
            encryptor.decryptFile(inputPath, tempFile.toString(), password);
            compressor.decompressFile(tempFile.toString(), outputPath);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
