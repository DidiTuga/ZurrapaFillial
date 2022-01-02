public class Produto {
    private int id;
    private String nome;
    double preco_venda;
    double preco_compra;
    public Produto(){
        this.id = 0;
        this.nome = "";
        this.preco_venda = 0.00;
        this.preco_compra = 0.00;
    }
    public Produto(int id, String nome, double preco_venda, double preco_compra){
        this.id = id;
        this.nome = nome;
        this.preco_venda = preco_venda;
        this.preco_compra = preco_compra;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco_venda=" + preco_venda +
                ", preco_compra=" + preco_compra +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco_compra() {
        return preco_compra;
    }

    public void setPreco_compra(double preco_compra) {
        this.preco_compra = preco_compra;
    }

    public double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }
}
