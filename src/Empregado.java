public class Empregado {
    private int id;
    private String nome;
    private String nLocal;

    public Empregado(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.nLocal = "";
    }

    public String getnLocal() {
        return nLocal;
    }

    public void setnLocal(String nLocal) {
        this.nLocal = nLocal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Empregado{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
