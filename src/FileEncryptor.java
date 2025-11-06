import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor {

    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256;
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KDF_ALGORITHM = "PBKDF2WithHmacSHA256";

    private final SecureRandom secureRandom = new SecureRandom();

    public void encryptFile(String inputPath, String outputPath, String password)
            throws IOException, GeneralSecurityException {
        Path input = Path.of(inputPath);
        Path output = Path.of(outputPath);
        ensureParentDirectory(output);

        byte[] salt = generateSalt();
        byte[] iv = generateIv();
        SecretKey key = deriveKey(password, salt);
        Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key, iv);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input.toFile()));
             DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output.toFile())))) {
            dos.writeInt(salt.length);
            dos.write(salt);
            dos.writeInt(iv.length);
            dos.write(iv);
            dos.flush();

            try (CipherOutputStream cos = new CipherOutputStream(dos, cipher)) {
                transfer(bis, cos);
            }
        }
    }

    public void decryptFile(String inputPath, String outputPath, String password)
            throws IOException, GeneralSecurityException {
        Path input = Path.of(inputPath);
        Path output = Path.of(outputPath);
        ensureParentDirectory(output);

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(input.toFile())));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output.toFile()))) {
            byte[] salt = readBlock(dis, SALT_LENGTH);
            byte[] iv = readBlock(dis, IV_LENGTH);
            SecretKey key = deriveKey(password, salt);
            Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key, iv);

            try (CipherInputStream cis = new CipherInputStream(dis, cipher)) {
                transfer(cis, bos);
            }
        }
    }

    public SecretKey generateKey(String password) throws GeneralSecurityException {
        byte[] salt = generateSalt();
        return deriveKey(password, salt);
    }

    private byte[] readBlock(DataInputStream dis, int expectedLength) throws IOException {
        int length = dis.readInt();
        if (length != expectedLength) {
            throw new IOException("Unexpected block length in encrypted file header.");
        }
        byte[] block = dis.readNBytes(length);
        if (block.length != length) {
            throw new IOException("Unexpected end of encrypted file while reading header.");
        }
        return block;
    }

    private void transfer(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        return iv;
    }

    private SecretKey deriveKey(String password, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KDF_ALGORITHM);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    private Cipher initCipher(int mode, SecretKey key, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(mode, key, new IvParameterSpec(iv));
        return cipher;
    }

    private void ensureParentDirectory(Path output) throws IOException {
        Path parent = output.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
