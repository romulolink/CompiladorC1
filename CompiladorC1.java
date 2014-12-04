package com.company;

/**
 *
 * @author PROFESSORES
 */
import java.io.*;

public class CompiladorC1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            BufferedReader in = new BufferedReader(new FileReader("C:/Users/Work/Desktop/CompiladorC1/prog1.c1"));
            String linha, programa;
            linha = in.readLine();
            programa = "";
            while (linha != null) {
                programa = programa + linha + "\n";
                linha = in.readLine();
            }
            C1 compilador = new C1(programa);

            in.close();
        } catch (IOException e) {
            System.out.println("prog1.c1 não encontrado!");
        }
        int x=4;
        int y=1;
        switch (x+y) {
            case 4: System.out.println("OK"); break;
            case 0: System.out.println("OK"); break;
            default: System.out.println("Erro");
        }
    }

}
