package entitys;

/**
 * Recurso compartido de los threads
 * 
 * @author ramiro
 */
public class Mesa {
    
    private Integer carta_en_mesa;

    public Mesa() {
        this.carta_en_mesa = null;
    }

    public Integer getCarta_en_mesa() {
        return carta_en_mesa;
    }

    public void setCarta_en_mesa(Integer carta_en_mesa) {
        this.carta_en_mesa = carta_en_mesa;
    }
    
}
