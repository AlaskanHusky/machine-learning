package by.fselection;

import by.fselection.Util.FileReaderUtil;
import by.fselection.Util.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для простого отбора информативных признаков
 * объектов двух классов.
 */
class SimpleFeatureSelection {
    private MatrixUtil matrixUtil;
    private FileReaderUtil fileReader;
    private List<String> featuresList;
    private float[][] healthyMatrix;
    private float[][] sickMatrix;

    /**
     * Конструктор класса, который считывает признаки
     * и их значения из файла и заполняет этими данными
     * поля класса.
     */
    SimpleFeatureSelection() {
        matrixUtil = new MatrixUtil();
        fileReader = new FileReaderUtil();
        featuresList = fileReader.getFeatures();
        healthyMatrix = matrixUtil.splitMatrix(getNormalizationMatrix(), 0, 26);
        sickMatrix = matrixUtil.splitMatrix(getNormalizationMatrix(), 26, 54);
    }

    /**
     * По формуле находит информативные признаки из списка.
     * Если пространство решений каждого класса намного
     * больше пространства пересечения, то признак
     * достаточно информативен.
     *
     * @param   features  список признаков и их коэффициенты
     * @return            список информативных признаков
     */
    List<Element> getInformativeFeatures(List<Element> features) {
        List<Element> informativeFeatures = new ArrayList<>();

        for (int i = 0; i < features.size(); i++) {
            float healthyValuesRange = MatrixUtil.getFeatureRange(healthyMatrix[i]);
            float sickValuesRange = MatrixUtil.getFeatureRange(sickMatrix[i]);
            float newCoeff = features.get(i).getCoeff() * 2;
            if (healthyValuesRange > newCoeff && sickValuesRange > newCoeff) {
                informativeFeatures.add(features.get(i));
            }
        }

        return informativeFeatures;
    }

    /**
     * По формуле находит информативные пары признаков из списка.
     * Если пространство решений каждого класса намного
     * больше пространства пересечения, то признак
     * достаточно информативен.
     *
     * @param   pairFeatures  список пар признаков и их коэффициенты
     * @return                список информативных пар признаков
     */
    List<Element> getInformativePairs(List<Element> pairFeatures) {
        List<Element> informPair = new ArrayList<>();

        for (Element el : pairFeatures) {
            int firstFeatureIndex = featuresList.indexOf(el.getFirstFeature());
            int secondFeatureIndex = featuresList.indexOf(el.getSecondFeature());
            float healthyArea = MatrixUtil.getFeaturesArea(healthyMatrix[firstFeatureIndex], healthyMatrix[secondFeatureIndex]);
            float sickArea = MatrixUtil.getFeaturesArea(sickMatrix[firstFeatureIndex], sickMatrix[secondFeatureIndex]);
            float newCoeff = el.getCoeff() * 2;
            if (healthyArea > newCoeff && sickArea > newCoeff) {
                informPair.add(el);
            }
        }

        return informPair;
    }

    /**
     * По формуле находит коэффициенты признаков.
     * Коэффициент указывает на сколько сильно пересекаются
     * пространства решений здоровых и больных для одного
     * признака. Чем меньше это значение, тем информативнее
     * признак.
     *
     * @return  список признаков и их коэффициентов
     */
    List<Element> calculateFeaturesCoefficients() {
        List<Element> coeffs = new ArrayList<>();

        for (int i = 0; i < featuresList.size(); i++) {
            float healthyMax = MatrixUtil.getMaxValue(healthyMatrix[i]);
            float sickMin = MatrixUtil.getMinValue(sickMatrix[i]);
            float coeff = healthyMax - sickMin;
            Element el = new Element();
            el.setFirstFeature(featuresList.get(i));
            el.setCoeff(coeff);
            coeffs.add(el);
        }

        return coeffs;
    }

    /**
     * По формуле находит коэффициенты парных признаков.
     * Коэффициент указывает на сколько сильно пересекаются
     * пространства решений здоровых и больных для двух
     * признаков. Чем меньше это значение, тем информативнее
     * признак.
     *
     * @return  список парных признаков и их коэффициентов
     */
    List<Element> calculatePairFeaturesCoefficients() {
        List<Element> pairFeatures = new ArrayList<>();

        for (int i = 0; i < featuresList.size(); i++) {
            for (int j = i + 1; j < featuresList.size(); j++) {
                Element el = new Element();
                float width = MatrixUtil.getMaxValue(healthyMatrix[i]) - MatrixUtil.getMinValue(sickMatrix[i]);
                float length = MatrixUtil.getMaxValue(healthyMatrix[j]) - MatrixUtil.getMinValue(sickMatrix[j]);
                float coeff = width * length;
                el.setFirstFeature(featuresList.get(i));
                el.setSecondFeature(featuresList.get(j));
                el.setCoeff(coeff);
                pairFeatures.add(el);
            }
        }

        return pairFeatures;
    }

    /**
     * Выводит в консоль признаки и их коэффициенты.
     *
     * @param  features  список прихнаков и их коэффициентов
     */
    void printFeatures(List<Element> features) {
        for (Element feature : features) {
            System.out.println(feature.getFirstFeature() + ": " + feature.getCoeff());
        }
    }

    /**
     * Выводит в консоль парные признаки и их коэффициенты.
     *
     * @param  pairFeatures  список парных прихнаков и их
     *                       коэффициентов
     */
    void printPairFeatures(List<Element> pairFeatures) {
        for (Element pairFeature : pairFeatures) {
            System.out.println(pairFeature.getFirstFeature() + " и " +
                    pairFeature.getSecondFeature() + ": " + pairFeature.getCoeff());
        }
    }

    /**
     * Считывает данные из файла, формирует матрицу и нормализует её.
     *
     * @return  нормализованная матрица
     */
    private float[][] getNormalizationMatrix() {
        float[][] healthyMatrix = fileReader.getHealthyFeaturesValues();
        float[][] sickMatrix = fileReader.getSickFeaturesValues();
        float[][] jointMatrix = matrixUtil.concatMatrixByColumns(healthyMatrix, sickMatrix);

        return matrixUtil.matrixNormalization(jointMatrix);
    }
}