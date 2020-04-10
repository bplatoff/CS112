package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords
	 *            Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);
		TrieNode previous = null;
		String wordHold = allWords[0];
		root.firstChild = new TrieNode(new Indexes(0, (short) 0, (short) (wordHold.length() - 1)), null, null);
		for (int i = 1; i < allWords.length; i++) {
			wordHold = allWords[i];
			previous = null;
			System.out.println("wordHold = " + wordHold);
			TrieNode a = root.firstChild;
			boolean q = false;

			while (!q) {
				System.out.println(a.substr.wordIndex + "= word index");
				System.out.println(allWords[a.substr.wordIndex]);
				String test = allWords[a.substr.wordIndex].substring(a.substr.startIndex, a.substr.endIndex + 1);
				System.out.println(
						"wordHold at 0 = " + wordHold.charAt(a.substr.startIndex) + " test at 0 = " + test.charAt(0));
				if (wordHold.charAt(a.substr.startIndex) == test.charAt(0)) {
					System.out.println("2");
					// System.out.println("test = " + test + ", wordHold substring = " +
					// wordHold.substring(a.substr.startIndex,a.substr.endIndex+1));
					String holdsub;
					if (wordHold.length() < a.substr.endIndex + 1) {
						holdsub = wordHold;
					} else {
						holdsub = wordHold.substring(a.substr.startIndex, a.substr.endIndex + 1);
					}
					System.out.println("test = " + test + ", wordHold substring = " + holdsub);
					if (holdsub.equals(test)) {
						previous = a;
						a = a.firstChild;
						System.out.println("wordHold = " + wordHold);
						System.out.println("3");
					} else {
						System.out.println("4");
						boolean k = false;
						int holder;
						if (wordHold.length() < a.substr.endIndex + 1) {
							holder = wordHold.length();
						} else
							holder = a.substr.endIndex + 1;
						while (!k) {
							holder--;
							if (wordHold.substring(a.substr.startIndex, holder)
									.equals(allWords[a.substr.wordIndex].substring(a.substr.startIndex, holder))) {
								k = true;
							}
						}
						TrieNode safe = a;
						a = new TrieNode(
								new Indexes(safe.substr.wordIndex, safe.substr.startIndex, (short) (holder - 1)),
								new TrieNode(new Indexes(safe.substr.wordIndex, (short) (holder), safe.substr.endIndex),
										safe.firstChild,
										new TrieNode(new Indexes(i, (short) (holder), (short) (wordHold.length() - 1)),
												null, null)),
								safe.sibling);
						if (previous == null) {
							root.firstChild = a;
							q = true;
						} else {
							if (previous.firstChild == safe) {
								previous.firstChild = a;
								q = true;
							}
							if (previous.sibling == safe) {
								previous.sibling = a;
								q = true;
							}
						}
						q = true;
					}
				} else {
					if (a.sibling == null) {
						a.sibling = new TrieNode(new Indexes(i, a.substr.startIndex, (short) (wordHold.length() - 1)),
								null, null);
						System.out.println("5");
						q = true;
					} else {
						previous = a;
						a = a.sibling;
					}
				}
			}
		}
		return root;
	}

	private static int sharedPrefix(String word1, String word2) {
		int commonPrefixWithSimilar = 0;
		int minLength = Math.min(word1.length(), word2.length());
		
		for (int i = 0; i < minLength; i++) {
			
			if (word1.charAt(0) != word2.charAt(0)) {
				return -1;
			}
			
			if (word1.charAt(i) == word2.charAt(i)) {
				commonPrefixWithSimilar++;
			}
			else {
				return commonPrefixWithSimilar;
			}
		}
		return commonPrefixWithSimilar;
	}
	
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {

		ArrayList<TrieNode> holder = new ArrayList<TrieNode>();
		TrieNode pointer = root.firstChild;

		int min = prefix.length();

		return leaf(pointer, holder, allWords, min, prefix);
	}

	private static ArrayList<TrieNode> leaf(TrieNode pointer, ArrayList<TrieNode> holder, String[] allWords,
			int min, String prefix) {

		while (pointer != null) {
			String word = allWords[pointer.substr.wordIndex].substring(0, pointer.substr.endIndex + 1);
			int commonLongestPrefix = sharedPrefix(word, prefix);
			if (commonLongestPrefix == -1) {
				pointer = pointer.sibling;
			} else {
				if (commonLongestPrefix < min) {
					if (pointer.firstChild != null) {
						holder = leaf(pointer.firstChild, holder, allWords, min, prefix);
						if (holder == null) {
							holder = new ArrayList<TrieNode>();
						}
						pointer = pointer.sibling;
					} else {
						pointer = pointer.sibling;
					}

					// This is finding the answers
				} else {
					if (pointer.firstChild != null) {
						holder = leaf(pointer.firstChild, holder, allWords, min, prefix);
						pointer = pointer.sibling;
						if (holder == null) {
							holder = new ArrayList<TrieNode>();
						}
					} else {
						holder.add(pointer);
						pointer = pointer.sibling;
					}
				}
			}
		}
		// Thats all me
		if (holder == null || holder.isEmpty() == true) {
			return null;
		}
		return holder;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
