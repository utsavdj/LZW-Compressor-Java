import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

// takes the output file from a LZW encoder and reduces its size at a bit level, assuming each symbol is a byte.
public class LZWpack {

  public static void main(String[] args) {
    // assuming we're using bytes as the symbol set, we have 256 phrases in the dictionary to start with
    int length = 256;
    // this should always be 8 initially but if we change the length we want to this will automatically accomodate for that
    int bitLength = (int) Math.ceil(Math.log(length) / Math.log(2));
    // what's the highest number of phrases we can encode with the current bit length
    // checking the current length against this instead of doing logs every phrase should be more efficient
    // we can only encode the first phrase with our 8 bits before increasing our length but hey might as well save that one bit
    int phraseLimit = (int) Math.pow(2, bitLength);

    int key;

    // we'll use an int to hold the data we read in - initlaised to have all zeroes
    int bitHolder = 0;
    // how many bits are currently in the holder
    int bitsHeld = 0;

    String line = null;
    String output = "";

    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      while ((line = br.readLine()) != null) {
        if (length > phraseLimit) {
          bitLength++;
          phraseLimit *= 2;
        }

        key = Integer.parseInt(line);
        bitHolder <<= bitLength;
        bitHolder |= key;
        bitsHeld += bitLength;

        while (bitsHeld >= 8) {
          int mask = (0xFF << (bitsHeld - 8));
          // get the last first bits in the bitholder and write them to the writer
          int b = bitHolder & mask;
          b >>= (bitsHeld - 8);
          System.out.write(b);
          // take those bits out of the holder
          bitHolder &= ~mask;
          bitsHeld -= 8;
        }

        length++;
      }
      // write whatever's left in the bitHolder to the output stream
      bitHolder <<= (8 - bitsHeld);
      System.out.write(bitHolder);
      System.out.flush();
    } catch (IOException e) {
      System.out.println("An unexpected file error occurred. Check that the specified input file exists and try again.");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("An unexpected error occurred.");
      e.printStackTrace();
    }

  }
}
