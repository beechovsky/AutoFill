package proj3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * HashedRBTs.java of Proj3
 * Contains Red-Black trees in an array list
 * of user-specified size
 * @param <AnyType>
 */
public class HashedRBTs<AnyType extends Comparable<? super AnyType>>
{
    private int arraySize; // Will be determined by user input from Driver class

    ArrayList<RedBlackTree<AnyType>> table;

     /**
     * Constructor
     * @param size user specified arraylist size
     */
    public HashedRBTs(int size)
    {
        arraySize = size;

        table = new ArrayList<RedBlackTree<AnyType>>(arraySize);

        for(int index = 0; index < arraySize; index++)
        {
            RedBlackTree<AnyType> tree = new RedBlackTree<AnyType>();
            table.add(tree);
        }
    }

    /**
     * Scans and parses data from input
     * Inserts parsed words into proper tree
     * @param fileName accepts input file
     */
    public void fileReader(String fileName)
    {
        Scanner readFile;
        try
        {
            readFile = new Scanner(new File(fileName));
            Node temp;
            final int UC_DIFF = 65; // Distance between Upper case ascii values and an alphabetical index
            final int LC_DIFF = 71; // Distance between Lower case ascii values and an alphabetical index

            while(readFile.hasNextLine())
            {

                String current = readFile.nextLine(); // store incoming data

                if(current != null)
                {
                    // Use a split to break up the text into more manageable parts
                    String[] words = current.split(" "); // split on spaces
                    String word = words[1].substring(6).replaceAll(",", ""); // ignore first section of split, and words=
                    int frequency = Integer.parseInt(words[2].substring(10).replaceAll("]", "")); // ignore frequency=
                    temp = new Node(word, frequency);

                    // Use First Char of each word to identify proper index
                    // Upper Case
                    if(temp.getWord().charAt(0) < 91) // Upper Case Letters
                    {
                        // Instantiate partial here
                        int index = temp.getWord().charAt(0) - UC_DIFF;
                        Partial part = new Partial(temp);
                        RedBlackTree<AnyType> tempTree = table.get(index); //cast table index to red black tree
                        if(tempTree.retreiveIfItContains((AnyType)part) == null)
                        {
                            tempTree.insert((AnyType)part);
                        }
                        else
                        {
                            tempTree.retreiveIfItContains((AnyType)part).insertNodeIntoHeap(temp);
                        }
                    }

                    // Lower Case
                    if(temp.getWord().charAt(0) > 96) // Lower Case Letters
                    {
                        // Instantiate partial here
                        int index = temp.getWord().charAt(0) - LC_DIFF;
                        Partial part = new Partial(temp);
                        RedBlackTree<AnyType> tempTree = table.get(index); //cast table index to red black tree
                        if(tempTree.retreiveIfItContains((AnyType)part) == null)
                        {
                            tempTree.insert((AnyType)part);
                        }
                        else
                        {
                            tempTree.retreiveIfItContains((AnyType)part).insertNodeIntoHeap(temp);
                        }
                    }
                }
            }

            // Stop reading input
            readFile.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * retreiveHashedBSTat - I left "retreive" misspelled as not to upset Driver
     * Returns the tree stored at a user specified index
     * @param index accepts int for arraylist index
     * @return returns a BinarySeachTree
     */
    public RedBlackTree<AnyType> retreiveHashedRBTat(int index)
    {
        return table.get(index);
    }

    /**
     * PrintHashCountResults
     * Ouputs data regarding contents of a tree
     */
    public void printHashCountResults()
    {
        for(RedBlackTree tree : table) // Proudly implementing my first enhanced for loop
        {
            if(tree.isEmpty())
            {
                System.out.println("This tree has no nodes.\n");
            }
            else
                tree.printRoot();
        }
    }
}
