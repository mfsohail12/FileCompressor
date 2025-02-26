package com.example.server.huffman;

public class Leaf extends Node {
    private char character;

    public Leaf(char character, int frequency) {
        super(frequency);
        this.character = character;
    }

    public Leaf(char character) {
        super();
        this.character = character;
    }

    public char getCharacter() {
        return this.character;
    }
}
