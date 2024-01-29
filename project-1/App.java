class SinglyLinkedList<T> {
	public class Node {
		T data;
		Node next;
		Node(T data) {
			this.data = data;
			this.next = null;
		}
	}
	public Node head;
	public int length;
	SinglyLinkedList() {
		this.head = new Node(null);
		this.length = 0;
	}
	void add(T data) {
		Node target = head;
		while (target.next != null) {
			target = target.next;
		}
		target.next = new Node(data);
		length++;
	}
	void remove(T data) {
		Node target = head;
		while (true) {
			if (target.next == null) {
				// data does not exist
				return;
			}
			if (target.next.data == data) {
				// data exists and is at the node following target
				break;
			}
		}
		target.next = target.next.next;
		length--;
	}
}

interface ProcessManager {
	public void create(int parent);
	public void destroy(int targetIndex);
}

class DynamicPM implements ProcessManager {
	private class PCB {
		public Integer parent;
		public SinglyLinkedList<Integer> childPCBIndexes;
		PCB(int parent) {
			this.parent = parent;
			this.childPCBIndexes = new SinglyLinkedList<Integer>();
		}
		PCB() {
			this.parent = null;
			this.childPCBIndexes = new SinglyLinkedList<Integer>();
		}
	}
	private PCB[] pcbs;
	DynamicPM() {
		pcbs = new PCB[]{new PCB()};
	}
	public void create(int parent) {
		PCB[] result = new PCB[pcbs.length + 1];
		for(int i = 0; i < pcbs.length; i++) {
			result[i] = pcbs[i];
		}
		result[pcbs.length] = new PCB(parent);
		result[parent].childPCBIndexes.add(pcbs.length);
		pcbs = result;
	}
	public void destroy(int targetIndex) {
		SinglyLinkedList<Integer>.Node child = pcbs[targetIndex].childPCBIndexes.head;
		while(child.data != null) {
			destroy(child.data);
			pcbs[pcbs[targetIndex].parent].childPCBIndexes.remove(targetIndex);
			child = child.next;
		}
		if(targetIndex == 0) {
			return;
		}
		pcbs[targetIndex] = null;
	}
}

class StaticPM implements ProcessManager {
	private class PCB {
		public Integer parent;
		public Integer firstChild;
		public Integer youngerSibling;
		public Integer olderSibling;
		PCB(int parent) {
			this.parent = parent;
			firstChild = null;
			youngerSibling = null;
			olderSibling = null;
		}
		PCB() {
			this.parent = null;
			firstChild = null;
			youngerSibling = null;
			olderSibling = null;
		}
	}
	private PCB[] pcbs;
	StaticPM() {
		pcbs = new PCB[]{new PCB()};
	}
	public void create(int parent) {
		Integer target = pcbs[parent].firstChild;
		if (target == null) {
			PCB[] result = new PCB[pcbs.length + 1];
			for(int i = 0; i < pcbs.length; i++) {
				result[i] = pcbs[i];
			}
			result[pcbs.length] = new PCB(parent);
			pcbs[parent].firstChild = pcbs.length;
			pcbs = result;
		} else {
			while (pcbs[target].youngerSibling != null) {
				target = pcbs[target].youngerSibling;
			}
			PCB[] result = new PCB[pcbs.length + 1];
			for(int i = 0; i < pcbs.length; i++) {
				result[i] = pcbs[i];
			}
			result[pcbs.length] = new PCB(parent);
			result[target].youngerSibling = pcbs.length;
			result[pcbs.length].olderSibling = target;
			pcbs = result;
		}
	}
	public void destroy(int targetIndex) {

	}
}

class App {
	private static void TestPM(ProcessManager pm) {
		// number of times to run simulation
		final int REPEAT_COUNT = 10000;
		// timestamps in nanoseconds
		long nsStart, nsEnd;
		// timestamp total in seconds
		double sTotal = 0;
		// run test
		for (int i = 0; i < REPEAT_COUNT; i++) {
			nsStart = System.nanoTime();
			pm.create(0);
			pm.create(0);
			pm.create(2);
			pm.create(0);
			pm.destroy(0);
			nsEnd = System.nanoTime();
			sTotal += (double)(nsEnd - nsStart)/1000000000;
		}
		// calculate result
		final double averageSecsPerTest = sTotal/REPEAT_COUNT;
		// print result
		System.out.println("Total secs: " + sTotal);
		System.out.println("Avg secs per test: " + averageSecsPerTest);
	}
	public static void main(String[] args) {
		/* Test Dynamic Process Manager */
		System.out.println();
		System.out.println("Dynamic Process Manager");
		DynamicPM dynamicPM = new DynamicPM();
		TestPM(dynamicPM);
		/* Test Static Process Manager */
		System.out.println();
		System.out.println("Static Process Manager");
		StaticPM staticPM = new StaticPM();
		TestPM(staticPM);
	}
}