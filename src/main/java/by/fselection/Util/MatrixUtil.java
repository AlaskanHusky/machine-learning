package by.fselection.Util;

import java.util.Arrays;

public class MatrixUtil {

    public MatrixUtil() {
    }

    public float[][] matrixNormalization(float[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        float[][] newMatrix = new float[rows][columns];

        for (int i = 0; i < rows; i++) {
            float min = getMinValue(matrix[i]);
            float max = getMaxValue(matrix[i]);
            for (int j = 0; j < columns; j++) {
                newMatrix[i][j] = (matrix[i][j] - min) / (max - min);
            }
        }

        return newMatrix;
    }

    public float[][] concatMatrixByColumns(float[][] firstMatrix, float[][] secondMatrix) {
        int newLength = firstMatrix[0].length + secondMatrix[0].length;
        int rowsNumber = firstMatrix.length;
        float[][] newMatrix = new float[rowsNumber][newLength];

        for (int i = 0; i < rowsNumber; i++) {
            float[] resultArray = new float[newLength];
            System.arraycopy(firstMatrix[i], 0, resultArray, 0, firstMatrix[i].length);
            System.arraycopy(secondMatrix[i], 0, resultArray, firstMatrix[i].length, secondMatrix[i].length);
            newMatrix[i] = resultArray;
        }

        return newMatrix;
    }

    public float[][] splitMatrix(float[][] matrix, int startIndex, int endIndex) {
        int rowsNumber = matrix.length;
        int columnsNumber = endIndex - startIndex;
        float[][] newMatrix = new float[rowsNumber][columnsNumber];

        for (int i = 0; i < rowsNumber; i++) {
            newMatrix[i] = Arrays.copyOfRange(matrix[i], startIndex, endIndex);
        }

        return newMatrix;
    }

    public static float getFeaturesArea(float[] firstFeature, float[] secondFeature) {
        return getFeatureRange(firstFeature) * getFeatureRange(secondFeature);
    }

    public static float getFeatureRange(float[] featureValues) {
        return getMaxValue(featureValues) - getMinValue(featureValues);
    }

    public static float getMinValue(float[] row) {
        float min = row[0];

        for (float el : row) {
            if (el < min) {
                min = el;
            }
        }

        return min;
    }

    public static float getMaxValue(float[] row) {
        float max = row[0];

        for (float el : row) {
            if (el > max) {
                max = el;
            }
        }

        return max;
    }
}
