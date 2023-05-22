public class Pixel {
    int rVal;
    int gVal;
    int bVal;
    int saturation;
    public Pixel(int rVal, int gVal, int bVal) {
        this.rVal = rVal;
        this.gVal = gVal;
        this.bVal = bVal;
        this.saturation = (rVal + gVal + bVal)/3;
    }

    public int getrVal() {
        return rVal;
    }

    public void setrVal(int rVal) {
        this.rVal = rVal;
    }

    public int getgVal() {
        return gVal;
    }

    public void setgVal(int gVal) {
        this.gVal = gVal;
    }

    public int getbVal() {
        return bVal;
    }

    public void setbVal(int bVal) {
        this.bVal = bVal;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
