package interpreter;

import java.io.*;
import interpreter.lexer.*;
import interpreter.node.*;
import interpreter.parser.*;

public class Main {

	public static void main(String[] args) {

        Parser p;
        try {
            if(args.length == 0) {
                p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(System.in), 1024)));
            }
            else {
                p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(new FileInputStream(args[1])))));
            }
            Node tree;
            tree = p.parse(); 
            System.out.println();
            Program.start(tree);
            if (args[0].equals("-html")) {
                Program.generateHTML();
            }
            if (args[0].equals("-pdf"))
                Program.generatePDF();
            if (args[0].equals("-jpg") || args[0].equals("-png"))
                Program.generateImage(args[0]);
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
}
