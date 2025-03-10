package com.example.server.huffman;

import java.util.Arrays;
import java.util.HashMap;

public class HuffmanCode implements Comparable<HuffmanCode> {
    char character;
    String code;
    int codeLength;

    public HuffmanCode(char character, String code) {
        this.character = character;
        this.code = code;
        this.codeLength = code.length();
    }

    public HuffmanCode(char character, int codeLength) {
        this.character = character;
        this.codeLength = codeLength;
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
            while (currCode.code.length() != currCode.codeLength) currCode.code = currCode.code + "0";
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

    // Serializes character and character code length for a huffmanCodebook
    public static byte[] SerializeCodebook(HuffmanCode[] codes) {
        byte[] serializedCodebook = new byte[2 * codes.length];

        Arrays.sort(codes);

        for (int i = 0; i < codes.length; i++) {
            HuffmanCode currCode = codes[i];

            serializedCodebook[2*i] = (byte) currCode.character; // adding character
            serializedCodebook[2*i + 1] = (byte) currCode.codeLength; // adding character code length
        }

        return serializedCodebook;
    }

    public static HuffmanCode[] DeserializeCodebook(byte[] serializedCodebook) {
        HuffmanCode[] codes = new HuffmanCode[serializedCodebook.length / 2];

        for (int i = 0; i < serializedCodebook.length; i+=2) {
            char character = (char) serializedCodebook[i];
            int codeLength = serializedCodebook[i + 1] & 0xFF; // ensure appropriate sign
            HuffmanCode code = new HuffmanCode(character, codeLength);
            codes[i/2] = code;
        }

        return toCanonicalCode(codes);
    }

    // Converts an array of huffmanCodes into a hashmap, where characters map to respective code
    public static HashMap<Character, String> createCodeMap(HuffmanCode[] codes) {
        HashMap<Character, String> codeMap = new HashMap<>();

        for (HuffmanCode code : codes) {
            codeMap.put(code.character, code.code);
        }

        return codeMap;
    }

    public static HuffmanCode[] codeMap2CodeArr(HashMap<Character, String> codeMap) {
        HuffmanCode[] codes = new HuffmanCode[codeMap.size()];

        int i = 0;
        for (HashMap.Entry<Character, String> entry : codeMap.entrySet()) {
            HuffmanCode code = new HuffmanCode(entry.getKey(), entry.getValue());
            codes[i] = code;
            i++;
        }

        return codes;
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

    public String toString() {
        return "{" + this.character + ":" + this.code + ", " + this.codeLength + "}";
    }
}
