public class barEuros {
    private  String data;
    private  int idBar;
    private double ganhos;
    private double gasto;

    public barEuros(String data, int idBar, double ganhos, double gasto) {
        this.data = data;
        this.idBar = idBar;
        this.ganhos = ganhos;
        this.gasto = gasto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIdBar() {
        return idBar;
    }

    public void setIdBar(int idBar) {
        this.idBar = idBar;
    }

    public double getGanhos() {
        return ganhos;
    }

    public void setGanhos(double ganhos) {
        this.ganhos = ganhos;
    }

    public double getGasto() {
        return gasto;
    }

    public void setGasto(double gasto) {
        this.gasto = gasto;
    }
}
