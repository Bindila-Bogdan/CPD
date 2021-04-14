#include "mpi.h"
#include <stdio.h>
#include <string.h>
#include <iostream>
using namespace std;

#define SIZE 2048
#define MAX_ELEMENT 10
#define DISPLAY_MATRICES 0
#define DISPLAY_MESSAGES 1

// functie pentru generarea matricilor
int* generateMatrix(char name) {
	if (name == 'A')
		srand(time(NULL) + 10);
	else
		srand(time(NULL));

	std::printf("Generating matrix %c...\n", name);

	int* matrix = (int*)malloc(SIZE * SIZE * sizeof(int));
	for (int i = 0; i < SIZE * SIZE; i++)
		matrix[i] = (rand() % MAX_ELEMENT);

	return matrix;
}

// functie pentru afisarea matricilor
void displayMatrix(int* matrix, char name) {
	if (DISPLAY_MATRICES == 1) {
		std::printf("\n%c:\n", name);

		for (int i = 0; i < SIZE * SIZE; i++) {
			std::printf("%2d ", matrix[i]);

			if ((i + 1) % SIZE == 0)
				std::printf("\n");
		}
	}
}

void main(int argc, char* argv[]){
	MPI_Status status;
	MPI_Request request;
	int processesNumber, rank, blockDim, iBlock, jBlock, numberOfBlocks, sum, abIndex;
	int* A, * B, * AB, * blockA, * blockB, * blocksIds, * blockRecv, * C;
	double startTime, finishTime;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &processesNumber);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0) {
		startTime = MPI_Wtime();
		std::printf("Total number of processes is %d.\n", processesNumber);
		std::printf("Size of matrices is: %d x %d.\n", SIZE, SIZE);
		
		if (DISPLAY_MESSAGES)
			std::printf("Process %d has been created.\n", rank);

		// verificam daca matrimea matricei este divizibila cu radical din numarul de procese
		if (SIZE % int(sqrt(processesNumber))) {
			std::printf("Number of processes must be a multiple of matrix size.\n");
			MPI_Finalize();
			return;
		}
		// verificam daca numarul de procese este un patrat perfect
		else if (pow(int(sqrt(processesNumber)), 2) != processesNumber){
			std::printf("Number of processes must be a squared number.\n");
			MPI_Finalize();
			return;
		}

		// calculam dimensiunea laturii unui bloc
		blockDim = SIZE / int(sqrt(processesNumber));

		A = generateMatrix('A');
		B = generateMatrix('B');

		displayMatrix(A, 'A');
		displayMatrix(B, 'B');

		// calculam indexul blocului pentru procesul cu rank = 0
		numberOfBlocks = SIZE / blockDim;
		iBlock = rank / numberOfBlocks;
		jBlock = rank % numberOfBlocks;

		// alocam blocurile A si B pentru procesul cu rank = 0
		blockA = (int*)malloc(blockDim * blockDim * sizeof(int));
		memcpy(blockA, A, blockDim * blockDim * sizeof(int));

		blockB = (int*)malloc(blockDim * blockDim * sizeof(int));
		memcpy(blockB, B, blockDim * blockDim * sizeof(int));

		AB = (int*)calloc(blockDim * blockDim, sizeof(int));

		// trimitem blocurile A si B pentru celalalte procese
		MPI_Bcast(&blockDim, 1, MPI_INT, 0, MPI_COMM_WORLD);
		for (int processNumber = 1; processNumber < processesNumber; processNumber++) {
			blockA = (int*)malloc(blockDim * blockDim * sizeof(int));
			memcpy(blockA, A + blockDim * blockDim * processNumber, blockDim * blockDim * sizeof(int));
			MPI_Send(blockA, blockDim * blockDim, MPI_INT, processNumber, 0, MPI_COMM_WORLD);

			blockB = (int*)malloc(blockDim * blockDim * sizeof(int));
			memcpy(blockB, B + blockDim * blockDim * processNumber, blockDim * blockDim * sizeof(int));
			MPI_Send(blockB, blockDim * blockDim, MPI_INT, processNumber, 0, MPI_COMM_WORLD);
		}
	}else {
		// primim marimea laturii unui bloc
		MPI_Bcast(&blockDim, 1, MPI_INT, 0, MPI_COMM_WORLD);

		// calculam radical din numarul total de blocuri si indecsii blocului pe baza rankului
		numberOfBlocks = SIZE / blockDim;
		iBlock = rank / numberOfBlocks;
		jBlock = rank % numberOfBlocks;

		// primim blocurile A si B
		blockA = (int*)malloc(blockDim * blockDim * sizeof(int));
		MPI_Recv(blockA, blockDim * blockDim, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

		blockB = (int*)malloc(blockDim * blockDim * sizeof(int));
		MPI_Recv(blockB, blockDim * blockDim, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

		AB = (int*)calloc(blockDim * blockDim, sizeof(int));
	}

	int sendingRank = 0;

	// repetam pasii algoritmului de un numar de ori egal cu radical din numarul de procese
	for (int iter = 0; iter < int(sqrt(processesNumber)); iter++) {
			// verificam daca procesul curent trebuie sa trimita blocul A
			if (jBlock == (iBlock * numberOfBlocks + jBlock + iter) % numberOfBlocks) {
				sendingRank = rank;
				// procesul trimite tuturor celorlalte procese de pe rand
				for (int j = 0; j < numberOfBlocks; j++) {
					if (j != jBlock) {
						// trimiterea blocului se face in mod non-blocant
						MPI_Isend(blockA, blockDim * blockDim, MPI_INT, numberOfBlocks * iBlock + j, 0, MPI_COMM_WORLD, &request);
					}
				}
			}

		// sincronizam procesele pentru ca in acest pas ca fie deja trimise blocurile A pe fiecare rand
		MPI_Barrier(MPI_COMM_WORLD);

		// citim in mod non-blocant blocul primit de procesul care a trimis anterior pe randul curent
		blockRecv = (int*)malloc(blockDim * blockDim * sizeof(int));
		MPI_Irecv(blockRecv, blockDim * blockDim, MPI_INT, sendingRank, 0, MPI_COMM_WORLD, &request);
		// inmultim matricile si acumulam rezultatul in matricea AB
		abIndex = 0;
		for (int iAB = 0; iAB < blockDim; iAB++)
			for (int jAB = 0; jAB < blockDim; jAB++) {
				sum = 0;
				for (int k = 0; k < blockDim; k++)
					sum += blockA[iAB * blockDim + k] * blockB[jAB + blockDim * k];
				AB[abIndex++] += sum;
			}

		// calculam rank-ul procesului care trebuie sa primeasca blocul B curent
		int sendRank;
		if (rank - numberOfBlocks >= 0)
			sendRank = rank - numberOfBlocks;
		else
			sendRank = numberOfBlocks * numberOfBlocks + rank - numberOfBlocks;

		// trimitem blocul B curent in mod non-blocant
		MPI_Isend(blockB, blockDim * blockDim, MPI_INT, sendRank, 0, MPI_COMM_WORLD, &request);

		// primim blocul B in mod non-blocant
		MPI_Irecv(blockB, blockDim * blockDim, MPI_INT, (rank + numberOfBlocks) % (numberOfBlocks * numberOfBlocks), 0, MPI_COMM_WORLD, &request);
	}

	// fiecare proces non-root trimite blocul calculat
	if (rank != 0)
		MPI_Send(AB, blockDim * blockDim, MPI_INT, 0, rank, MPI_COMM_WORLD);
	else {
		// in procesul root reconstruim matricea C din blocurile calculate de catre procese
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
		// masuram timpul, afisam matricea iar apoi timpul total de executie
		finishTime = MPI_Wtime();
		displayMatrix(C, 'C');
		std::printf("Required time: %lf", finishTime - startTime);
	}

	MPI_Finalize();
}
