import java.util.Objects;

public class Produto {
    private int id;
    private String nome;
    private double preco_venda;
    private double preco_compra;
    public int quantidade;
    private int ultimoID = 0;

    public Produto() {
        this.id = 0;
        this.nome = "";
        this.preco_venda = 0.00;
        this.preco_compra = 0.00;
        this.quantidade = 0;
    }

    public Produto(int id, String nome, double preco_venda, double preco_compra) {
        this.id = id;
        this.nome = nome;
        this.preco_venda = preco_venda;
        this.preco_compra = preco_compra;
        this.quantidade = 0;
    }
    public Produto(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.preco_venda = 0;
        this.preco_compra = 0;
        this.quantidade = 0;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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

    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco_venda=" + preco_venda +
                ", preco_compra=" + preco_compra +
                ", quantidade=" + quantidade +
                '}';
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id == produto.id && Double.compare(produto.preco_venda, preco_venda) == 0 && Double.compare(produto.preco_compra, preco_compra) == 0 && quantidade == produto.quantidade && ultimoID == produto.ultimoID && Objects.equals(nome, produto.nome);
    }
    public Object clone() {
        Produto x = new Produto(this.id, this.nome, this.preco_venda, this.preco_compra);
        return x;
    }


}
