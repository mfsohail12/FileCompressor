package com.example.server.huffman;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
    private Node root;
    private String text;
    private HashMap<Character, Integer> charFrequencies;
    public HashMap<Character, String> huffmanCodes;

    public Huffman(String text) {
        this.text = text;
        this.charFrequencies = new HashMap<Character, Integer>();
        updateCharFrequencies();
        this.huffmanCodes = new HashMap<Character, String>();
    }

    public Huffman(HashMap<Character, String> huffmanCodes) {
        this.huffmanCodes = huffmanCodes;
        buildHuffmanTree();
    }

    // Builds Huffman tree based on Huffman code map
    private void buildHuffmanTree() {
        this.root = new Node();

        for (HashMap.Entry<Character, String> entry : this.huffmanCodes.entrySet()) {
            char character = entry.getKey();
            String code = entry.getValue();

            Node currNode = this.root;

            for (int i = 0; i < code.length(); i++) {
                if (i == code.length() - 1) { // creating the leaf node in the tree
                    if (code.charAt(i) == '0') {
                        currNode.leftNode = new Leaf(character);
                    } else if (code.charAt(i) == '1') {
                        currNode.rightNode = new Leaf(character);
                    }
                } else { // creating non-leaf nodes
                    if (code.charAt(i) == '0') {
                        if (currNode.leftNode == null) {
                            currNode.leftNode = new Node();
                            currNode = currNode.leftNode;
                        } else {
                            currNode = currNode.leftNode;
                        }
                    } else if (code.charAt(i) == '1') {
                        if (currNode.rightNode == null) {
                            currNode.rightNode = new Node();
                            currNode = currNode.rightNode;
                        } else {
                            currNode = currNode.rightNode;
                        }
                    }
                }
            }
        }

    }

    // Updates charFrequencies with character frequencies of each character in the text
    private void updateCharFrequencies() {
        for (int i = 0; i < this.text.length(); i++) {
            char currChar = this.text.charAt(i);
            Integer currCharFreq = this.charFrequencies.get(currChar);
            this.charFrequencies.put(currChar, currCharFreq == null ? 1 : currCharFreq + 1);
        }
    }

    // Encodes the text by generating canonical huffman codes
    public String encode() {
        if (this.text.isEmpty()) // empty string given to encode
            return "";

        // Building Huffman tree:
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        this.charFrequencies.forEach((character, frequency) -> queue.add(new Leaf(character, frequency)));

        while (queue.size() > 1) {
            queue.add(new Node(queue.poll(), queue.poll())); // creates a new node using the smallest 2 frequencies
        }
        this.root = queue.poll();

        // Updating Huffman code map based on generated Huffman tree structure
        generateHuffmanCodes(this.root, "");

        // Converting Huffman code map to canonical form before encoding text
        HuffmanCode[] codes = HuffmanCode.codeMap2CodeArr(this.huffmanCodes);
        HuffmanCode[] canonicalCodes = HuffmanCode.toCanonicalCode(codes);
        this.huffmanCodes = HuffmanCode.createCodeMap(canonicalCodes);

        // returns encoded text based on canonical Huffman codebook
        return getEncodedText();
    }

    // Updates Huffman code map based on generated Huffman tree structure
    private void generateHuffmanCodes(Node root, String code) {
        if (root instanceof Leaf) {
            if (code.isEmpty()) {
                code = "0"; // Assign at least one bit (edge case for one character input)
            }
            this.huffmanCodes.put(((Leaf) root).getCharacter(), code);
            return;
        }
        generateHuffmanCodes(root.leftNode, code + "0");
        generateHuffmanCodes(root.rightNode, code + "1");
    }

    // Returns encoded text based on Huffman code map
    private String getEncodedText() {
        StringBuilder encodedText = new StringBuilder();
        for (int i = 0; i < this.text.length(); i++) {
            char currChar = this.text.charAt(i);
            encodedText.append(this.huffmanCodes.get(currChar));
        }
        return encodedText.toString();
    }

    // Serializes Huffman code map into a byte array
    // Array will be of length 2n, where n is the number of unique chars
    public byte[] serializeHuffmanTable() {
        HuffmanCode[] codes = HuffmanCode.codeMap2CodeArr(this.huffmanCodes);
        return HuffmanCode.SeralizeCodebook(codes);
    }

    public static HashMap<Character, String> deserializeHuffmanTable(byte[] tableBytes) {
        HuffmanCode[] codesArr = HuffmanCode.DeserializeCodebook(tableBytes);
        return HuffmanCode.createCodeMap(codesArr);
    }

    // Serializes a string of 1s and 0s into a byte array, adding padding if necessary
    public static byte[] serializeBitString(String bitString) {
        int padding = bitString.length() % 8 == 0 ? 0 : 8 - (bitString.length() % 8);
        String paddingZeros = new String(new char[padding]).replace('\0', '0');
        String paddedBitString = bitString + paddingZeros;

        byte[] byteArray = new byte[paddedBitString.length() / 8];

        int byteCount = paddedBitString.length() / 8;
        for (int i = 0; i < byteCount; i++) {
            String byteSegment = paddedBitString.substring(i * 8, (i + 1) * 8);
            byteArray[i] = (byte) Integer.parseInt(byteSegment, 2);
        }

        return byteArray;
    }

    // Deserializes a byte array into 1s and 0s, removing any extra padding if necessary
    public static String deserializeBytes(byte[] stringBytes, int padding) {
        StringBuilder bitString = new StringBuilder();

        for (byte b : stringBytes) {
            // Convert byte to an 8-bit binary string, ensuring leading zeros
            String bits = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            bitString.append(bits);
        }

        return bitString.toString().substring(0, bitString.length() - padding);
    }

    public String decode(String encodedText) {
        if (encodedText.isEmpty())
            return ""; // empty encodedText

        StringBuilder originalText = new StringBuilder();
        Node huffmanTreeNode = this.root;

        for (int i = 0; i < encodedText.length(); i++) {
            if (this.root instanceof Leaf) { // root is a leaf edge case
                originalText.append(((Leaf) this.root).getCharacter());
                continue;
            }

            //System.out.println(i);

            char instruction = encodedText.charAt(i);

            if (instruction == '0') {
                huffmanTreeNode = huffmanTreeNode.leftNode;
            } else if (instruction == '1') {
                huffmanTreeNode = huffmanTreeNode.rightNode;
            }

            if (huffmanTreeNode instanceof Leaf) {
                originalText.append(((Leaf) huffmanTreeNode).getCharacter());
                huffmanTreeNode = this.root;
            }
        }

        return originalText.toString();
    }

    public static void printTree(Node root) {
        printTreeRecursive(root, 0);
    }

    // Recursive helper function to print the tree with indentation
    private static void printTreeRecursive(Node node, int level) {
        if (node == null) {
            return;
        }

        // Print the right subtree first (so it's printed top to bottom)
        printTreeRecursive(node.rightNode, level + 1);

        // Print the current node's value with indentation for tree level
        for (int i = 0; i < level; i++) {
            System.out.print("   "); // Space for indentation
        }
        System.out.println(node.getFrequency());

        // Print the left subtree
        printTreeRecursive(node.leftNode, level + 1);
    }
}
