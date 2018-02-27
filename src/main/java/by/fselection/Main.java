package by.fselection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String fileName = "src/main/resources/data.xlsx";
        String healthySheet = "Здоровые";
        String sickSheet = "Больные";
        String indicatorsSheet = "Признаки";
        int healthyPeopleNumber = 26;
        int sickPeopleNumber = 28;
        int indicatorsNumber = 14;
        FileReaderUtil fileReader = new FileReaderUtil();
        SimpleFeatureSelection featureSelection = new SimpleFeatureSelection();

        // Матрица признаков и их значений здоровых людей
        float[][] healthyMatrix = fileReader.getIndicatorsValues(fileName, healthySheet, healthyPeopleNumber);
        // Матрица признаков и их значений больных людей
        float[][] sickMatrix = fileReader.getIndicatorsValues(fileName, sickSheet, sickPeopleNumber);
        // Объединенная матрица здоровых и больных людей
        float[][] jointMatrix = featureSelection.concatMatrixByColumns(healthyMatrix, sickMatrix);
        // Нормализованная матрица здоровых и больных людей
        float[][] normalizedMatrix = featureSelection.matrixNormalization(jointMatrix);
        // Нормализованная матрица здоровых людей
        float[][] healthyNormalizedMatrix = featureSelection.splitMatrix(normalizedMatrix, healthyPeopleNumber, 0, 26);
        // Нормализованная матрица больных людей
        float[][] sickNormalizedMatrix = featureSelection.splitMatrix(normalizedMatrix, sickPeopleNumber, 26, 54);
        /*
            Коэффициенты близости. Данная величина показывает степень совпадения значений одного класса с другим.
            Чем меньше значения, тем информативнее признак.
            То есть значение 0.2 говорит о том, что этот признак информативнее, чем признак со значением 0.6.
        */
        Float[] coeffs = featureSelection.getCompactnessCoefficients(healthyNormalizedMatrix, sickNormalizedMatrix, indicatorsNumber);
        // Текстовый список признаков
        List<String> features = fileReader.getIndicators(fileName, indicatorsSheet, indicatorsNumber);

        Map<String, Float> unsortedMap = featureSelection.convertToHashMap(features, coeffs);

        Map<String, Float> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Float> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + ": "+ entry.getValue());
        }
    }
}