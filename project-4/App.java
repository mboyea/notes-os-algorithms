import java.util.ArrayList;

public class App {
	/**
	 * Generate a reference string
	 * @param sizeOfVM size of virtual memory as int
	 * @param length the minimum length of reference string
	 * @param sizeOfLocus the size of locus
	 * @param rateOfMotion how many pages in one locus
	 * @param prob the probability of transition
	 * @return the reference string as an ArrayList of Integers
	 */
	public static ArrayList<Integer> createRS(int sizeOfVM, int length, int sizeOfLocus, int rateOfMotion, double prob) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int start = 0;
		int n;
		while (result.size() < length) {
			for (int i = 0; i < rateOfMotion; i++) {
				n = (int)(Math.random() * sizeOfLocus + start);
				result.add(n);
			}
			if (Math.random() < prob) start = (int) Math.random() * sizeOfVM;
			else start++;
		}
		return result;
	}

	// check if the page resides in physical memory
	private static int isInArray(int[] frames, int page) {
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] == page) return i;
		}
		return -1;
	}

	/**
	 * Simulate FIFO replacement algorithm
	 * @param rs Reference string as ArrayList of Integer
	 * @param frameCount Size of physical memory. Initially, pages
	 * 0, 1, ..., frameCount-1 are loaded into physical memory
	 * @return number of page faults if using FIFO Replacement algorithm
	 */
	public static int FIFOReplacement(ArrayList<Integer> rs, int frameCount) {
		int [] frames = new int[frameCount];
		int i, first = 0, count = 0;
		for (i = 0; i < frameCount; i++) frames[i] = i;
		for (i = 0; i < rs.size(); i++) {
			if (isInArray(frames, rs.get(i)) == -1) {
				frames[first] = rs.get(i);
				count++;
				first = (first+1) % frames.length;
			}
		}
		return count;
	}

	/**
	 * Simulate LRU replacement algorithm
	 * @param rs Reference string as ArrayList of Integer
	 * @param frameCount Size of physical memory. Initially, pages
	 * 0, 1, ..., frameCount-1 are loaded into physical memory
	 * @return number of page faults if using LRU Replacement algorithm
	 */
	public static int LRUReplacement(ArrayList<Integer> rs, int frameCount) {
		int [] frames = new int[frameCount];
		int i, j, count = 0;
		for (i = 0; i < frameCount; i++) frames[i] = i;
		for (i = 0; i < rs.size(); i++) {
			int index = isInArray(frames, rs.get(i));
			int least;
			if (index == -1) {
				least = rs.get(i);
				count++;
				index = 0;
			}
			else least = frames[index];
			for (j = index; j < frames.length - 1; j++) {
				frames[j] = frames[j+1];
			}
			frames[j] = least;
		}
		return count;
	}
	public static void main(String[] args) {
		/*
		// Test integrity of functions
		int rss[] = {2, 0, 3, 1, 4, 1, 0, 1, 2, 3};
		ArrayList<Integer> rs = new ArrayList<Integer>();
		for (int i = 0; i < rss.length; i++) rs.add(rss[i]);
		*/

		// Test performance of functions
		ArrayList<Integer> rs = createRS(4096, 10000, 10, 100, 0.1);
		System.out.println("The page fault using FIFO replacement algorithm:");
		System.out.println("\t" + FIFOReplacement(rs, 10) + " times");
		System.out.println("The page fault using LRU replacement algorithm:");
		System.out.println("\t" + LRUReplacement(rs, 10) + " times");
	}
}