
#include "matrix.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// Because I'm not qualified to be writing reliable matrix math at this hour, this is based mostly off of:
// https://cvs.khronos.org/svn/repos/registry/trunk/public/webgl/sdk/demos/google/resources/moz/matrix4x4.js

void matrix_identity(Matrix *m)
{
	int i, j;

	for (i = 0; i < 4; ++i)
		for (j = 0; j < 4; ++j)
			if (i == j)
				m->data[4 * i + j] = 1;
			else
				m->data[4 * i + j] = 0;
}

void matrix_scale(Matrix *m, float sx, float sy, float sz)
{
	int i;

	for (i = 0; i < 4; ++i)
	{
		m->data[    i] *= sx;
		m->data[4 + i] *= sy;
		m->data[8 + i] *= sz;
	}
}

void matrix_rotate3d(Matrix *m, float angle, float x, float y, float z)
{
	float mag, sinAngle, cosAngle;

	mag = sqrt(x*x + y*y + z*z);
	sinAngle = sin(angle * M_PI / 180.f);
	cosAngle = cos(angle * M_PI / 180.f);

	if (mag > 0)
	{
		float xx, yy, zz, xy, yz, zx, xs, ys, zs, oneMinusCos, oneOverMag;
		Matrix rot;

		oneOverMag = 1.f / mag;
		x *= oneOverMag;
		y *= oneOverMag;
		z *= oneOverMag;

		xx = x * x;
		yy = y * y;
		zz = z * z;
		xy = x * y;
		yz = y * z;
		zx = z * x;
		xs = x * sinAngle;
		ys = y * sinAngle;
		zs = z * sinAngle;
		oneMinusCos = 1.f - cosAngle;

		rot.data[0]  = (oneMinusCos * xx) + cosAngle;
		rot.data[1]  = (oneMinusCos * xy) - zs;
		rot.data[2]  = (oneMinusCos * zx) + ys;
		rot.data[3]  = 0.f;

		rot.data[4]  = (oneMinusCos * xy) + zs;
		rot.data[5]  = (oneMinusCos * yy) + cosAngle;
		rot.data[6]  = (oneMinusCos * yz) - xs;
		rot.data[7]  = 0.f;

		rot.data[8]  = (oneMinusCos * zx) - ys;
		rot.data[9]  = (oneMinusCos * yz) + xs;
		rot.data[10] = (oneMinusCos * zz) + cosAngle;
		rot.data[11] = 0.f;

		rot.data[12] = 0.f;
		rot.data[13] = 0.f;
		rot.data[14] = 0.f;
		rot.data[15] = 1.f;

		matrix_multiply(&rot, m);
		matrix_copy(m, &rot);
	}
}

void matrix_rotate2d(Matrix *m, float angle)
{
	float sinAngle, cosAngle;
	Matrix rot;

	sinAngle = sin(angle * M_PI / 180.f);
	cosAngle = cos(angle * M_PI / 180.f);

	rot.data[0]  = cosAngle;
	rot.data[1]  = -sinAngle;
	rot.data[2]  = 0.f;
	rot.data[3]  = 0.f;

	rot.data[4]  = sinAngle;
	rot.data[5]  = cosAngle;
	rot.data[6]  = 0.f;
	rot.data[7]  = 0.f;

	rot.data[8]  = 0.f;
	rot.data[9]  = 0.f;
	rot.data[10] = 1.f;
	rot.data[11] = 0.f;

	rot.data[12] = 0.f;
	rot.data[13] = 0.f;
	rot.data[14] = 0.f;
	rot.data[15] = 1.f;

	matrix_multiply(&rot, m);
	matrix_copy(m, &rot);
}

void matrix_translate(Matrix *m, float x, float y, float z)
{
	m->data[12] += m->data[0] * x + m->data[4] * y + m->data[ 8] * z;
	m->data[13] += m->data[1] * x + m->data[5] * y + m->data[ 9] * z;
	m->data[14] += m->data[2] * x + m->data[6] * y + m->data[10] * z;
	m->data[15] += m->data[3] * x + m->data[7] * y + m->data[11] * z;
}

