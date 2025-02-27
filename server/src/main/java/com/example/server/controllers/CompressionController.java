package com.example.server.controllers;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.huffman.Huffman;

@RestController
@RequestMapping("/compression")
public class CompressionController {

    @PostMapping("/encode")
    public ResponseEntity<?> encodeFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: No file uploaded!");
        }

        try {
            // Read text from uploaded file
            String text = new String(file.getBytes());

            // Compress using Huffman
            Huffman huff = new Huffman(text);
            String encodedText = huff.encode();

            // Serializing data
            byte[] serializedHuffmanTable = huff.serializeHuffmanTable();
            int padding = encodedText.length() % 8 == 0 ? 0 : 8 - (encodedText.length() % 8); // padding for encoded bit string
            byte[] serializedBitString = Huffman.serializeBitString(encodedText);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (DataOutputStream dos = new DataOutputStream(byteStream)) {
                // Write Huffman table length and data
                dos.writeInt(serializedHuffmanTable.length);
                dos.write(serializedHuffmanTable);

                // Write encoded text padding and data
                dos.writeInt(padding);
                dos.write(serializedBitString);
            }

            // Convert memory buffer to a byte array
            byte[] compressedData = byteStream.toByteArray();

            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"compressed.bin\"")
                    .body(compressedData);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }
    }

    @PostMapping("/decode")
    public ResponseEntity<String> decodeFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: No file uploaded!");
        }

        try {
            // Read input stream from uploaded file
            InputStream inputStream = file.getInputStream();
            DataInputStream dis = new DataInputStream(inputStream);

            // Extract huffmanTable
            int huffmanTableLength = dis.readInt();
            byte[] serializedHuffmanTable = new byte[huffmanTableLength];
            dis.readFully(serializedHuffmanTable); // Read the Huffman table bytes

            // Extract encoded text bytes
            int padding = dis.readInt();
            byte[] encodedByteArray = dis.readAllBytes(); // read remaining file data

            dis.close();

            // Creating new Huffman instance with extracted Huffman table
            Huffman huff = new Huffman(Huffman.deserializeHuffmanTable(serializedHuffmanTable));
            String encodedBitString = Huffman.deserializeBytes(encodedByteArray, padding);
            String decodedString = huff.decode(encodedBitString);

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(decodedString);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during decoding");
        }
    }
}
