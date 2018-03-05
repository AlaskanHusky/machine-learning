package by.fselection;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        SimpleFeatureSelection featureSelection = new SimpleFeatureSelection();
        List<Element> coeffs = featureSelection.calculateFeaturesCoefficients();
        List<Element> informativeFeatures = featureSelection.getInformativeFeatures(coeffs);

        System.out.println("_____ Информативные признаки и их коэффициенты_____");
        featureSelection.printFeatures(informativeFeatures);

        List<Element> pairCoeffs = featureSelection.calculatePairFeaturesCoefficients();
        List<Element> informativePairFeatures = featureSelection.getInformativePairs(pairCoeffs);

        System.out.println("_____ Информативные пары признаков и их коэффициенты _____");
        featureSelection.printPairFeatures(informativePairFeatures);
    }
}