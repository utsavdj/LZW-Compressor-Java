LZW Compressor in Java
========================================

**Included test files**: MobyDick.txt and BrownCorpus.txt are text files and image.RAF
is a raw image file.

Instructions
------------

**Compile Java Code**: Enter the following command in the terminal to compile the java code from the terminal in the path where the java files are present:

```
java_file_path>javac LZWencode.java LZWpack.java LZWunpack.java LZWdecode.java
```

**Compression**: Enter the following command in the terminal to compress a text file or raw image, where filename and extension can be set as desired.

```
java_file_path>java LZWencode.java < MobyDick.txt | java LZWpack.java > *filename*.*extension*
```

**Decompression**: Enter the following command in the terminal to decompress the previously compressed file, where compressed_filename and compress_extension should be the filename and extension of the compressed file. The filename when decompressed can be set as desired but, the extension should be the same as the original extension. 
```
java_file_path>LZWunpack.java < *compressed_filename*.*compressed_extension* | java LZWdecode.java > *filename*.*extension*
```