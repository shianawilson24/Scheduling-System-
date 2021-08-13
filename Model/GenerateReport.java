package Model;

public class GenerateReport {
    private String type;
    private String month;
    private String january;
    private int may;
    private String feb;
    private String mar;
    private String apr;
    private String june;
    private String july;
    private String aug;
    private String sep;
    private String oct;
    private String nov;
    private String dec;
    private String Modified;

    public GenerateReport(String type, String month,String january,int may, String feb, String mar, String apr,String june, String july, String aug, String sep,String oct, String nov, String dec, String Modified){
        this.type=type;
        this.month=month;
        this.january=january;
        this.may=may;
        this.feb=feb;
        this.mar=mar;
        this.apr=apr;
        this.june=june;
        this.july=july;
        this.aug=aug;
        this.sep=sep;
        this.oct=oct;
        this.nov=nov;
        this.dec=dec;
        this.Modified=Modified;

    }



    public GenerateReport(String type) {
        this.type=type;
    }

    public String getModified() {
        return Modified;
    }

    public GenerateReport(int may) {

    }

    public String getDec() {
        return dec;
    }

    public String getNov() {
        return nov;
    }

    public String getOct() {
        return oct;
    }

    public String getSep() {
        return sep;
    }

    public String getAug() {
        return aug;
    }

    public String getJuly() {
        return july;
    }

    public String getJune() {
        return june;
    }

    public String getApr() {
        return apr;
    }

    public String getMar() {
        return mar;
    }

    public String getJanuary(){
        return january;
    }

    public int getMay() {
        return may;
    }

    public String getType(){
        return type;

    }
    public void setType(String type){
        this.type=type;
    }
    public String getMonth(){
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getFeb() {
        return feb;
    }
}
