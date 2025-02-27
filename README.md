# Huffman Coding FileCompressor

This is my implementation of a file compressor using Huffman coding. Huffman coding is a lossless data compression algorithm that assigns variable-length binary codes to data symbols. Frequently occuring symbols receive shorter codes, while less frequent symbols receive longer ones. This results in an overall reduction in data size without losing information. A .txt file can be inputted to generate a compressed binary file. This binary file can then be decompressed to retrieve the original .txt file.

![Example of compressing a file](https://github.com/mfsohail12/FileCompressor/blob/main/Assets/Compress.png)

![Example of decompressing a file](https://github.com/mfsohail12/FileCompressor/blob/main/Assets/Decompress.png)

## Sample Compressions

### Bee Movie
Compression of a .txt file containing the whole Bee Movie script:

![Bee Movie script compression comparison](https://github.com/mfsohail12/FileCompressor/blob/main/Assets/BeeMovie.png)

Compression rate: 41% 

### Shrek
Compression of a .txt file containing the whole Shrek movie script:

![Shrek script compression comparison](https://github.com/mfsohail12/FileCompressor/blob/main/Assets/Shrek.png)

Compression rate: 40%

### Toy Story
Compression of a .txt file containing the whole Toy Story movie script:

![Toy Story script compression comparison](https://github.com/mfsohail12/FileCompressor/blob/main/Assets/ToyStory.png)

Compression rate: 47%

Average Compression rate: 
