#include <stdio.h>
#include <time.h>
/*
NOTE:
it has come to my attention that we are
allowed to write this assignment in Java.
Therefore, I will be abandoning this file
and beginning a Java implementation.
*/

/* Process Manager */

typedef void* PMTarget;
struct PM {
	const char* name;
	PMTarget (*NewPMTarget)();
	void (*Create)(PMTarget, unsigned int);
	void (*Delete)(PMTarget, unsigned int);
};

/* Dynamic Process Manager */
struct DynamicPCB {};
PMTarget DynamicNewPMTarget() {
	
};
void DynamicCreate(PMTarget array, unsigned int parentPCBIndex) {};
void DynamicDelete(PMTarget array, unsigned int pcbIndex) {};
const struct PM DynamicPM = {
	.name = "Dynamic Process Manager",
	.NewPMTarget = &DynamicNewPMTarget,
	.Create = &DynamicCreate,
	.Delete = &DynamicDelete,
};

/* Static Process Manager */

PMTarget StaticNewPMTarget() {};
void StaticCreate(PMTarget head, unsigned int parentPCBIndex) {};
void StaticDelete(PMTarget head, unsigned int pcbIndex) {};
const struct PM StaticPM = {
	.name = "Static Process Manager",
	.NewPMTarget = &StaticNewPMTarget,
	.Create = &StaticCreate,
	.Delete = &StaticDelete,
};

/* Performance Test */

double TestPM(struct PM processManager) {
	const unsigned int REPEAT_COUNT = 100;
	clock_t start, end;
	double timeTaken = 0;
	for (unsigned int j = 0; j < REPEAT_COUNT; j++) {
		start = clock();
		
		PMTarget target = processManager.NewPMTarget();
		processManager.Create(target, 0); // creates P1 under P0
		processManager.Create(target, 0); // creates P2 under P0
		processManager.Create(target, 2); // creates P3 under P2
		processManager.Create(target, 0); // creates P4 under P0
		processManager.Delete(target, 0); // deletes all processes

		end = clock();
		timeTaken += ((double)(end - start))/CLOCKS_PER_SEC;
	}
	const double averageTimePerTest = timeTaken/REPEAT_COUNT;
	return averageTimePerTest;
}

/* Program Entry */

int main() {
	const struct PM processManagers[] = {
		StaticPM,
		DynamicPM
	};
	// for every process manager
	for (unsigned int i = 0; i < 2; i++) {
		// print title
		printf(processManagers[i].name);
		printf("\n");
		// run test
		const double averageTimePerTest = TestPM(processManagers[i]);
		// print report
		printf("Avg secs per test: %f\n", averageTimePerTest);
		printf("\n");
	}
	printf("Press ENTER to quit.");
	getchar();
	return 0;
}