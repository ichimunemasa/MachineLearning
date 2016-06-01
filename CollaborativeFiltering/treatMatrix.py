#-*- coding: utf-8 -*-


#行列の転置
def transformMatrix(matrix):
	result = {}

	for row in matrix:
		for column in matrix[row]:
			result.setdefault(column,{})
			result[column][row] = matrix[row][column]
	
	return result


#行列の掛け算
def multiplication(matrixFirst,matrixSecond):
	result = {}

	

	for row in matrixFirst:
		result[row] = {}

		for column in matrixSecond:
			elementValue = 0.0

			for K in matrixSecond[column]:
				elementValue += matrixFirst[row][K] * matrixSecond[column][K]

			result[row][column] = elementValue
	

	return result
			
