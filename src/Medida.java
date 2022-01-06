public class Medida {
    private int idMedida;
    private String Designacao;
    public Medida(int idMedida, String Designacao){
        this.idMedida = idMedida;
        this.Designacao = Designacao;
    }

    public int getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(int idMedida) {
        this.idMedida = idMedida;
    }

    public String getDesignacao() {
        return Designacao;
    }

    public void setDesignacao(String designacao) {
        Designacao = designacao;
    }
}
