package by.fselection;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileReaderUtil {

    FileReaderUtil() {

    }

    float[][] getIndicatorsValues(String fileName, String sheetName, int peopleNumber) {
        final int indicatorsNumber = 14;
        XSSFSheet myExcelSheet = null;

        try {
            XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(fileName));
            myExcelSheet = excelBook.getSheet(sheetName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        float[][] healthyMatrix = new float[indicatorsNumber][peopleNumber];

        for (int i = 0; i < indicatorsNumber; i++) {
            for (int j = 0; j < peopleNumber; j++) {
                XSSFRow row = myExcelSheet.getRow(j + 1);
                healthyMatrix[i][j] = Float.parseFloat(row.getCell(i + 1).getRawValue());
            }

        }

        return healthyMatrix;
    }

    List<String> getIndicators(String fileName, String sheetName, int indicatorsNumber) {
        XSSFSheet myExcelSheet = null;

        try {
            XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(fileName));
            myExcelSheet = excelBook.getSheet(sheetName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        List<String> indicators = new ArrayList<>();

        for (int i = 0; i < indicatorsNumber; i++) {
            XSSFRow row = myExcelSheet.getRow(i + 1);
            indicators.add(row.getCell(0).toString());
        }

        return indicators;
    }

}
