import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Vérifier qu'un argument est bien fourni
        if (args.length == 0) {
            System.err.println("Erreur : veuillez fournir un fichier source à analyser.");
            System.err.println("Usage : java -jar Moncompil.jar fichier_source");
            System.exit(1);  // Quitte le programme avec code erreur
        }

        String filename = args[0];  // Prend le premier argument comme nom de fichier

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(filename)));
            System.out.println("Compilateur démarré sur : " + filename);
            System.out.println("----------------------------------------");
            System.out.println("=== CODE SOURCE JS ===");
            System.out.println(sourceCode);
            System.out.println("======================");

            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = lexer.tokenize();

            System.out.println("\n=== TOKENS GÉNÉRÉS ===");
            for (Token t : tokens) {
                System.out.println(t);
            }

            System.out.println("\n=== ANALYSE SYNTAXIQUE ===");
            Parser parser = new Parser(tokens);
            parser.parse();
        } catch (IOException e) {
            System.err.println("ERREUR : Impossible de lire le fichier '" + filename + "'");
            System.err.println("Vérifiez que le fichier existe bien à la racine du projet.");
        }
    }
}
