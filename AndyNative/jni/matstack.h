
#ifndef MATSTACK_H_
#define MATSTACK_H_

#include "matrix.h"

typedef void* MatrixStack;

MatrixStack *matrix_stack_create();
void matrix_stack_destroy(MatrixStack* ms);
void matrix_stack_push(MatrixStack* ms);
void matrix_stack_pop(MatrixStack* ms);
float *matrix_stack_data_ptr(MatrixStack* ms);
Matrix *matrix_stack_matrix_ptr(MatrixStack* ms);
void matrix_stack_get_elems(MatrixStack* ms, float *e);
void matrix_stack_set_elems(MatrixStack* ms, float *e);
void matrix_stack_get(MatrixStack* ms, Matrix *m);
void matrix_stack_set(MatrixStack* ms, Matrix *m);
void matrix_stack_reset(MatrixStack* ms);
int matrix_stack_depth(MatrixStack* ms);

#endif /* MATSTACK_H_ */
