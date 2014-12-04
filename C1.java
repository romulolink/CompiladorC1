/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.company;

/**
 *
 * @author PROFESSORES
 */
import java.util.*;

public class C1 {
    private String programa;
    private Token token;
    private String prox;
    private int pont;
    private int linha;
    private Hashtable<String,Token> Tabela;

    public C1(String prog) {
        programa = prog;
        pont = 0;
        linha = 0;
        Tabela = new Hashtable<String,Token>();
        Tabela.put("read", new Token("read","read"));
        Tabela.put("write", new Token("write","write"));
        Tabela.put("halt", new Token("halt","halt"));
        Tabela.put("main", new Token("main","main"));
        Tabela.put("do", new Token("do","do"));
        Tabela.put("while", new Token("while","while"));
        Tabela.put("for", new Token("for","for"));
        Tabela.put("if", new Token("if","if"));
        Tabela.put("else", new Token("else","else"));
        Tabela.put("break", new Token("break","break"));
        Tabela.put("continue", new Token("continue","continue"));
        Tabela.put("int", new Token("int","tipo"));
        Tabela.put("double", new Token("double","tipo"));
        Tabela.put("boolean", new Token("boolean","tipo"));

        prox = " ";
        token = analex();
        /*while (!token.igual("fim")) {
            System.out.println("token=" + token);
            token = analex();
        }*/
        if (programa()){
            System.out.println("Programa correto");
        }
    }

