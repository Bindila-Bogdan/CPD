#include <iostream>
#include <omp.h>
using namespace std;

#define THREAD_NUM 4

int testFunction() {
	omp_set_num_threads(THREAD_NUM);
	double startTime = omp_get_wtime();

#pragma omp for
	for (int i = 0; i < 1000; i++)
		cout << "Thread: " << omp_get_thread_num() << endl;

	double endTime = omp_get_wtime();
	cout << "Required time: " << endTime - startTime << " s" << endl;

	return 0;
}


void matrix_mult_alg(int size, int* a, int* b, int* ab) {
    int m, k, n;
    int temp;

    #pragma omp for
    for(m = 0; m < size; m++) {
        for (n = 0; n < size; n++) {
            temp = 0.0;
            for (k = 0; k < size; k++) {
                temp += *(a + (m * size + k)) * *(b + (k * size + n));
            }
            *(ab + (m * size + n)) = temp;
        }
    }
}

void matrix_mul() {
    omp_set_num_threads(THREAD_NUM);
    omp_set_nested(1);

    const int size = 10;
    int k, n, m;

    printf("\A:\n");
    int matrix_a[size * size];
    for (m = 0; m < size; m++) {
        for (k = 0; k < size; k++) {
            matrix_a[m * size + k] = m * k + 1;
            printf("%d\t", matrix_a[m * size + k]);
        }
        printf("\n");
    }

    printf("\nB:\n");
    int matrix_b[size * size];
    for (k = 0; k < size; k++) {
        for (n = 0; n < size; n++) {
            matrix_b[k * size + n] = k * n + 1;
            printf("%d\t", matrix_b[k * size + n]);
        }
        printf("\n");
    }

    printf("\nA*B:\n");
    int matrix_result[size * size];


    double startTime = omp_get_wtime();
    matrix_mult_alg(size, matrix_a, matrix_b, matrix_result);
    double endTime = omp_get_wtime();
    cout << "Required time for multiplication: " << endTime - startTime << " s" << endl;

    for (m = 0; m < size; m++) {
        for (n = 0; n < size; n++) {
            printf("%d\t", matrix_result[m * size + n]);
        }
        printf("\n");
    }
}

int main()
{
    matrix_mul();
	return 0;
}
