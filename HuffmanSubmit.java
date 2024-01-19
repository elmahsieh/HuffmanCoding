import java.io.*;
import java.util.*;

public class HuffmanSubmit implements Huffman {
    private static final int ASCII = 256;
    private static String temp;

    static class myNode implements Comparable<myNode> {
        Character value = null;
        Integer frequency = null;
        myNode leftNode = null;
        myNode rightNode = null;


        public myNode(Character value, Integer frequency, myNode leftNode, myNode rightNode) {
            this.value = value;
            this.frequency = frequency;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        @Override
        public int compareTo(myNode other) {
            return this.frequency - other.frequency;
        }

        @Override
        public String toString() {
            if (value != null) {
                return "Node(value=" + value + ")";
            } else {
                return "Node(left=" + leftNode + ", right=" + rightNode + ")";
            }
        }

        private boolean isLeaf() {
            if (leftNode == null && rightNode == null) {
                return true;
            } else if (leftNode != null && rightNode != null) {
                return false;
            } else {
                throw new IllegalStateException("Node has only one child");
            }
        }
    }
    @Override
    public void encode(String inputFile, String outputFile, String freqFile) {
        BinaryIn in = new BinaryIn(inputFile);

        BinaryOut out = new BinaryOut(outputFile);

        String stringContent = in.readString();

        // This char array monitors the values of the input using ASCII
        char[] input = stringContent.toCharArray();

        // This int array monitors the frequency of each char
        int[] frequency = new int[ASCII];
        temp = stringContent;

        // This loop calculates the freq of each char.
        for (int i = 0; i < input.length; i++)
            frequency[input[i]]++;

        // Builds the Huffman Trie
        myNode root = buildHuffmanTree(frequency);

        // Builds the code table
        String[] table = new String[ASCII];

        buildCodeTable(table, root, "");

        writeTreeToOutput(root, outputFile);

        out.write(input.length);
        // This loop writes the encoded message to the output file
        for (int i = 0; i < input.length; i++) {

            String data = table[input[i]];

            // this writes each bit og the code to the output file
            for (int j = 0; j < data.length(); j++) {

                char bit = data.charAt(j);

                switch (bit) {

                    case '0':
                        out.write(false);
                        break;

                    case '1':
                        out.write(true);
                        break;

                    default:
                        throw new IllegalStateException("Invalid bit value: " + bit);
                }
            }
        }
        // This for loop writes the freq table to the freq file
        try (FileWriter freq = new FileWriter(freqFile)) {

            for (int i = 0; i < frequency.length; i++) {

                freq.write(Integer.toBinaryString(i));
                freq.write(":");
                freq.write("" + frequency[i]);
                freq.write("\n");
            }
        } catch (IOException e) {
        }

        out.close();
    }

    @Override
    public void decode(String inputFile, String outputFile, String freqFile) throws IOException {
        BinaryIn in = new BinaryIn(freqFile);

        String content = in.readString();

        Scanner scanner = new Scanner(content);

        // Stores char in freq file
        ArrayList<Character> charList = new ArrayList<>();

        // HashMap to store freq of each char in file
        HashMap<Character, Integer> hashMap = new HashMap<>();

        // reads freq file line by line
        while (scanner.hasNextLine()) {

            String temp = scanner.nextLine();
            Character c = (char) Integer.parseInt(temp.substring(0, temp.indexOf(":")), 2);
            Integer i = Integer.parseInt(temp.substring(temp.indexOf(":") + 1));

            hashMap.put(c, i);
        }
        // priority queue to build Huffman Tree
        PriorityQueue<myNode> queue = new PriorityQueue<>();

        // Adds each char to queue as a new node
        for (Character key : hashMap.keySet()) {

            Integer value = hashMap.get(key);
            queue.add(new myNode(key, value, null, null));
        }
        // Builds Huffman tree
        while (queue.size() > 1) {

            myNode left = queue.poll();
            myNode right = queue.poll();
            myNode newNode = new myNode('\0', left.frequency + right.frequency, left, right);

            queue.add(newNode);
        }
        // Reads from input file
        BinaryIn input = new BinaryIn(inputFile);

        charList.add('a');
        int total = input.readInt();

        // stores decoded char
        ArrayList<Character> characters = new ArrayList<>();

        // Read the Huffman binary from the input and extract the corresponding
        // characters
        while (--total > 0) {

            try {
                myNode tempNode = queue.peek();

                while (!tempNode.isLeaf()) {
                    Boolean bit = input.readBoolean();
                    if (bit)
                        tempNode = tempNode.rightNode;
                    else
                        tempNode = tempNode.leftNode;
                }

                characters.add(tempNode.value);

            } catch (Exception e) {
                break;
            }
        }

        // writes ascii values into output file
        BinaryOut output = new BinaryOut(outputFile);

        for (Character c : charList)
            output.write(temp);
        output.close();

    }

    /**
     * My Own Tree:
     * Constructs the Huffman tree from a priority queue of nodes by iteratively merging the nodes with the lowest
     * frequencies until a single root node remains, which represents the Huffman coding scheme.
     */

    private static myNode buildHuffmanTree(int[] frequency) {
        PriorityQueue<myNode> priorityQueue = new PriorityQueue<>();

        for (char character = 0; character < ASCII; character++) {
            if (frequency[character] > 0) {
                myNode node = new myNode(character, frequency[character], null, null);
                priorityQueue.add(node);
            }
        }
        while (priorityQueue.size() > 1) {

            myNode left = priorityQueue.poll();
            myNode right = priorityQueue.poll();
            myNode parent = new myNode('\0', left.frequency + right.frequency, left, right);

            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }

    // This method writes the Huffman Tree to the output file
    private static void writeTreeToOutput(myNode node, String outputFile) {

        BinaryOut output = new BinaryOut(outputFile);

        if (node.isLeaf()) {
            output.write(true);
            output.write(node.value);
            return;
        }

        output.write(false);

        writeTreeToOutput(node.leftNode, outputFile);
        writeTreeToOutput(node.rightNode, outputFile);
    }

    // This method builds the code table
    private static void buildCodeTable(String[] table, myNode node, String content) {

        if (!node.isLeaf()) {
            buildCodeTable(table, node.leftNode, content + '0');
            buildCodeTable(table, node.rightNode, content + '1');
        } else
            table[node.value] = content;
    }
    public static void main(String[] args) throws IOException {
        Huffman  huffman = new HuffmanSubmit();

        // Encode & Decode ur.jpg
        huffman.encode("ur.jpg", "ur.enc", "freq.txt");
        huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");

    }

}