# Huffman Coding File Compressor

This is my implementation of a file compressor using Huffman coding. Huffman coding is a lossless data compression algorithm that assigns variable-length binary codes to data symbols. Frequently occuring symbols receive shorter codes, while less frequent symbols receive longer ones. This results in an overall reduction in data size without losing information. A .txt file can be inputted to generate a compressed binary file. This binary file can then be decompressed to retrieve the original .txt file.

![Example of compressing a file](./Assets/Compress.png)

![Example of decompressing a file](./Assets/Decompress.png)

Now available at [huffman-file-compressor.vercel.app](https://huffman-file-compressor.vercel.app/)

## Sample Compressions

### Bee Movie

Compression of a .txt file containing the whole Bee Movie script:

- Original file size: 49,476 bytes
- Compressed file size: 29,314 bytes
- Compression rate: ~41%

![Bee Movie script compression comparison](/Assets/BeeMovie.png)

### Shrek

Compression of a .txt file containing the whole Shrek movie script:

- Original file size: 52,638 bytes
- Compressed file size: 31,685 bytes
- Compression rate: ~40%

![Shrek script compression comparison](/Assets/Shrek.png)

### Toy Story

Compression of a .txt file containing the whole Toy Story movie script:

- Original file size: 168,592 bytes
- Compressed file size: 89,220 bytes
- Compression rate: ~47%

![Toy Story script compression comparison](/Assets/ToyStory.png)

### Average Compression Rate

The average compression rate is about 43%.

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/mfsohail12/FileCompressor.git
   ```
2. Install client-side dependencies:
   ```
   cd client
   npm install
   ```
3. Install server-side dependencies:
   ```
   cd server
   mvn package
   ```

## Usage

1. Start the server:
   ```
   cd server
   mvn spring-boot:run
   ```
2. Start the client:
   ```
   cd client
   npm start
   ```
