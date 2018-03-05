package by.fselection.Util;

import java.util.Arrays;

/**
 * Класс для работы с матрицами
 */
public class MatrixUtil {

    public MatrixUtil() {
    }

    /**
     * Приводит все значения матрицы к
     * одному виду (нормализует её).
     *
     * @param   matrix  входная матрица
     * @return          нормализованная матрица
     */
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

    /**
     * Объеденяет две матрицы в оду по столбцам.
     *
     * @param   firstMatrix   первая матрица
     * @param   secondMatrix  вторая матрица
     * @return                объединённая матрица
     */
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

    /**
     * Выделяет из матрицы подматрицу, обозначенную
     * стартовым и конечным индексами первоначальной матрицы.
     *
     * @param   matrix      матрица значений
     * @param   startIndex  начальный индекс строки
     * @param   endIndex    конечный индекс строки
     * @return              подматрицу
     */
    public float[][] splitMatrix(float[][] matrix, int startIndex, int endIndex) {
        int rowsNumber = matrix.length;
        int columnsNumber = endIndex - startIndex;
        float[][] newMatrix = new float[rowsNumber][columnsNumber];

        for (int i = 0; i < rowsNumber; i++) {
            newMatrix[i] = Arrays.copyOfRange(matrix[i], startIndex, endIndex);
        }

        return newMatrix;
    }

    /**
     * Возвращает пространство решенийдля данных
     * характеристик определённого класса объектов.
     *
     * @param   firstFeature   массив значений первой характеристики
     * @param   secondFeature  массив значений второй характеристики
     * @return                 площадь покрытия значениями
     */
    public static float getFeaturesArea(float[] firstFeature, float[] secondFeature) {
        return getFeatureRange(firstFeature) * getFeatureRange(secondFeature);
    }

    /**
     * Находит длину вектора характеристики.
     *
     * @param   featureValues  значения характеристики
     * @return                 длина вектора
     */
    public static float getFeatureRange(float[] featureValues) {
        return getMaxValue(featureValues) - getMinValue(featureValues);
    }

    /**
     * Находит минимальный элемент массива.
     *
     * @param   row  одномерный массив
     * @return       минимальный элемент массива
     */
    public static float getMinValue(float[] row) {
        float min = row[0];

        for (float el : row) {
            if (el < min) {
                min = el;
            }
        }

        return min;
    }

    /**
     * Находит максимальный элемент массива.
     *
     * @param   row  одномерный массив
     * @return       максимальный элемент массива
     */
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
