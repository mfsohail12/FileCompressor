package com.example.server.controllers;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
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

    public static String convertToBitString(byte[] byteArray, int bitLength) {
        StringBuilder bitString = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitString.append(((byteArray[i] >> j) & 1) == 1 ? '1' : '0');
                if (bitString.length() == bitLength)
                    return bitString.toString(); // Stop at correct length
            }
        }
        return bitString.toString();
    }

    public static byte[] convertToByteArray(String bitString) {
        int byteLength = (bitString.length() + 7) / 8; // Round up to full bytes
        byte[] byteArray = new byte[byteLength];

        for (int i = 0; i < bitString.length(); i++) {
            int byteIndex = i / 8;
            int bitIndex = 7 - (i % 8); // Store bits from left to right
            if (bitString.charAt(i) == '1') {
                byteArray[byteIndex] |= (1 << bitIndex);
            }
        }
        return byteArray;
    }

    /*public static byte[] convertToByteArray(String bitString) {
        StringBuilder sb = new StringBuilder(bitString);
        if (bitString.length() % 8 != 0) {
            int padding = 8 - (bitString.length() % 8);
            for (int i = 0; i < padding; i++) {
                sb.append("0");
            }
        }
        byte[] bval = new BigInteger(sb.toString(), 2).toByteArray();
        return bval;
    }*/

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
            //String huffmanTable = huff.serializeHuffmanCodes(); // Store the Huffman table

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (DataOutputStream dos = new DataOutputStream(byteStream)) {
                //byte[] huffmanTableBytes = huffmanTable.getBytes(StandardCharsets.UTF_8);
                byte[] encodedBits = convertToByteArray(encodedText);

                // Write Huffman table length and data
                //dos.writeInt(huffmanTableBytes.length);
                //dos.write(huffmanTableBytes);

                // Write encoded text length and data
                dos.writeInt(encodedBits.length);
                dos.write(encodedBits);
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
        //TODO;
        return null;
    }
}
