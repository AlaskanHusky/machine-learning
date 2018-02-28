package by.fselection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        SimpleFeatureSelection featureSelection = new SimpleFeatureSelection();
        // Словарь, где ключ - признак, а значение - коэффициент признака
        Map<String, Float> featuresAndKeys = featureSelection.featureEvaluation();

        System.out.println("_____ Признаки и их коэффициенты _____");
        featureSelection.printFeaturesAndValues(featuresAndKeys);

        // Отсортированный словарь
        Map<String, Float> sortedFeatures = sortMap(featuresAndKeys);
        System.out.println("_____ Признаки и их коэффициенты (Отсортированные по убыванию информативности) _____");
        featureSelection.printFeaturesAndValues(sortedFeatures);

        System.out.println("_____ Информативные признаки _____");
        List<Integer> featuresIndexes = featureSelection.getFeaturesIndexes();
        List<String> features = featureSelection.getFeaturesList();

        for (int i = 0; i < featuresAndKeys.size(); i++) {
            if(featuresIndexes.contains(i))
            System.out.println(features.get(i));
        }
    }

    private static Map<String, Float> sortMap(Map<String, Float> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}