    public String proximo() {
        try {
            String resultado = programa.charAt(pont)+"";
            pont++;
            return resultado;
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean branco(String p) {
        return p.equals(" ") || p.equals("\t");
    }
    public boolean newLine(String p) {
        return p.equals("\n");
    }
    public void nada() {}

    public boolean digito(String p) {
        return ('0' <= p.charAt(0)) && (p.charAt(0)<='9');
    }
    public boolean letra(String p) {
        return (('a' <= p.charAt(0)) && (p.charAt(0)<='z'))  ||
                (('A' <= p.charAt(0)) && (p.charAt(0)<='Z')) ||
                (p.charAt(0) == '_');
    }

    public Token analex() {
        try {
            for ( ; ; prox=proximo() ) {
                if ( branco(prox)) {
                    nada();
                } else if ( newLine(prox) ) {
                    linha = linha+1;
                } else {
                    break;
                }
            }
            if (digito(prox)) {
                String v="";
                do {
                    v = v + prox;
                    prox = proximo();
                } while (digito(prox));
                return new Token(v, "num");
            }
            if (letra(prox)) {
                String s="";
                do {
                    s = s + prox;
                    prox = proximo();
                } while (letra(prox));
                Token w = Tabela.get(s);
                if ( w != null ) {
                    return w;
                } else {
                    w = new Token(s,"id");
                    Tabela.put(s, w);
                    return w;
                }
            } 
            /* se atingiu este ponto, o caracter em prox é uma token */
            if (prox.equals("=")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("==","==");
                } else {
                    return new Token("=","=");
                }
            } else if (prox.equals("!")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("!=","!=");
                } else {
                    return new Token("!","!");
                }
            } else if (prox.equals("<")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("<=","<=");
                } else {
                    return new Token("<","<");
                }
            } else if (prox.equals(">")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token(">=",">=");
                } else {
                    return new Token(">",">");
                }
            } else if (prox.equals("+")) {
                prox = proximo();
                if (prox.equals("+")) {
                    prox = " ";
                    return new Token("++","++");
                } else if (prox.equals("=")) {
                    prox = " ";
                    return new Token("+=","+=");
                } else {
                    return new Token("+","+");
                }
            } else if (prox.equals("-")) {
                prox = proximo();
                if (prox.equals("-")) {
                    prox = " ";
                    return new Token("--","--");
                } else if (prox.equals("=")) {
                    prox = " ";
                    return new Token("-=","-=");
                } else {
                    return new Token("-","-");
                }
            } else if (prox.equals("*")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("*=","*=");
                } else {
                    return new Token("*","*");
                }
            } else if (prox.equals("/")) {
                prox = proximo();
                if (prox.equals("=")) {
                    prox = " ";
                    return new Token("/=","/=");
                } else {
                    return new Token("/","/");
                }
            } else if (prox.equals("&")) {
                prox = proximo();
                if (prox.equals("&")) {
                    prox = " ";
                    return new Token("&&","&&");
                } else {
                    return new Token("&","&");
                }
            } else if (prox.equals("|")) {
                prox = proximo();
                if (prox.equals("|")) {
                    prox = " ";
                    return new Token("||","||");
                } else {
                    return new Token("|","|");
                }
            } else {
                Token w = new Token(prox,prox);
                prox = " "; /* initialization */
                return w;
            }
        } catch (NullPointerException e) {
            return new Token("fim","fim");
        }
    }

    /* implementação dos diagramas hierárquicos */
    public boolean Erro(String msg) {
        System.out.println(linha + ": Erro: " + msg);
        return false;
    }
    public boolean match(String x) {
        if (token.igual(x)) {
            token = analex();
            return true;
        } else {
            return false;
        }
    }

    public boolean programa() {
        System.out.println("Programa:" + token);
        if (!match("main")) return Erro("main esperado!");
        if (!match("(")) return Erro("'(' esperado!");
        if (!match(")")) return Erro("')' esperado!");

        if (!bloco()) return Erro("Erro no bloco.");
        return true;
    }

    public boolean bloco() {
        System.out.println("Bloco:" + token);
        if (token.igual("{")) {
            match("{");
            while (true) {
                if (!comando()) return Erro("Erro no comando");
                if (token.igual("}")) {
                    match("}");
                    return true;
                }
            }
        } else {
            return comando();
        }
    }

    public boolean comando() {
        System.out.println("Comando:" + token);
        boolean tem_ponto_virgula= true;

        if (token.igual("halt")) {
            match("halt");
        } if (token.igual("break")) {
            match("break");
        } if (token.igual("continue")) {
            match("continue");
        } if (token.igual("tipo")) {
            if (!declaracao()) return Erro("Erro na declareção.");
        } else if (token.igual("read")) {
            if (!leitura()) return Erro("Erro na leitura");
        } else if (token.igual("write")) {
            if (!escrita()) return Erro("Erro na escrita");
        } else if (token.igual("id")) {
            if (!atrib()) return Erro("Erro na ATRIBUIÇÃO");
        } else if (token.igual("do")) {
            if (!rep_do()) return Erro("Erro no do-while");
        } else if (token.igual("while")) {
            tem_ponto_virgula= false;
            if (!rep_while()) return Erro("Erro no while");
        } else if (token.igual("for")) {
            tem_ponto_virgula= false;
            if (!rep_for()) return Erro("Erro no for");
        } else if (token.igual("if")) {
            tem_ponto_virgula= false;
            if (!sel_if()) return Erro("Erro no if");
        }
        if (tem_ponto_virgula && (!match(";"))) return Erro("';' esperado.");

        return true;
    }

    public boolean declaracao() {
        System.out.println("Declaração: " + token);
        match("tipo");
        while (true) {
            if (!match("id")) return Erro("identificador esperado");
            if (token.igual("[")) {
                if (!dimensoes()) return Erro("Erro na dimensao do array");
            }
            if (token.igual("=")) {
                match("=");
                if (!expr()) return Erro("Erro na expressao");
            }
            if (token.igual(",")) {
                match(",");
            } else {
                break;
            }
        }
        return true;
    }

    public boolean incremento() {
        System.out.println("Incremento: " + token);
        if (token.igual("++")) {
            return match("++");
        } else if (token.igual("--")) {
            return match("--");
        }
        return false;
    }

    public boolean dimensoes() {
        System.out.println("Dimensoes: " + token);
        while (true) {
            match("[");
            if (!expr()) return Erro("Erro na expressão");
            if (!match("]")) return Erro("']' esperado");
            if (!token.igual("[")) break;
        }
        return true;
    }
    public boolean rep_do() {
        System.out.println("Rep_do:" + token);
        match("do");
        if (!bloco()) return Erro("Erro no bloco");
        System.out.println("token=" + token);
        if (!match("while")) return Erro("'while' esperado.");
        if (!match("(")) return Erro("'(' esperado.");
        if (!expr()) return Erro("Erro na expressão");
        if (!match(")")) return Erro("')' esperado.");
        return true;
    }
    public boolean rep_while() {
        System.out.println("Rep_while:" + token);
        match("while");
        if (!match("(")) return Erro("'(' esperado.");
        if (!expr()) return Erro("Erro na expressão");
        if (!match(")")) return Erro("')' esperado.");
        if (!bloco()) return Erro("Erro no bloco");
        return true;
    }
    public boolean rep_for() {
        System.out.println("Rep_for:" + token);
        match("for");
        if (!match("(")) return Erro("'(' esperado.");
        if(!atrib()) return Erro("atribuição esperada");
        if (!match(";")) return Erro("';' esperado.");
        if (!expr()) return Erro("Erro na expressão");
        if (!match(";")) return Erro("';' esperado.");
        if(!atrib()) return Erro("atribuição esperada");
        if (!match(")")) return Erro("')' esperado.");
        if (!bloco()) return Erro("Erro no bloco");
        return true;
    }
    public boolean sel_if() {
        System.out.println("Sel_if:" + token);
        match("if");
        if (!match("(")) return Erro("'(' esperado.");
        if (!expr()) return Erro("Erro na expressão");
        if (!match(")")) return Erro("')' esperado.");
        if (!bloco()) return Erro("Erro no bloco");
        if (token.igual("else")) {
            match("else");
            if (!bloco()) return Erro("Erro no bloco");
        }
        return true;}

    public boolean atrib() {
        System.out.println("Atrib:" + token);
        match("id");
        if (token.igual("[")) {
            if (!dimensoes()) return Erro("Erro na dimensão.");
        }
        if (!match("=")) return Erro("'=' esperado!");
        if (!expr()) return Erro("erro na expressão (1)!");
        return true;
    }

    public boolean leitura() {
        System.out.println("Leitura:" + token);
        match("read");
        if (!match("(")) return Erro("'(' esperado!");
        if (!match("id")) return Erro("Identificador esperado!");
        if (!match(")")) return Erro("')' esperado!");
        return true;
    }

    public boolean escrita() {
        System.out.println("Escrita:" + token);
        match("write");
        if (!match("(")) return Erro("'(' esperado!");
        if (!expr()) return Erro("erro na expressão (1)!");
        if (!match(")")) return Erro("')' esperado!");
        return true;
    }

    public boolean expr() {
        System.out.println("expr:" + token);
        if (!termo1()) return Erro("erro no termo1 (1)");
        if (!r1()) return Erro("erro no R1");
        return true;
    }
    public boolean r1() {
        System.out.println("r1:" + token);
        if (token.igual("&&")) {
            match("&&");
            if (!termo1()) return Erro("erro no termo1 (2)");
            if (!r1()) return Erro("erro no R1");
        } else if (token.igual("||")) {
            match("||");
            if (!termo1()) return Erro("erro no termo (2)");
            if (!r1()) return Erro("erro no R1");
        }
        return true;
    }

    public boolean termo1() {
        System.out.println("termo1:" + token);
        if (!termo2()) return Erro("erro no termo2 (1)");
        if (!r2()) return Erro("erro no R2");
        return true;
    }
    public boolean r2() {
        System.out.println("r2:" + token);
        if (token.igual("==")) {
            match("==");
            if (!termo2()) return Erro("erro no termo2 (2)");
            if (!r2()) return Erro("erro no R2");
        } else if (token.igual("!=")) {
            match("!=");
            if (!termo2()) return Erro("erro no termo2 (2)");
            if (!r2()) return Erro("erro no R2");
        } else if (token.igual("<")) {
            match("<");
            if (!termo2()) return Erro("erro no termo2 (2)");
            if (!r2()) return Erro("erro no R2");
        } else if (token.igual("<=")) {
            match("<=");
            if (!termo2()) return Erro("erro no termo2 (2)");
            if (!r2()) return Erro("erro no R2");
        } else if (token.igual(">")) {
            match(">");
            if (!termo2()) return Erro("erro no termo2 (2)");
            if (!r2()) return Erro("erro no R2");
        } else if (token.igual(">=")) {
            match(">=");
            if (!termo2()) return Erro("erro no termo2 (2)");
            if (!r2()) return Erro("erro no R2");
        }
        return true;
    }

    public boolean termo2() {
        System.out.println("termo2:" + token);
        if (!termo3()) return Erro("erro no termo3 (1)");
        if (!r3()) return Erro("erro no R3");
        return true;
    }
    public boolean r3() {
        System.out.println("r3:" + token);
        if (token.igual("+")) {
            match("+");
            if (!termo3()) return Erro("erro no fator (2)");
            if (!r3()) return Erro("erro no R3");
        } else if (token.igual("-")) {
            match("-");
            if (!termo3()) return Erro("erro no termo3 (2)");
            if (!r3()) return Erro("erro no R3");
        }
        return true;
    }

    public boolean termo3() {
        System.out.println("termo3:" + token);
        if (!fator()) return Erro("erro no fator (1)");
        if (!r4()) return Erro("erro no R4");
        return true;
    }
    public boolean r4() {
        System.out.println("r4:" + token);
        if (token.igual("*")) {
            match("*");
            if (!fator()) return Erro("erro no fator (2)");
            if (!r4()) return Erro("erro no R4");
        } else if (token.igual("/")) {
            match("/");
            if (!fator()) return Erro("erro no fator (2)");
            if (!r4()) return Erro("erro no R4");
        } else if (token.igual("%")) {
            match("%");
            if (!fator()) return Erro("erro no fator (2)");
            if (!r4()) return Erro("erro no R4");
        }
        return true;
    }

    public boolean fator() {
        System.out.println("fator:" + token);
        if (token.igual("id")) {
            match("id");
            if (token.igual("[")) {
                if (!dimensoes()) return Erro("Erro na dimensão.");
            }
            if (token.igual("++")) {
                return incremento();
            } else if (token.igual("--")) {
                return incremento();
            }
            return true ;
        }
        if (token.igual("++") || token.igual("--")) {
            incremento();
            if (!match("id")) return Erro("Identificador esperado.");
            if (token.igual("[")) {
                if (!dimensoes()) return Erro("Erro na dimensão.");
            }
            return true;
        }
        if (token.igual("num")) {
            return match("num");
        }
        if (token.igual("(")) {
            match("(");
            if (!expr()) return Erro("erro na expressão (2)");
            if (!match(")")) return Erro("')' esperado!");
        }
        return false;
    }
}
