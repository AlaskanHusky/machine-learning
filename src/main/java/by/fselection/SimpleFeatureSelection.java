package by.fselection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SimpleFeatureSelection {

    private final static String FILE_NAME = "src/main/resources/data.xlsx";
    private final static String HEALTHY_SHEET_NAME = "Здоровые";
    private final static String SICK_SHEET_NAME = "Больные";
    private final static String FEATURES_SHEET_NAME = "Признаки";
    private final static int HEALTHY_PEOPLE_NUMBER = 26;
    private final static int SICK_PEOPLE_NUMBER = 28;
    private final static int FEATURES_NUMBER = 14;

    SimpleFeatureSelection() {
    }

    Map<String, Float> featureEvaluation() {
        // Текстовый список признаков
        List<String> features = getFeaturesList();
        /*
            Коэффициенты близости. Данная величина показывает степень совпадения значений одного класса с другим.
            Чем меньше значения, тем информативнее признак.
            То есть значение 0.2 говорит о том, что этот признак информативнее, чем признак со значением 0.6.
        */
        List<Float> coeffs = getCoefficients();

        return convertToMap(features, coeffs);
    }

    List<Integer> getFeaturesIndexes() {
        List<Integer> indexes = new ArrayList<>();
        List<Float> coeffs = getCoefficients();
        float[][] healthyMatrix = getHealthyNormalizedMatrix();
        float[][] sickMatrix = getSickNormalizedMatrix();

        for (int i = 0; i < coeffs.size(); i++) {
            float healthyValuesRange = getMaxValue(healthyMatrix[i]) - getMinValue(healthyMatrix[i]);
            float sickValuesRange = getMaxValue(sickMatrix[i]) - getMinValue(sickMatrix[i]);
            float newCoeff = coeffs.get(i) * 2;
            if (healthyValuesRange > newCoeff && sickValuesRange > newCoeff) {
                indexes.add(i);
            }
        }

        return indexes;
    }

    void printFeaturesAndValues(Map<String, Float> map) {
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    List<String> getFeaturesList() {
        FileReaderUtil fileReader = new FileReaderUtil();
        return fileReader.getIndicators(FILE_NAME, FEATURES_SHEET_NAME, FEATURES_NUMBER);
    }

    private List<Float> getCoefficients() {
        // Нормализованная матрица здоровых людей
        float[][] healthyNormalizedMatrix = getHealthyNormalizedMatrix();
        // Нормализованная матрица больных людей
        float[][] sickNormalizedMatrix = getSickNormalizedMatrix();

        return calculateCompactnessCoefficients(healthyNormalizedMatrix, sickNormalizedMatrix, FEATURES_NUMBER);
    }

    private List<Float> calculateCompactnessCoefficients(float[][] healthyMatrix, float[][] sickMatrix, int featuresNumber) {
        List<Float> coeffs = new ArrayList<>();

        for (int i = 0; i < featuresNumber; i++) {
            float healthyMax = getMaxValue(healthyMatrix[i]);
            float sickMin = getMinValue(sickMatrix[i]);
            Float coeff = healthyMax - sickMin;
            coeffs.add(coeff);
        }

        return coeffs;
    }

    private float[][] getNormalizationMatrix() {
        FileReaderUtil fileReader = new FileReaderUtil();

        // Матрица признаков и их значений здоровых людей
        float[][] healthyMatrix = fileReader.getIndicatorsValues(FILE_NAME, HEALTHY_SHEET_NAME, HEALTHY_PEOPLE_NUMBER);
        // Матрица признаков и их значений больных людей
        float[][] sickMatrix = fileReader.getIndicatorsValues(FILE_NAME, SICK_SHEET_NAME, SICK_PEOPLE_NUMBER);
        // Объединенная матрица здоровых и больных людей
        float[][] jointMatrix = concatMatrixByColumns(healthyMatrix, sickMatrix);

        return matrixNormalization(jointMatrix);
    }

    private float[][] getHealthyNormalizedMatrix() {
        return  splitMatrix(getNormalizationMatrix(), HEALTHY_PEOPLE_NUMBER, 0, 26);
    }

    private float[][] getSickNormalizedMatrix() {
        return  splitMatrix(getNormalizationMatrix(), SICK_PEOPLE_NUMBER, 26, 54);
    }

    private float[][] splitMatrix(float[][] matrix, int columnsNumber, int startIndex, int endIndex) {
        int rowsNumber = matrix.length;
        float[][] newMatrix = new float[rowsNumber][columnsNumber];

        for (int i = 0; i < rowsNumber; i++) {
            newMatrix[i] = Arrays.copyOfRange(matrix[i], startIndex, endIndex);
        }

        return newMatrix;
    }

    private float[][] matrixNormalization(float[][] matrix) {
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

    private float[][] concatMatrixByColumns(float[][] firstMatrix, float[][] secondMatrix) {
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

    private Map<String, Float> convertToMap(List<String> indicators, List<Float> coeffs) {
        Map<String, Float> map = new HashMap<>();

        for (int i = 0; i < indicators.size(); i++) {
            map.put(indicators.get(i), coeffs.get(i));
        }

        return map;
    }

    private float getMinValue(float[] row) {
        float min = row[0];

        for (float el : row) {
            if (el < min) {
                min = el;
            }
        }

        return min;
    }

    private float getMaxValue(float[] row) {
        float max = row[0];

        for (float el : row) {
            if (el > max) {
                max = el;
            }
        }

        return max;
    }
}
