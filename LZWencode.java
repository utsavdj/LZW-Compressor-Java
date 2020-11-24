import java.io.BufferedInputStream;
import java.io.IOException;

// encoder based on LZW algorithm
public class LZWencode {

  //   takes in standard input, encodes it using LZW algorithm and produces standard output
  public static void main(String[] args) {

    Encode encode = new Encode();

    BufferedInputStream bis;
    try {
      bis = new BufferedInputStream(System.in);
      int i;
      while ((i = bis.read()) != -1) {
        encode.trie(i, bis);
      }
//    outputs the final phrase number
      System.out.println(encode.getCurrentNodePhraseNo());
      bis.close();
    } catch (IOException e) {
      System.err.println("IO Exception: " + e);
    } catch (Exception e) {
      System.err.println("Exception: " + e);
    }
  }

  //  encodes input by creating a multiway trie as per LZW algorithm and outputs phrase number
  private static class Encode {
    // number of all the possible initial phrases for 8 bit i.e. 0-255
    private final int INITIAL_PHRASES = 256;

    /* binary search tree for the node in the multi way trie which stores the left child and right child of the
       tree, the tree's value and an exit node.
    */
    private class BST {
      private int value;
      private BST leftChild;
      private BST rightChild;
      private Node node;

      private BST(int value, int phraseNo) {
        this.value = value;
        this.node = new Node(value, phraseNo);
      }
    }

    /* node for the multi way trie which stores the value, phrase number, inital phrases for the root node and
       binary search tree
    */
    private class Node {
      private int value;
      private int phraseNo;

      private Node[] initialPhrases;
      private BST bst;

      private Node(int value, int phraseNo) {
        this.value = value;
        this.phraseNo = phraseNo;
        // Sets the initial phrases for the root
        if (this.phraseNo == -1) {
          initialPhrases = new Node[INITIAL_PHRASES];
        }
      }
    }

    private int phraseNo;
    private Node root;
    private Node currentNode;
    private BST currentBST;

    // constructor to set the phrase number, root node, current node and initial phrases on initialization of Encode
    public Encode() {
      phraseNo = -1;
      root = new Node(' ', phraseNo);
      currentNode = root;

      // Creates all the possible initial phrases as per the bit size
      for (int i = 0; i < INITIAL_PHRASES; i++) {
        phraseNo = i;
        root.initialPhrases[i] = new Node(i, phraseNo);
      }
    }

    // creates a multi way trie for the LZW encoder
    private void trie(int value, BufferedInputStream bis) {
      if (currentBST == null && currentNode.initialPhrases == null) {
        outputMismatchPhraseAndIncreasePhraseNo();
        currentNode.bst = new BST(value, phraseNo);
        setCurrentReferencesAndResetInputBuffer(currentNode.bst, bis);
      } else {
        if (currentNode.initialPhrases != null) {
          // Selects the node with initial matching phrase
          currentNode = root.initialPhrases[value];
          currentBST = currentNode.bst;
          /* Mark a position in the input stream so that the stream can be reset to start from the mismatch
             phrase after it reaches the mismatch phrase
          */
          /* In the mark method an integer 1 is passed as an argument as a read limit of 1 byte for the buffer
             because the latest matching character that calls the mark method will always be 1 byte apart from
             the mismatch character so that once the input stream is reset after reaching the mismatch character,
             the stream will reset 1 byte back and start from the same mismatch character
          */
          bis.mark(1);
        } else {
          if (currentBST.value == value) {
            currentNode = currentBST.node;
            currentBST = currentNode.bst;
            bis.mark(1);
          } else if (currentBST.value > value) {
            if (currentBST.leftChild == null) {
              outputMismatchPhraseAndIncreasePhraseNo();
              currentBST.leftChild = new BST(value, phraseNo);
              setCurrentReferencesAndResetInputBuffer(currentNode.bst, bis);
            } else {
              currentBST = currentBST.leftChild;
              trie(value, bis);
            }
          } else {
            if (currentBST.rightChild == null) {
              outputMismatchPhraseAndIncreasePhraseNo();
              currentBST.rightChild = new BST(value, phraseNo);
              setCurrentReferencesAndResetInputBuffer(currentNode.bst, bis);
            } else {
              currentBST = currentBST.rightChild;
              trie(value, bis);
            }
          }
        }
      }
    }

    // outputs the mismatch phrase number and increases the phrase number by 1; returns void
    private void outputMismatchPhraseAndIncreasePhraseNo() {
      System.out.println(currentNode.phraseNo);
      phraseNo++;
    }

    // sets the current node, current binary search tree and resets the buffered input stream; returns void
    private void setCurrentReferencesAndResetInputBuffer(BST currentBST, BufferedInputStream bis) {
      currentNode = root;
      this.currentBST = currentBST;
      try {
         /* Resets the input stream so that the stream starts after the latest matching character which is just
            before the mismatch character that calls the mark method that is the stream will start back from the
            same mismatch character
        */
        bis.reset();
      } catch (IOException e) {
        System.err.println("IO Exception: " + e);
      } catch (Exception e) {
        System.err.println("Exception: " + e);
      }
    }

    // returns the phrase number of the current node
    public int getCurrentNodePhraseNo() {
      return currentNode.phraseNo;
    }

  }
}