void matrix_frustum(Matrix *m, float left, float right, float bottom, float top, float near, float far)
{
	float dx, dy, dz;
	Matrix f;

	dx = right - left;
	dy = top - bottom;
	dz = far - near;

	if (near <= 0.f || far <= 0.f || dx <= 0.f || dy <= 0.f || dz <= 0.f)
		return;

	f.data[0]  = 2.f * near / dx;
	f.data[1]  = 0.f;
	f.data[2]  = 0.f;
	f.data[3]  = 0.f;

	f.data[4]  = 0.f;
	f.data[5]  = 2.f * near / dy;
	f.data[6]  = 0.f;
	f.data[7]  = 0.f;

	f.data[8]  = (right + left) / dx;
	f.data[9]  = (top + bottom) / dy;
	f.data[10] = -(near + far) / dz;
	f.data[11] = -1.f;

	f.data[12] = 0.f;
	f.data[13] = 0.f;
	f.data[14] = -2.f * near * far / dz;
	f.data[15] = 0.f;

	matrix_multiply(&f, m);
	matrix_copy(m, &f);
}

void matrix_perspective(Matrix *m, float fov, float aspect, float near, float far)
{
	float fh, fw;

	fh = tan(fov / 360.f * M_PI) * near;
	fw = fh * aspect;

	matrix_frustum(m, -fw, fw, -fh, fh, near, far);
}

void matrix_ortho(Matrix *m, float left, float right, float bottom, float top, float near, float far)
{
	float dx, dy, dz;
	Matrix o;

	dx = right - left;
	dy = top - bottom;
	dz = far - near;

	if (dx == 0.f || dy == 0.f || dz == 0.f)
		return;

	o.data[0]  = 2.f / dx;
	o.data[1]  = 0.f;
	o.data[2]  = 0.f;
	o.data[3]  = 0.f;

	o.data[4]  = 0.f;
	o.data[5]  = 2.f / dy;
	o.data[6]  = 0.f;
	o.data[7]  = 0.f;

	o.data[8]  = 0.f;
	o.data[9]  = 0.f;
	o.data[10] = -2.f / dz;
	o.data[11] = 0.f;

	o.data[12] = -(right + left) / dx;
	o.data[13] = -(top + bottom) / dy;
	o.data[14] = -(near + far) / dz;
	o.data[15] = 0.f;

	matrix_multiply(&o, m);
	matrix_copy(m, &o);
}

void matrix_multiply(Matrix *m, Matrix *other)
{
	Matrix tmp;
	int i, j;

	for (i = 0; i < 4; ++i)
		for (j = 0; j < 4; ++j)
			tmp.data[i * 4 + j] = m->data[i * 4 + 0] * other->data[0 * 4 + j] +
								  m->data[i * 4 + 1] * other->data[1 * 4 + j] +
								  m->data[i * 4 + 2] * other->data[2 * 4 + j] +
								  m->data[i * 4 + 3] * other->data[3 * 4 + j];

	matrix_copy(m, &tmp);
}

void matrix_copy(Matrix *m, Matrix *other)
{
	memcpy(m->data, other->data, 16 * sizeof(float));
}

float matrix_get(Matrix *m, int row, int col)
{
	return m->data[4 * row + col];
}

void matrix_invert(Matrix *m)
{
	// oh fuck no
}

void matrix_transpose(Matrix *m)
{
	int i, j;
	float tmp;

	for (i = 0; i < 4; ++i)
	{
		for (j = 0; j < i; ++j)
		{
			tmp = m->data[4 * i + j];
			m->data[4 * i + j] = m->data[4 * j + i];
			m->data[4 * j + i] = tmp;
		}
	}
}
