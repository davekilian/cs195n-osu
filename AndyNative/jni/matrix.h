
#ifndef MATRIX_H_
#define MATRIX_H_

typedef union _Matrix
{
	float data[16];

	struct
	{
		float m11, m12, m13, m14,
			  m21, m22, m23, m24,
			  m31, m32, m33, m34,
			  m41, m42, m43, m44;
	};
} Matrix;

void matrix_identity(Matrix *m);
void matrix_scale(Matrix *m, float sx, float sy, float sz);
void matrix_rotate3d(Matrix *m, float angle, float x, float y, float z);
void matrix_rotate2d(Matrix *m, float angle);
void matrix_translate(Matrix *m, float x, float y, float z);
void matrix_frustum(Matrix *m, float left, float right, float bottom, float top, float near, float far);
void matrix_perspective(Matrix *m, float fov, float aspect, float near, float far);
void matrix_ortho(Matrix *m, float left, float right, float bottom, float top, float near, float far);
void matrix_multiply(Matrix *m, Matrix *other);
void matrix_copy(Matrix *m, Matrix *other);
float matrix_get(Matrix *m, int row, int col);
void matrix_invert(Matrix *m);
void matrix_transpose(Matrix *m);

#endif /* MATRIX_H_ */
