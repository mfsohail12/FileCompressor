package com.example.server.huffman;

public class Node implements Comparable<Node> {
    private int frequency;
    public Node leftNode;
    public Node rightNode;

    public Node(Node leftNode, Node rightNode) {
        this.frequency = leftNode.getFrequency() + rightNode.getFrequency();
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    // used for leaf nodes
    public Node(int frequency) {
        this.frequency = frequency;
        this.leftNode = null;
        this.rightNode = null;
    }

    public Node() {
        this.leftNode = null;
        this.rightNode = null;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int compareTo(Node node) {
        return Integer.compare(this.frequency, node.getFrequency());
    }

}
