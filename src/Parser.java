import java.util.Arrays;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private boolean hasError = false;

    // Liste des mots-clés C valides qu'on doit ignorer (pour ne pas créer de fausses erreurs)
    private static final List<String> IGNORED_KEYWORDS = Arrays.asList(
        "int", "float", "char", "void", "return", "if", "else", "for", "do", "main", "printf", "scanf", "include"
    );

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            try {
                // CAS 1 : C'est un WHILE correct -> On analyse
                if (match(TokenType.WHILE)) {
                    whileStatement();
                } 
                
                // CAS 2 : L'utilisateur a oublié le mot 'while' -> Ça commence par '('
                else if (check(TokenType.LPAREN)) {
                    // On vérifie si c'est suivi d'une condition et d'une accolade, signe d'un bloc orphelin
                    error(peek(), "Syntaxe invalide : Instruction manquante avant '('. S'agit-il d'un 'while' ?");
                    synchronize();
                }

                // CAS 3 : Gestion des mots inconnus ou des fautes de frappe
                else if (check(TokenType.IDENTIFIER)) {
                    String val = peek().value;

                    // Si c'est un mot-clé C valide (mais pas while), on l'ignore proprement
                    if (IGNORED_KEYWORDS.contains(val)) {
                        advance();
                    }
                    // Si ça ressemble à "while" (faute de frappe ex: wile, whle, whil)
                    else if (isTypoOfWhile(val)) {
                        error(peek(), "Instruction inconnue '" + val + "'. Vouliez-vous dire 'while' ?");
                        synchronize(); // On saute ce mot pour essayer de continuer
                    }
                    // Sinon, c'est du code inconnu (ou une variable seule), on l'ignore ou on avertit
                    else {
                        // Optionnel : On peut ignorer silencieusement les variables isolées
                        advance(); 
                    }
                }

                // CAS 4 : Autre chose (Symboles isolés, nombres...), on avance pour ne pas bloquer
                else {
                    advance(); 
                }

            } catch (Exception e) {
                synchronize();
            }
        }
        
        // Résultat final
        if (!hasError) {
            System.out.println("\n[SUCCÈS] Analyse syntaxique terminée sans erreurs !");
        } else {
            System.out.println("\n[TERMINÉ] Analyse terminée avec des erreurs.");
        }
    }

    // --- ANALYSE DU WHILE (Cœur du sujet) ---
    private void whileStatement() {
        System.out.println("-> Début analyse structure WHILE");

        consume(TokenType.LPAREN, "Attendu '(' après 'while'");
        parseCondition();
        consume(TokenType.RPAREN, "Attendu ')' après la condition");
        consume(TokenType.LBRACE, "Attendu '{' pour le bloc while");

        // On consomme le corps de la boucle
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
             // Si on trouve un autre while imbriqué, on l'analyse aussi ! (Bonus robustesse)
             if (check(TokenType.WHILE)) {
                 advance();
                 whileStatement();
             } else {
                 advance();
             }
        }

        consume(TokenType.RBRACE, "Attendu '}' fin du bloc while");
        System.out.println("-> Fin analyse structure WHILE");
    }

    private void parseCondition() {
        // Premier terme
        if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER) || check(TokenType.MON_PRENOM)) {
            advance(); 
        } else {
            error(peek(), "Condition invalide : attendu une variable, un nombre ou '" + TokenType.MON_PRENOM + "'");
        }

        // Opérateur optionnel
        if (match(TokenType.LT, TokenType.GT, TokenType.EQUALS)) {
            // Deuxième terme
             if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER)) {
                advance();
            } else {
                error(peek(), "Attendu une valeur après l'opérateur de comparaison");
            }
        }
    }

    // --- UTILITAIRES ---
    
    // Petite fonction pour détecter les typos de "while" (ex: whil, wile)
    private boolean isTypoOfWhile(String text) {
        return text.equals("whil") || text.equals("wile") || text.equals("whle") || text.equals("wyle");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        error(peek(), message);
        return peek(); 
    }

    private void error(Token token, String message) {
        hasError = true;
        System.err.println("[ERREUR SYNTAXIQUE] Ligne " + token.line + " : " + message + " (Trouvé: " + token.value + ")");
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;
            if (previous().type == TokenType.RBRACE) return;
            
            // On s'arrête si on retrouve un mot clé connu
            if (peek().type == TokenType.WHILE || peek().type == TokenType.INT || peek().type == TokenType.RETURN) {
                return;
            }
            advance();
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() { return peek().type == TokenType.EOF; }
    private Token peek() { return tokens.get(current); }
    private Token previous() { return tokens.get(current - 1); }
}