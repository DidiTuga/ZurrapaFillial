public class Local {
    private int idLocal;
    private String Nome;


    public Local(int idLocal, String nome) {
        this.idLocal = idLocal;
        this.Nome = nome;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}
