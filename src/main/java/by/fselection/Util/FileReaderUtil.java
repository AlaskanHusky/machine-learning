package by.fselection.Util;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения данных из файла
 */
public class FileReaderUtil {

    private final static String FILE_NAME = "src/main/resources/data.xlsx";
    private final static String HEALTHY_SHEET_NAME = "Здоровые";
    private final static String SICK_SHEET_NAME = "Больные";
    private final static String FEATURES_SHEET_NAME = "Признаки";
    private final static int HEALTHY_PEOPLE_NUMBER = 26;
    private final static int SICK_PEOPLE_NUMBER = 28;
    private final static int FEATURES_NUMBER = 14;

    public FileReaderUtil() {

    }

    /**
     * Считывает из файла значения признаков здоровых людей.
     *
     * @return  матрица значений здоровых людей
     */
    public float[][] getHealthyFeaturesValues() {
        return getFeaturesValues(HEALTHY_SHEET_NAME, HEALTHY_PEOPLE_NUMBER);
    }

    /**
     * Считывает из файла значения признаков больных людей.
     *
     * @return  матрица значений больных людей
     */
    public float[][] getSickFeaturesValues() {
        return getFeaturesValues(SICK_SHEET_NAME, SICK_PEOPLE_NUMBER);
    }

    /**
     * Считывает из файла признаки.
     *
     * @return  список признаков
     */
    public List<String> getFeatures() {
        XSSFSheet myExcelSheet = null;

        try {
            XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(FILE_NAME));
            myExcelSheet = excelBook.getSheet(FEATURES_SHEET_NAME);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        List<String> features = new ArrayList<>();

        for (int i = 0; i < FEATURES_NUMBER; i++) {
            XSSFRow row = myExcelSheet.getRow(i + 1);
            features.add(row.getCell(0).toString());
        }

        return features;
    }

    /**
     * Считывает из файла значения признаков.
     *
     * @param   sheetName     название листа
     * @param   peopleNumber  количество людей
     * @return                матрица значений
     */
    private float[][] getFeaturesValues(String sheetName, int peopleNumber) {
        XSSFSheet myExcelSheet = null;

        try {
            XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(FILE_NAME));
            myExcelSheet = excelBook.getSheet(sheetName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        float[][] values = new float[FEATURES_NUMBER][peopleNumber];

        for (int i = 0; i < FEATURES_NUMBER; i++) {
            for (int j = 0; j < peopleNumber; j++) {
                XSSFRow row = myExcelSheet.getRow(j + 1);
                values[i][j] = Float.parseFloat(row.getCell(i + 1).getRawValue());
            }

        }

        return values;
    }
}
