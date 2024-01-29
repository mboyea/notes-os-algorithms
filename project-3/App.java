import java.util.Scanner;

public class App {
	public static class ClaimGraph {
		public int processCount, resourceCount;
		public int[] resources;
		public int[][] currentAllocation, maxRequests;

		ClaimGraph(int processCount, int[] resources, int[][] currentAllocation, int[][] maxRequests) {
			this.processCount = processCount;
			this.resourceCount = resources.length;
			this.resources = resources;
			this.currentAllocation = currentAllocation;
			this.maxRequests = maxRequests;
		}

		public void initialize(int processCount, int[] resources, int[][] currentAllocation, int[][] maxRequests) {
			this.processCount = processCount;
			this.resourceCount = resources.length;
			this.resources = resources;
			this.currentAllocation = currentAllocation;
			this.maxRequests = maxRequests;
		}

		public boolean request(int p, int r, int amount) {
			if (resources[r] < amount) return false;
			if (maxRequests[p][r] - currentAllocation[p][r] < amount) return false;
			resources[r] -= amount;
			currentAllocation[p][r] += amount;
			return true;
		}

		public void release(int p, int r, int amount) {
			resources[r] += amount;
			currentAllocation[p][r] -= amount;
		}

		public boolean isDeadlocked() {
			int cantFinishCount = 0;
			boolean isDetected = false;
			for (int i = 0; i < processCount; i++) {
				isDetected = false;
				for (int j = 0; !isDetected && j < resourceCount; j++) {
					if (maxRequests[i][j] > resources[j]) isDetected = true;
				}
				if (isDetected) cantFinishCount++;
			}
			return cantFinishCount == processCount;
		}

		public String toString() {
			String result = "Claim Matrix: \n";
			int i, j;
			for (i = 0; i < processCount; i++) {
				for (j = 0; j < resourceCount; j++) {
					result += "\t" + maxRequests[i][j];
				}
				result += "\n";
			}
			result += "\nAllocationMatrix: \n";
			for (i = 0; i < processCount; i++) {
				for (j = 0; j < resourceCount; j++) {
					result += "\t" + currentAllocation[i][j];
				}
				result += "\n";
			}
			result += "\nAvailable Resources:\n";
			for (i = 0; i < resourceCount; i++) result += "\t" + resources[i];
			return result;
		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		ClaimGraph cg;
		{
			System.out.println("Enter number of processes: ");
			int p = input.nextInt();
			System.out.println("Enter number of resources: ");
			int r = input.nextInt();
			int[] resources = new int[r];
			System.out.println("Enter number of each resource, searated by whitespace: ");
			int i, j;
			for (i = 0; i < r; i++) resources[i] = input.nextInt();
			System.out.println("Enter claim matrix: ");
			int[][] maxRequests = new int[p][r];
			for (i = 0; i < p; i++) {
				System.out.println("Enter maximum requests of process " + i + " for each resource separated by white space: ");
				for (j = 0; j < r; j++) maxRequests[i][j] = input.nextInt();
			}

			cg = new ClaimGraph(p, resources, new int[p][r], maxRequests);
		}

		System.out.println("System initial state is: \n" + cg);
		while (!cg.isDeadlocked()) {
			System.out.println("Enter one of the accepted commands:");
			System.out.println("1.) request(process, resource, amount)");
			System.out.println("2.) release(process, resource, amount)");
			String command[] = input.nextLine().split("\\(|,|\\)");
			if (command.length == 4) {
				int p = Integer.parseInt(command[1].trim());
				int r = Integer.parseInt(command[2].trim());
				int amount = Integer.parseInt(command[3].trim());
				if (command[0].trim().equals("request")) {
					if (!cg.request(p, r, amount)) {
						System.out.println("The request is rejected.");
					}
					else System.out.println("The request is granted.");
				}
				else if (command[0].trim().equals("release")) {
					cg.release(p, r, amount);
				}
				else System.out.println("Wrong command format.");
			}
			else System.out.println("Wrong command format.");
			System.out.println("System current state is: \n" + cg);
		}
		System.out.println("The system is deadlocked.");
	}
}