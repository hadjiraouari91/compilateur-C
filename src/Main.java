import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        
        if (args.length == 0) {
            SwingUtilities.invokeLater(() -> {
                new CompilerGUI().setVisible(true);
            });
            return; 
        }

        
        String filename = args[0];

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(filename)));
            System.out.println("Compilateur démarré sur : " + filename);
            System.out.println("----------------------------------------");
            
            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = lexer.tokenize();

            System.out.println("\n=== TOKENS ===");
            for (Token t : tokens) {
                System.out.println(t);
            }

            System.out.println("\n=== ANALYSE SYNTAXIQUE ===");
            Parser parser = new Parser(tokens);
            parser.parse();
            
        } catch (IOException e) {
            System.err.println("ERREUR : Impossible de lire le fichier '" + filename + "'");
        }
    }
}