import java.io.IOException;

// takes the output file from a LZW encoder and reduces its size at a bit level, assuming each symbol is a byte.
public class LZWunpack {

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

    try {
      while ((key = System.in.read()) != -1) {
        if (length > phraseLimit) {
          bitLength++;
          phraseLimit *= 2;
        }

        bitHolder <<= 8;
        bitHolder |= key;
        bitsHeld += 8;

        while (bitsHeld >= bitLength) {
          // get the first n bits where n = bitLength
          int b = bitHolder >>> (bitsHeld - bitLength);
          System.out.println(b);
          // take those bits out of the holder
          bitsHeld -= bitLength;
          bitHolder = bitHolder << -bitsHeld >>> -bitsHeld;
          // bit shifting won't work here if we need to clear everything out
          if (bitsHeld == 0) {
            bitHolder = 0;
          }
          length++;
        }
      }
    } catch (IOException e) {
      System.out.println("An unexpected file error occurred. Check that the specified input file exists and try again.");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("An unexpected error occurred.");
      e.printStackTrace();
    }

  }
}
