package by.fselection;

class Element {
    private String firstFeature;
    private String secondFeature;
    private float coeff;

    Element() {
    }

    String getFirstFeature() {
        return firstFeature;
    }

    void setFirstFeature(String firstFeature) {
        this.firstFeature = firstFeature;
    }

    String getSecondFeature() {
        return secondFeature;
    }

    void setSecondFeature(String secondFeature) {
        this.secondFeature = secondFeature;
    }

    float getCoeff() {
        return coeff;
    }

    void setCoeff(float coeff) {
        this.coeff = coeff;
    }
}
