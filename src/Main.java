import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final FileProcessor processor = new FileProcessor();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            System.out.println();
            System.out.println("1. Compress & Encrypt file");
            System.out.println("2. Decrypt & Decompress file");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleCompressEncrypt();
                case "2" -> handleDecryptDecompress();
                case "3" -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void handleCompressEncrypt() {
        Path input = promptForExistingPath("Enter path to input file: ");
        if (input == null) {
            return;
        }
        Path output = promptForPath("Enter path for encrypted output file: ");
        String password = readPassword();

        try {
            processor.compressAndEncrypt(input.toString(), output.toString(), password);
            System.out.println("File compressed and encrypted successfully.");
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
    }

    private void handleDecryptDecompress() {
        Path input = promptForExistingPath("Enter path to encrypted input file: ");
        if (input == null) {
            return;
        }
        Path output = promptForPath("Enter path for decrypted output file: ");
        String password = readPassword();

        try {
            processor.decryptAndDecompress(input.toString(), output.toString(), password);
            System.out.println("File decrypted and decompressed successfully.");
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
    }

    private Path promptForExistingPath(String message) {
        System.out.print(message);
        String pathInput = scanner.nextLine().trim();
        if (pathInput.isEmpty()) {
            System.out.println("Path cannot be empty.");
            return null;
        }
        Path path = Path.of(pathInput);
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            System.out.println("File does not exist: " + path);
            return null;
        }
        return path;
    }

    private Path promptForPath(String message) {
        System.out.print(message);
        String pathInput = scanner.nextLine().trim();
        while (pathInput.isEmpty()) {
            System.out.print("Path cannot be empty. Try again: ");
            pathInput = scanner.nextLine().trim();
        }
        return Path.of(pathInput);
    }

    private String readPassword() {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword("Enter password: ");
            return new String(passwordChars);
        }
        System.out.print("Enter password: ");
        return scanner.nextLine();
    }
}
