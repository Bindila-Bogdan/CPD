#include "mpi.h"
#include <stdio.h>
#include <string.h>
#include <iostream>
using namespace std;

#define SIZE 3072
#define MAX_ELEMENT 10
#define DISPLAY_MATRICES 0
#define DISPLAY_MESSAGES 0

// functie pentru generarea matricilor
int* generateMatrix(char name) {
	if (name == 'A')
		srand(time(NULL) + 10);
	else
		srand(time(NULL));

	printf("Generating matrix %c...\n", name);

	int* matrix = (int*)malloc(SIZE * SIZE * sizeof(int));
	for (int i = 0; i < SIZE * SIZE; i++)
		matrix[i] = (rand() % MAX_ELEMENT);

	return matrix;
}

// functie pentru afisarea matricilor
void displayMatrix(int* matrix, char name) {
	if (DISPLAY_MATRICES == 1) {
		printf("\n%c:\n", name);

		for (int i = 0; i < SIZE * SIZE; i++) {
			printf("%2d ", matrix[i]);

			if ((i + 1) % SIZE == 0)
				printf("\n");
		}
	}
}

void main(int argc, char* argv[]){
	MPI_Status status;
	int processesNumber, rank, blockDim;
	int* A, * B, * C, * AB, * blocksIds;
	double startTime, finishTime;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &processesNumber);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0) {
		startTime = MPI_Wtime();
		printf("Total number of processes is %d.\n", processesNumber);
		printf("Size of matrices is: %d x %d.\n", SIZE, SIZE);
		
		if (DISPLAY_MESSAGES)
			printf("Process %d has been created.\n", rank);

		// verificam daca matrimea matricei este divizibila cu radical din numarul de procese
		if (SIZE % int(sqrt(processesNumber))) {
			printf("Number of processes must be a multiple of matrix size.\n");
			MPI_Finalize();
			return;
		}
		// verificam daca numarul de procese este un patrat perfect
		else if (pow(int(sqrt(processesNumber)), 2) != processesNumber){
			printf("Number of processes must be a squared number.\n");
			MPI_Finalize();
			return;
		}

		// calculam dimensiunea laturii unui bloc
		blockDim = SIZE / int(sqrt(processesNumber));

		A = generateMatrix('A');
		B = generateMatrix('B');

		displayMatrix(A, 'A');
		displayMatrix(B, 'B');

		int k = 0;

		// calculam intr-un sir care sunt id-urile blocurilor pe care fiecare proces trebuie sa le ia din matricile A si B
		blocksIds = (int*)malloc(2 * int(sqrt(processesNumber)) * int(sqrt(processesNumber)) * sizeof(int));
		for (int i = 0; i < int(sqrt(processesNumber)); i++)
			for (int j = 0; j < int(sqrt(processesNumber)); j++) {
				blocksIds[k++] = i;
				blocksIds[k++] = j;
			}

		MPI_Bcast(&blockDim, 1, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(A, SIZE * SIZE, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(B, SIZE * SIZE, MPI_INT, 0, MPI_COMM_WORLD);
		MPI_Bcast(blocksIds, 2 * int(sqrt(processesNumber)) * int(sqrt(processesNumber)), MPI_INT, 0, MPI_COMM_WORLD);
	}	
	else {
		if (DISPLAY_MESSAGES)
			printf("Process %d has been created.\n", rank);

		// procesul ia din root dimensiunea laturii unui bloc
		MPI_Bcast(&blockDim, 1, MPI_INT, 0, MPI_COMM_WORLD);
		
		if (DISPLAY_MESSAGES)
			printf("Process %d has received: %d\n", rank, blockDim);

		// procesul acceseaza matricile A si B din root
		A = (int*)malloc(SIZE * SIZE * sizeof(int));
		MPI_Bcast(A, SIZE * SIZE, MPI_INT, 0, MPI_COMM_WORLD);

		B = (int*)malloc(SIZE * SIZE * sizeof(int));
		MPI_Bcast(B, SIZE * SIZE, MPI_INT, 0, MPI_COMM_WORLD);

		// in procesul curent se stocheaza din root sirul de indici
		blocksIds = (int*)malloc(2 * int(sqrt(processesNumber)) * int(sqrt(processesNumber)) * sizeof(int));
		MPI_Bcast(blocksIds, 2 * int(sqrt(processesNumber)) * int(sqrt(processesNumber)), MPI_INT, 0, MPI_COMM_WORLD);
	}

	// fiecare proces inclusiv cel root extrage blocul din A si pe cel din B pe baza indicilor din blocksIds
	int *sliceA = (int*)malloc(blockDim * SIZE * sizeof(int));
	int *sliceB = (int*)malloc(blockDim * SIZE * sizeof(int));
	memcpy(sliceA, A + blockDim * SIZE * blocksIds[rank * 2], blockDim * SIZE * sizeof(int));
	
	int k = 0;

	for (int j = 0; j < blockDim; j++)
		for (int i = 0; i < SIZE; i++) 
			sliceB[k++] = B[blockDim * blocksIds[rank * 2 + 1] + j + i * SIZE];

	// in fiecare proces se inmulteste randul de blocuri din A cu o coloana corespondenta de blocuri din B
	int sum;
	AB = (int*)calloc(blockDim * blockDim, sizeof(int));

	for (int iAB = 0; iAB < blockDim; iAB++)
		for (int jAB = 0; jAB < blockDim; jAB++) {
			sum = 0;
			for (int i = 0; i < SIZE; i++) 
				sum += sliceA[iAB * SIZE + i] * sliceB[SIZE * jAB + i];

			AB[iAB * blockDim + jAB] = sum;
		}

	// fiecare process non-root trimite blocul calculat procesului root
	if(rank != 0)
		MPI_Send(AB, blockDim * blockDim, MPI_INT, 0, rank, MPI_COMM_WORLD);
	else {
		// procesul root primeste blocurile de la celalalte procese si construieste matricea rezultat
		C = (int*)calloc(SIZE * SIZE, sizeof(int));

		int k = 0, nextLine = 0;
		for (int i = 0; i < blockDim; i++)
			for (int j = 0; j < blockDim; j++)
				C[i * SIZE + j] = AB[k++];

		for (int processNumber = 1; processNumber < processesNumber; processNumber++) {
			if (processNumber % int(sqrt(processesNumber)) == 0)
				nextLine++;

			MPI_Recv(AB, blockDim * blockDim, MPI_INT, processNumber, processNumber, MPI_COMM_WORLD, &status);
			k = 0;

			for (int i = 0; i < blockDim; i++) 
				for (int j = 0; j < blockDim; j++)
					C[(processNumber % (int)sqrt(processesNumber)) * blockDim + nextLine * SIZE * blockDim + i * SIZE + j] = AB[k++];
		}	
			// masuram timpul curent, afisam matricea rezultata si timpul necesar executiei programului
			finishTime = MPI_Wtime();
			displayMatrix(C, 'C');
			printf("Required time: %lf", finishTime - startTime);
	}

	MPI_Finalize();
}
