import java.util.Scanner;
public class HillCipher {
    // Function to find the modular inverse of a number mod 26
    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1; // If no inverse exists
    }
    // Function to find the determinant of a 2x2 matrix modulo 26
    private static int determinant(int[][] matrix, int mod) {
        return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % mod;
    }
    // Function to find the inverse of a 2x2 matrix modulo 26
    private static int[][] inverseMatrix(int[][] matrix, int mod) {
        int det = determinant(matrix, mod);
        int invDet = modInverse(det, mod);
        if (invDet == -1) {
            throw new IllegalArgumentException("Matrix is not invertible");
        }
        // Finding adjugate matrix (cofactors of the matrix)
        int[][] adjugate = new int[2][2];
        adjugate[0][0] = matrix[1][1];
        adjugate[1][1] = matrix[0][0];
        adjugate[0][1] = -matrix[0][1];
        adjugate[1][0] = -matrix[1][0];
        // Applying modulo 26 and multiplying by inverse of determinant
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                adjugate[i][j] = (adjugate[i][j] * invDet) % mod;
                if (adjugate[i][j] < 0) {
                    adjugate[i][j] += mod;
                }
            }
        }
        return adjugate;
    }
    // Function to multiply two matrices mod 26
    private static int[][] multiplyMatrices(int[][] mat1, int[][] mat2, int mod) {
        int[][] result = new int[2][1]; // Result will be a 2x1 matrix (vector)
        for (int i = 0; i < 2; i++) {
            result[i][0] = (mat1[i][0] * mat2[0][0] + mat1[i][1] * mat2[1][0]) % mod;
            if (result[i][0] < 0) result[i][0] += mod;
        }
        return result;
    }
    // Function to convert string to matrix (vector)
    private static int[][] stringToMatrix(String text) {
        int[][] matrix = new int[2][1];
        matrix[0][0] = text.charAt(0) - 'A';
        matrix[1][0] = text.charAt(1) - 'A';
        return matrix;
    }
    // Function to convert matrix (vector) to string
    private static String matrixToString(int[][] matrix) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            result.append((char) (matrix[i][0] + 'A'));
        }
        return result.toString();
    }
    // Function to encrypt plaintext using Hill Cipher
    public static String encrypt(String plaintext, int[][] keyMatrix) {
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            String block = plaintext.substring(i, i + 2);
            int[][] plaintextMatrix = stringToMatrix(block);
            int[][] encryptedMatrix = multiplyMatrices(keyMatrix, plaintextMatrix, 26);
            ciphertext.append(matrixToString(encryptedMatrix));
        }
        return ciphertext.toString();
    }
    // Function to decrypt ciphertext using Hill Cipher
    public static String decrypt(String ciphertext, int[][] keyMatrix) {
        StringBuilder decryptedText = new StringBuilder();
        int[][] inverseKeyMatrix = inverseMatrix(keyMatrix, 26);
        for (int i = 0; i < ciphertext.length(); i += 2) {
            String block = ciphertext.substring(i, i + 2);
            int[][] ciphertextMatrix = stringToMatrix(block);
            int[][] decryptedMatrix = multiplyMatrices(inverseKeyMatrix, ciphertextMatrix, 26);
            decryptedText.append(matrixToString(decryptedMatrix));
        }
        return decryptedText.toString();
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Example 2x2 key matrix for Hill Cipher
        int[][] keyMatrix = {
                {3, 3},
                {2, 5}
        };
        System.out.print("Enter plaintext (must be even length): ");
        String plaintext = sc.nextLine().toUpperCase().replaceAll("[^A-Z]", "");
        // Ensure the length of the plaintext is even
        if (plaintext.length() % 2 != 0) {
            plaintext += "X"; // Padding with 'X' if the length is odd
        }
        // Encryption
        String ciphertext = encrypt(plaintext, keyMatrix);
        System.out.println("Ciphertext: " + ciphertext);
        // Decryption
        String decryptedText = decrypt(ciphertext, keyMatrix);
        System.out.println("Decrypted text: " + decryptedText);
        sc.close();
    }
}