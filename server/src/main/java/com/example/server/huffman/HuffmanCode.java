package com.example.server.huffman;

import java.util.Arrays;

public class HuffmanCode implements Comparable<HuffmanCode> {
    char character;
    String code;
    int codeLength;

    public HuffmanCode(char character, String code) {
        this.character = character;
        this.code = code;
        this.codeLength = code.length();
    }

    // converts an array of huffmanCodes representing a codebook into canonical form
    public static HuffmanCode[] toCanonicalCode(HuffmanCode[] codes) {
        Arrays.sort(codes);

        // makes first code a string of 0s with same length as original code
        String newCode1 = new String(new char[codes[0].codeLength]).replace('\0', '0');
        codes[0].code = newCode1;

        for (int i = 1; i < codes.length; i++) {
            HuffmanCode prevCode = codes[i - 1];
            HuffmanCode currCode = codes[i];
            currCode.code = incrementBinaryString(prevCode.code);
            if (prevCode.codeLength != currCode.codeLength) currCode.code = appendZero(currCode.code);
        }

        return codes;
    }

    private static String incrementBinaryString(String binaryString) {
        int decimal = Integer.parseInt(binaryString, 2); // Convert to decimal
        decimal += 1;
        String incrementedBinary = Integer.toBinaryString(decimal); // Convert back to binary

        // Ensure leading zeros are preserved
        while (incrementedBinary.length() < binaryString.length()) {
            incrementedBinary = "0" + incrementedBinary;
        }

        return incrementedBinary;
    }

    private static String appendZero(String binaryString) {
        return binaryString + "0";
    }

    // Serializes character and character code length for a huffmanCodebook
    public static byte[] SeralizeCodebook(HuffmanCode[] codes) {
        byte[] serializedCodebook = new byte[2 * codes.length];

        for (int i = 0; i < codes.length; i++) {
            HuffmanCode currCode = codes[i];

            serializedCodebook[2*i] = (byte) currCode.character; // adding character
            serializedCodebook[2*i + 1] = (byte) currCode.codeLength; // adding character code length
        }

        return serializedCodebook;
    }

    // ordering based on code length and then alphabetically
    @Override
    public int compareTo(HuffmanCode o) {
        int lengthOrder = this.codeLength - o.codeLength;
        if (lengthOrder == 0) {
            return this.character - o.character;
        }
        return lengthOrder;
    }

    public static void main(String[] args) {
        // Define a sample Huffman codebook
        HuffmanCode[] codes = {
                new HuffmanCode('A', "0"),
                new HuffmanCode('B', "10"),
                new HuffmanCode('C', "110"),
                new HuffmanCode('D', "111")
        };

        // Convert to canonical Huffman codes
        HuffmanCode[] canonicalCodes = HuffmanCode.toCanonicalCode(codes);

        // Print canonical codes
        System.out.println("Canonical Huffman Codes:");
        for (HuffmanCode code : canonicalCodes) {
            System.out.println(code.character + ": " + code.code + ", " + code.codeLength);
        }

        // Serialize the canonical codebook
        byte[] serializedCodebook = HuffmanCode.SeralizeCodebook(canonicalCodes);

        // Print serialized data (character and code length)
        System.out.println("\nSerialized Codebook:");
        for (int i = 0; i < serializedCodebook.length; i += 2) {
            char character = (char) serializedCodebook[i];
            int codeLength = serializedCodebook[i + 1] & 0xFF; // Ensure unsigned interpretation
            System.out.println(character + " -> Length: " + codeLength);
        }
    }
}
