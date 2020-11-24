import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;

public class LZWdecode {

  public static void main(String[] args) {
    // using an array turned out to be much faster than a list since we can index right into a specific location in constant time when we need to look up a phrase

    BufferedReader br = null;
    String line = null;

    int length = 256;
    byte[][] phrases = new byte[length * 2][];

    for (int i = 0; i < length; i++) {
      phrases[i] = new byte[]{(byte) i};
    }

    try {
      br = new BufferedReader(new InputStreamReader(System.in));

      // we deal with the first entry a little differently since there's no previous entry to append a phrase to
      line = br.readLine();
      if (line == null) {
        System.out.println("the file is empty.");
        return;
      }
      int key = Integer.parseInt(line);
      byte[] arr = phrases[key];
      byte[] newarr = new byte[arr.length + 1];
      for (int i = 0; i < arr.length; i++) {
        newarr[i] = arr[i];
      }

      phrases[length] = newarr;
      length++;

      System.out.write(arr);


      while ((line = br.readLine()) != null) {
        // if the array is full, make a new array with double the size.
        if (phrases.length == length) {
          byte[][] temp = new byte[2 * length][];
          for (int i = 0; i < length; i++) {
            temp[i] = phrases[i];
          }
          phrases = temp;
        }

        // add first char to last phrase
        // add new phrase with current num
        // output

        key = Integer.parseInt(line);

        arr = phrases[key];

        phrases[length - 1][phrases[length - 1].length - 1] = arr[0];

        // if we are dealing with the last phrase, we just altered it so we need to update our arr
        if (key == length - 1) {
          arr = phrases[key];
        }

        newarr = new byte[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
          newarr[i] = arr[i];
        }

        phrases[length] = newarr;
        length++;

        System.out.write(arr);
      }
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
