package com.example.server.huffman;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private void buildHuffmanTree() {
        this.root = new Node();

        for (HashMap.Entry<Character, String> entry : this.huffmanCodes.entrySet()) {
            char character = entry.getKey();
            String code = entry.getValue();

            Node currNode = this.root;

            for (int i = 0; i < code.length(); i++) {
                if (i == code.length() - 1) { // creating the leaf node in the tree
                    if (code.charAt(i) == '0') {
                        if (currNode.leftNode == null) {
                            currNode.leftNode = new Leaf(character);
                        }
                    } else if (code.charAt(i) == '1') {
                        if (currNode.rightNode == null) {
                            currNode.rightNode = new Leaf(character);
                        }
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

    private void updateCharFrequencies() {
        for (int i = 0; i < this.text.length(); i++) {
            char currChar = this.text.charAt(i);
            Integer currCharFreq = this.charFrequencies.get(currChar);
            this.charFrequencies.put(currChar, currCharFreq == null ? 1 : currCharFreq + 1);
        }
    }

    public String encode() {
        if (this.text.isEmpty())
            return ""; // empty string given to encode

        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        this.charFrequencies.forEach((character, frequency) -> queue.add(new Leaf(character, frequency)));

        while (queue.size() > 1) {
            queue.add(new Node(queue.poll(), queue.poll())); // creates a new node using the smallest 2 frequencies
        }

        this.root = queue.poll();
        generateHuffmanCodes(this.root, "");

        return getEncodedText();
    }

    private void generateHuffmanCodes(Node root, String code) {
        if (root instanceof Leaf) {
            if (code.isEmpty()) {
                code = "0"; // Assign at least one bit
            }
            this.huffmanCodes.put(((Leaf) root).getCharacter(), code);
            return;
        }
        generateHuffmanCodes(root.leftNode, code + "0");
        generateHuffmanCodes(root.rightNode, code + "1");
    }

    public String serializeHuffmanCodes() {
        StringBuilder sb = new StringBuilder();
        for (var entry : huffmanCodes.entrySet()) {
            String character = entry.getKey().toString();
            String code = entry.getValue();

            // Escape the special characters (e.g., newline, colon)
            character = character.replace("\n", "\\n").replace(":", "\\:");
            sb.append(character).append(":").append(code).append("\n");
        }
        return sb.toString();
    }


    private String getEncodedText() {
        StringBuilder encodedText = new StringBuilder();
        for (int i = 0; i < this.text.length(); i++) {
            char currChar = this.text.charAt(i);
            encodedText.append(this.huffmanCodes.get(currChar));
        }
        return encodedText.toString();
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

    public static void main(String[] args) throws IOException {
        Huffman huff = new Huffman("lolsies\nlosies");

        String enc = huff.encode();
        String og = huff.decode(enc);

        System.out.println(og);
    }
}
