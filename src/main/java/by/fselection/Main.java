package by.fselection;

import by.fselection.Util.CollectionUtil;
import by.fselection.Util.FileReaderUtil;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        SimpleFeatureSelection featureSelection = new SimpleFeatureSelection();
        Map<String, Float> featuresAndKeys = featureSelection.featureEvaluation();
        Map<String, Float> sortedFeatures = CollectionUtil.sortMap(featuresAndKeys);

        System.out.println("_____ Признаки и их коэффициенты (Отсортированные по убыванию информативности) _____");
        featureSelection.printFeatures(sortedFeatures);

        List<Integer> featuresIndexes = featureSelection.getFeaturesIndexes();
        List<String> features = FileReaderUtil.getFeatures();
        System.out.println("_____ Информативные признаки _____");

        for (int i = 0; i < featuresAndKeys.size(); i++) {
            if(featuresIndexes.contains(i))
            System.out.println(features.get(i));
        }

        List<Element> pairCoeffs = featureSelection.calculatePairFeaturesCoefficients();
        List<Element> informativeFeatures = featureSelection.getInformativePairs(pairCoeffs);

        System.out.println("_____ Информативные пары признаков и их коэффициенты _____");
        featureSelection.printPairFeatures(informativeFeatures);
    }
}