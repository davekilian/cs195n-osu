
#include "matstack.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct _mstackelem
{
	Matrix *mat;
	struct _mstackelem *next;
} mstackelem;

typedef struct
{
	mstackelem *top;
	int depth;
} mstack;

mstackelem *mstackelem_create()
{
	mstackelem *e = (mstackelem*)malloc(sizeof(mstackelem));
	e->mat = (Matrix*)malloc(sizeof(Matrix));
	e->next = 0;
	return e;
}

void mstackelem_destroy(mstackelem *e)
{
	free(e->mat);
	free(e);
}

MatrixStack *matrix_stack_create()
{
	mstack *m = (mstack*)malloc(sizeof(mstack));

	m->top = mstackelem_create();
	matrix_identity(m->top->mat);

	m->depth = 1;

	return (MatrixStack*)m;
}

void matrix_stack_destroy(MatrixStack* ms)
{
	mstack *m = (mstack*)ms;

	while (m->depth > 1)
		matrix_stack_pop(ms);
	mstackelem_destroy(m->top);

	free(m);
}

void matrix_stack_push(MatrixStack* ms)
{
	mstack *m;
	mstackelem *e;

	m = (mstack*)ms;

	e = mstackelem_create();
	matrix_copy(e->mat, m->top->mat);

	e->next = m->top;
	m->top = e;
	++m->depth;
}

void matrix_stack_pop(MatrixStack* ms)
{
	mstack *m;
	mstackelem *tmp;

	m = (mstack*)ms;

	if (m->depth > 1)
	{
		tmp = m->top;
		m->top = m->top->next;
		mstackelem_destroy(tmp);
		--m->depth;
	}
}

float *matrix_stack_data_ptr(MatrixStack* ms)
{
	return ((mstack*)ms)->top->mat->data;
}

Matrix *matrix_stack_matrix_ptr(MatrixStack* ms)
{
	return ((mstack*)ms)->top->mat;
}

void matrix_stack_get_elems(MatrixStack* ms, float *e)
{
	memcpy(e, matrix_stack_data_ptr(ms), 16 * sizeof(float));
}

void matrix_stack_set_elems(MatrixStack* ms, float *e)
{
	memcpy(matrix_stack_data_ptr(ms), e, 16 * sizeof(float));
}

void matrix_stack_get(MatrixStack* ms, Matrix *m)
{
	matrix_copy(m, matrix_stack_matrix_ptr(ms));
}

void matrix_stack_set(MatrixStack* ms, Matrix *m)
{
	matrix_copy(matrix_stack_matrix_ptr(ms), m);
}

void matrix_stack_reset(MatrixStack* ms)
{
	mstack *m = (mstack*)ms;

	while (m->depth > 1)
		matrix_stack_pop(ms);

	matrix_identity(m->top->mat);
}
