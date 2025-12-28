#pragma once
#include "libraries.h"

class Utils {
public:
	Utils() {};
	~Utils() {};

	static GLuint createShaderProgram(const char *vp, const char *fp);
	static GLuint createShaderProgram(const char *vp, const char *gp, const char *fp);
	static GLuint createShaderProgram(const char* vp, const char* tCS, const char* tES, const char *fp);
	static GLuint createShaderProgram(const char* vp, const char* tCS, const char* tES, const char *gp, const char *fp);

};