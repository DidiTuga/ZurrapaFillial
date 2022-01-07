public class Stock {
    private int IDStock;
    private int IDProduto;
    private int Qtd;
    private int IDMedida;
    private String Designacao;
    public Stock(int idstock, int idproduto, String Designacao, int qtd, int idmedida){
        this.IDMedida = idmedida;
        this.IDProduto = idproduto;
        this.IDStock = idstock;
        this.Qtd = qtd;
        this.Designacao = Designacao;
    }

    public String getDesignacao() {
        return Designacao;
    }

    public void setDesignacao(String designacao) {
        Designacao = designacao;
    }

    public int getIDStock() {
        return IDStock;
    }

    public void setIDStock(int IDStock) {
        this.IDStock = IDStock;
    }

    public int getIDProduto() {
        return IDProduto;
    }

    public void setIDProduto(int IDProduto) {
        this.IDProduto = IDProduto;
    }

    public int getQtd() {
        return Qtd;
    }

    public void setQtd(int qtd) {
        Qtd = qtd;
    }

    public int getIDMedida() {
        return IDMedida;
    }

    public void setIDMedida(int IDMedida) {
        this.IDMedida = IDMedida;
    }

    public String toString() {
        return "Stock{" +
                "IDStock=" + IDStock +
                ", IDProduto=" + IDProduto +
                ", Qtd=" + Qtd +
                ", IDMedida=" + IDMedida +
                ", Designacao='" + Designacao + '\'' +
                '}';
    }
}
