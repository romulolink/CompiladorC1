package com.company;

/**
 *
 * @author PROFESSORES
 */
public class Token {
    private String lexema;
    private String tipo;

    public Token(String lx, String tp) {
        lexema = lx;
        tipo = tp;
    }

    public boolean igual(String tp) {
        return tipo.equals(tp);
    }

    public String toString() {
        return "<" + tipo + "," + lexema + ">";
    }
}
