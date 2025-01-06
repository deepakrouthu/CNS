import java.util.Scanner;
public class RailFence {
    public static String encrypt(String plaintext, int depth) {
        if (depth <= 1) return plaintext; // Handle edge case for depth <= 1
        char[][] rail = new char[depth][plaintext.length()];
        boolean down = false;
        int row = 0, col = 0;
        for (int i = 0; i < plaintext.length(); i++) {
            if (row == 0 || row == depth - 1) down = !down;
            rail[row][col++] = plaintext.charAt(i);
            row += down ? 1 : -1;
        }
        StringBuilder ciphertext = new StringBuilder();
        for (char[] r : rail) {
            for (char c : r) {
                if (c != 0) ciphertext.append(c);
            }
        }
        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, int depth) {
        if (depth <= 1) return ciphertext; // Handle edge case for depth <= 1
        char[][] rail = new char[depth][ciphertext.length()];
        boolean down = false;
        int row = 0, col = 0;
        // Mark the positions to be filled
        for (int i = 0; i < ciphertext.length(); i++) {
            if (row == 0 || row == depth - 1) down = !down;
            rail[row][col++] = '*';
            row += down ? 1 : -1;
        }
        // Fill the marked positions with ciphertext characters
        int index = 0;
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < ciphertext.length(); j++) {
                if (rail[i][j] == '*' && index < ciphertext.length()) {
                    rail[i][j] = ciphertext.charAt(index++);
                }
            }
        }
        // Read the rail matrix to construct the plaintext
        StringBuilder plaintext = new StringBuilder();
        row = 0;
        col = 0;
        for (int i = 0; i < ciphertext.length(); i++) {
            if (row == 0 || row == depth - 1) down = !down;
            if (rail[row][col] != 0) plaintext.append(rail[row][col++]);
            row += down ? 1 : -1;
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the plaintext:");
        String plaintext = scanner.nextLine();
        System.out.println("Enter the depth:");
        int depth = scanner.nextInt();
        String encrypted = encrypt(plaintext, depth);
        System.out.println("Encrypted text: " + encrypted);
        String decrypted = decrypt(encrypted, depth);
        System.out.println("Decrypted text: " + decrypted);
        scanner.close();
    }
}