### Huffman Coding
## Internet Resources
GeeksforGeeks: website used to understand encode and decode methods. Did not use code snippets from the website. 

## Project Synopsis 
The provided Java code defines a HuffmanSubmit class that implements the Huffman coding algorithm for file compression and decompression. Within the class, an inner myNode class represents nodes in the Huffman tree, along with methods for building the tree which writes its structure to an output file, and creating a code table for character encoding. The buildHuffmanTree method constructs the Huffman tree from a priority queue of nodes based on character frequencies. The writeTreeToOutput method writes the Huffman tree to an output file in a binary format. The buildCodeTable method generates a code table by traversing the Huffman tree. The encode method reads an input file, calculates character frequencies, builds the Huffman tree, creates a code table, writes the tree and encoded message to output files, and generates a separate file for frequency information. The decode method reads the frequency file, reconstructs the Huffman tree, and decodes the binary input file, writing the decoded characters to an output file. The main method demonstrates the functionality by encoding and decoding the "ur.jpg" file. Overall, the code provides a comprehensive implementation of the Huffman coding algorithm in Java, showcasing the construction of Huffman trees, character encoding, and decoding of binary files for compression and decompression tasks.

## File Information
HuffmanSubmit.java: this file contains the encode and decode, and other necessary methods, that are required to finish the project. 

## Command Lines
Make sure the file is in the correct directory then execute the following commands. 

javac *.java

java main
