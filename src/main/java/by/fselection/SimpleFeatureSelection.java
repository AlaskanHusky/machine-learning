package by.fselection;

import by.fselection.Util.CollectionUtil;
import by.fselection.Util.FileReaderUtil;
import by.fselection.Util.MatrixUtil;

import java.util.*;

class SimpleFeatureSelection {
    private MatrixUtil matrixUtil = new MatrixUtil();
    private FileReaderUtil fileReader = new FileReaderUtil();

    SimpleFeatureSelection() {

    }

    Map<String, Float> featureEvaluation() {
        // Текстовый список признаков
        List<String> features = FileReaderUtil.getFeatures();
        /*
            Коэффициенты близости. Данная величина показывает степень совпадения значений одного класса с другим.
            Чем меньше значения, тем информативнее признак.
            То есть значение 0.2 говорит о том, что этот признак информативнее, чем признак со значением 0.6.
        */
        List<Float> coeffs = calculateFeaturesCoefficients();

        return CollectionUtil.convertToMap(features, coeffs);
    }

    List<Element> getInformativePairs(List<Element> pairFeatures) {
        float[][] healthyMatrix = getHealthyNormalizedMatrix();
        float[][] sickMatrix = getSickNormalizedMatrix();
        List<String> features = FileReaderUtil.getFeatures();
        List<Element> informPair = new ArrayList<>();

        for (Element el : pairFeatures) {
            int firstFeatureIndex = features.indexOf(el.getFirstFeature());
            int secondFeatureIndex = features.indexOf(el.getSecondFeature());
            float healthyArea = MatrixUtil.getFeaturesArea(healthyMatrix[firstFeatureIndex], healthyMatrix[secondFeatureIndex]);
            float sickArea = MatrixUtil.getFeaturesArea(sickMatrix[firstFeatureIndex], sickMatrix[secondFeatureIndex]);
            float newCoeff = el.getCoeff() * 2;
            if (healthyArea > newCoeff && sickArea > newCoeff) {
                informPair.add(el);
            }
        }

        return informPair;
    }

    List<Element> calculatePairFeaturesCoefficients() {
        float[][] healthyMatrix = getHealthyNormalizedMatrix();
        float[][] sickMatrix = getSickNormalizedMatrix();
        List<String> features = FileReaderUtil.getFeatures();
        int rowsNumber = healthyMatrix.length;
        List<Element> pairFeatures = new ArrayList<>();

        for (int i = 0; i < rowsNumber; i++) {
            for (int j = i + 1; j < rowsNumber; j++) {
                Element el = new Element();
                float width = MatrixUtil.getMaxValue(healthyMatrix[i]) - MatrixUtil.getMinValue(sickMatrix[i]);
                float length = MatrixUtil.getMaxValue(healthyMatrix[j]) - MatrixUtil.getMinValue(sickMatrix[j]);
                float coeff = width * length;
                el.setFirstFeature(features.get(i));
                el.setSecondFeature(features.get(j));
                el.setCoeff(coeff);
                pairFeatures.add(el);
            }
        }

        return pairFeatures;
    }

    List<Integer> getFeaturesIndexes() {
        List<Integer> indexes = new ArrayList<>();
        List<Float> coeffs = calculateFeaturesCoefficients();
        float[][] healthyMatrix = getHealthyNormalizedMatrix();
        float[][] sickMatrix = getSickNormalizedMatrix();

        for (int i = 0; i < coeffs.size(); i++) {
            float healthyValuesRange = MatrixUtil.getMaxValue(healthyMatrix[i]) - MatrixUtil.getMinValue(healthyMatrix[i]);
            float sickValuesRange = MatrixUtil.getMaxValue(sickMatrix[i]) - MatrixUtil.getMinValue(sickMatrix[i]);
            float newCoeff = coeffs.get(i) * 2;
            if (healthyValuesRange > newCoeff && sickValuesRange > newCoeff) {
                indexes.add(i);
            }
        }

        return indexes;
    }

    void printFeatures(Map<String, Float> map) {
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    void printPairFeatures(List<Element> pairFeatures) {
        for (Element pairFeature : pairFeatures) {
            System.out.println(pairFeature.getFirstFeature() + " и " +
                    pairFeature.getSecondFeature() + ": " + pairFeature.getCoeff());
        }
    }

    private List<Float> calculateFeaturesCoefficients() {
        float[][] healthyMatrix = getHealthyNormalizedMatrix();
        float[][] sickMatrix = getSickNormalizedMatrix();
        int featuresNumber = healthyMatrix.length;
        List<Float> coeffs = new ArrayList<>();

        for (int i = 0; i < featuresNumber; i++) {
            float healthyMax = MatrixUtil.getMaxValue(healthyMatrix[i]);
            float sickMin = MatrixUtil.getMinValue(sickMatrix[i]);
            Float coeff = healthyMax - sickMin;
            coeffs.add(coeff);
        }

        return coeffs;
    }

    private float[][] getHealthyNormalizedMatrix() {
        return matrixUtil.splitMatrix(getNormalizationMatrix(), 0, 26);
    }

    private float[][] getSickNormalizedMatrix() {
        return matrixUtil.splitMatrix(getNormalizationMatrix(), 26, 54);
    }

    private float[][] getNormalizationMatrix() {
        float[][] healthyMatrix = fileReader.getHealthyFeaturesValues();
        float[][] sickMatrix = fileReader.getSickFeaturesValues();
        float[][] jointMatrix = matrixUtil.concatMatrixByColumns(healthyMatrix, sickMatrix);

        return matrixUtil.matrixNormalization(jointMatrix);
    }
}