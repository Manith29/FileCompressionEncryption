import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileCompressor {

    public void compressFile(String inputPath, String outputPath) throws IOException {
        Path input = Path.of(inputPath);
        Path output = Path.of(outputPath);
        ensureParentDirectory(output);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input.toFile()));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output.toFile()));
             GZIPOutputStream gzipOut = new GZIPOutputStream(bos)) {
            transfer(bis, gzipOut);
        }
    }

    public void decompressFile(String inputPath, String outputPath) throws IOException {
        Path input = Path.of(inputPath);
        Path output = Path.of(outputPath);
        ensureParentDirectory(output);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input.toFile()));
             GZIPInputStream gzipIn = new GZIPInputStream(bis);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output.toFile()))) {
            transfer(gzipIn, bos);
        }
    }

    private void transfer(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
    }

    private void ensureParentDirectory(Path output) throws IOException {
        Path parent = output.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
