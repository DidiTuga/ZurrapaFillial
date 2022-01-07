public class Medida {
    private int idMedida;
    private String Designacao;
    private int conversao;
    public Medida(int idMedida, String Designacao){
        this.idMedida = idMedida;
        this.Designacao = Designacao;
        this.conversao = 0;
    }

    public int getConversao() {
        return conversao;
    }

    public void setConversao(int conversao) {
        this.conversao = conversao;
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
