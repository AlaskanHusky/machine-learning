package by.fselection.Util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для работы с коллекциями.
 */
public class CollectionUtil {

    /**
     * Преобразует списки признаков и их коэффициентов
     * в словарь.
     *
     * @param   features  список признаков
     * @param   coeffs    список коэффициентов
     * @return            словарь признаков и их коэффициентов
     */
    public static Map<String, Float> convertToMap(List<String> features, List<Float> coeffs) {
        Map<String, Float> map = new HashMap<>();

        for (int i = 0; i < features.size(); i++) {
            map.put(features.get(i), coeffs.get(i));
        }

        return map;
    }

    /**
     * Сортирует словарь.
     *
     * @param   map  словарь признаков и их коэффициентов
     * @return       отсортированный словарь
     */
    public static Map<String, Float> sortMap(Map<String, Float> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
