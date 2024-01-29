import java.util.Arrays;

class Random {
	static int range(int min, int max) {
		return (int)(Math.random() * (max - min + 1) + min);
	}
}

class Producer extends Thread {
	private final int[] buffer;
	private final int kMin;
	private final int kMax;
	private final int tMin;
	private final int tMax;
	public Producer(int[] buffer, int kMin, int kMax, int tMin, int tMax) {
		this.buffer = buffer;
		this.kMin = kMin;
		this.kMax = kMax;
		this.tMin = tMin;
		this.tMax = tMax;
	}
	public void run() {
		int target = 0;
		while(true) {
			final int k = Random.range(kMin, kMax);
			for (int i = 0; i < k - 1; i++) {
				buffer[(target + i) % buffer.length] += 1;
			}
			target = (target + k) % buffer.length;

			final int t = Random.range(tMin, tMax);
			try {
				Thread.sleep(t);
			} catch(InterruptedException e) {
				System.out.println(e);
				System.exit(0);
			}
			System.out.println("Producer: Produced " + k + ", Waited " + t);
		}
	}
}

class Consumer extends Thread {
	private final int[] buffer;
	private final int kMin;
	private final int kMax;
	private final int tMin;
	private final int tMax;
	public Consumer(int[] buffer, int kMin, int kMax, int tMin, int tMax) {
		this.buffer = buffer;
		this.kMin = kMin;
		this.kMax = kMax;
		this.tMin = tMin;
		this.tMax = tMax;
	}
	public void run() {
		int target = 0;
		while (true) {
			final int t = Random.range(tMin, tMax);
			try {
				Thread.sleep(t);
			} catch(InterruptedException e) {
				System.out.println(e);
				System.exit(0);
			}

			final int k = Random.range(kMin, kMax);
			for (int i = 0; i < k - 1; i++) {
				final int data = buffer[(target + i) % buffer.length];
				if (data > 1) {
					System.out.println("Race Condition: Consumer was too slow!");
					System.exit(0);
				} else if (data == 0) {
					System.out.println("Consumer: Waited " + t + ", Consumed " + k);
					System.out.println("Race Condition: Producer was too slow!");
					System.exit(0);
				} else {
					buffer[(target + i) % buffer.length] = 0;
				}
			}
			target = (target + k) % buffer.length;
			System.out.println("Consumer: Waited " + t + ", Consumed " + k);
		}
	}
}

class App {
	public static void main(String[] args) {
		final int BUFFER_SIZE = 20;
		int[] buffer = new int[BUFFER_SIZE];
		Arrays.fill(buffer, 0);

		final int PRODUCE_MIN = 1;
		final int PRODUCE_MAX = 5;
		final int PRODUCER_WAIT_MIN = 300;
		final int PRODUCER_WAIT_MAX = 500;
		Producer producer = new Producer(
			buffer,
			PRODUCE_MIN,
			PRODUCE_MAX,
			PRODUCER_WAIT_MIN,
			PRODUCER_WAIT_MAX
		);

		final int CONSUME_MIN = 1;
		final int CONSUME_MAX = 5;
		final int CONSUMER_WAIT_MIN = 300;
		final int CONSUMER_WAIT_MAX = 500;
		Consumer consumer = new Consumer(
			buffer,
			CONSUME_MIN,
			CONSUME_MAX,
			CONSUMER_WAIT_MIN,
			CONSUMER_WAIT_MAX
		);

		producer.start();
		consumer.start();
	}
}