public class ConteudoPedido {

    private int idPedido;
    private int idProduto;
    private int quantidade_servida;
    private int quantidade_pedida;

    public ConteudoPedido(int idPedido, int idProduto, int quantidade_servida, int quantidade_pedida) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.quantidade_servida = quantidade_servida;
        this.quantidade_pedida = quantidade_pedida;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade_servida() {
        return quantidade_servida;
    }

    public void setQuantidade_servida(int quantidade_servida) {
        this.quantidade_servida = quantidade_servida;
    }

    public int getQuantidade_pedida() {
        return quantidade_pedida;
    }

    public void setQuantidade_pedida(int quantidade_pedida) {
        this.quantidade_pedida = quantidade_pedida;
    }

    public String toString() {
        return "ConteudoPedido{" +
                "idPedido=" + idPedido +
                ", idProduto=" + idProduto +
                ", quantidade_servida=" + quantidade_servida +
                ", quantidade_pedida=" + quantidade_pedida +
                '}';
    }

    public Object clone() {
        ConteudoPedido x = new ConteudoPedido(this.idPedido, this.idProduto, this.quantidade_servida, this.quantidade_pedida);
        return x;
    }

}
