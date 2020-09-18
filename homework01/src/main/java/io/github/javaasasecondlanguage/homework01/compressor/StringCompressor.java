//package io.github.javaasasecondlanguage.homework01.compressor;

public class StringCompressor {
    /**
     * Given an array of characters, compress it using the following algorithm:
     *
     * Begin with an empty string s.
     * For each group of consecutive repeating characters in chars:
     * If the group's length is 1, append the character to s.
     * Otherwise, append the character followed by the group's length.
     * Return a compressed string.
     * </p>
     * Follow up:
     * Could you solve it using only O(1) extra space?
     * </p>
     * Examples:
     * a -> a
     * aa -> a2
     * aaa -> a3
     * aaabb -> a3b2
     * "" -> ""
     * null -> Illegal argument
     * 234 sdf -> Illegal argument
     *
     * @param str nullable array of chars to compress
     *            str may contain illegal characters
     * @return a compressed array
     * @throws IllegalArgumentException if str is null
     * @throws IllegalArgumentException if any char is not in range 'a'..'z'
     */
    public static char[] compress(char[] str) {
		if (str == null) {
			throw new IllegalArgumentException("Incorrect argument " + str);
		}

		char []output = new char[str.length];
		int inPtr = 0;
		int inPtrStart = 0;
		int outPtr = 0;
		int chunkSize = 0;

		while (inPtr < str.length) {
			if (str[inPtr] < 'a' || str[inPtr] > 'z') {
				throw new IllegalArgumentException("Incorrect value " + str[inPtr]);
			}

			if (str[inPtr] == str[inPtrStart]) {
				chunkSize++;
				inPtr++;
				continue;
			}

			output[outPtr] = str[inPtrStart];
			outPtr++;
			if (chunkSize > 1) {
				String count = String.valueOf(chunkSize);
				count.getChars(0, count.length(), output, outPtr);
				outPtr += count.length();
			}
			chunkSize = 0;
			inPtrStart = inPtr;

		}

		if (chunkSize > 0) {
			output[outPtr] = str[inPtrStart];
			outPtr++;
			if (chunkSize > 1) {
				String count = String.valueOf(chunkSize);
				count.getChars(0, count.length(), output, outPtr);
				outPtr += count.length();
			}
		}

		return output;
    }

	public static void main(String [] argv) {
		char [] t = compress(new char [] {'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'b', 'b', 'c', 'c'});
		System.out.println(t);
		t = compress(new char[0]);
		System.out.println(t.length + " " + new String(t));
	}

}
