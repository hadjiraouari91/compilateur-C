import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private boolean hasError = false;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            try {
                // CAS 1 : C'est un WHILE correct
                if (match(TokenType.WHILE)) {
                    whileStatement();
                } 
                
                // CAS 2 : L'utilisateur a oublié le mot 'while' -> Ça commence par '('
                else if (check(TokenType.LPAREN)) {
                    error(peek(), "Syntaxe invalide : Instruction 'while' manquante ou inattendue avant '('.");
                    synchronize();
                }

                // CAS 3 : Faute de frappe (ex: 'whil', 'wile')
                // On vérifie si c'est un mot suivi d'une parenthèse '('
                else if (check(TokenType.IDENTIFIER) && peekNext().type == TokenType.LPAREN) {
                    String val = peek().value;
                    // On autorise les fonctions C standards, mais on bloque le reste
                    if (!val.equals("main") && !val.equals("printf") && !val.equals("scanf") && !val.equals("if")) {
                        error(peek(), "Instruction inconnue '" + val + "'. Vouliez-vous dire 'while' ?");
                        synchronize();
                    } else {
                        advance(); // C'est main() ou printf(), on laisse passer
                    }
                }

                // CAS 4 : Autre chose (int, float, etc.), on ignore
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

    // --- ANALYSE DU WHILE (Reste identique) ---
    private void whileStatement() {
        System.out.println("-> Début analyse structure WHILE");

        consume(TokenType.LPAREN, "Attendu '(' après 'while'");
        parseCondition();
        consume(TokenType.RPAREN, "Attendu ')' après la condition");
        consume(TokenType.LBRACE, "Attendu '{' pour le bloc while");

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
             advance(); 
        }

        consume(TokenType.RBRACE, "Attendu '}' fin du bloc while");
        System.out.println("-> Fin analyse structure WHILE");
    }

    private void parseCondition() {
        if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER) || check(TokenType.MON_PRENOM)) {
            advance(); 
        } else {
            error(peek(), "Condition invalide (attendu variable ou nombre)");
        }

        if (match(TokenType.LT, TokenType.GT, TokenType.EQUALS)) {
             if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER)) {
                advance();
            } else {
                error(peek(), "Attendu une valeur après l'opérateur de comparaison");
            }
        }
    }

    // --- UTILITAIRES ---
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        error(peek(), message);
        // On retourne le token actuel pour ne pas crasher, mais l'erreur est notée
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
            
            switch (peek().type) {
                case INT: case FLOAT: case WHILE: case IF: case RETURN:
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
    
    // NOUVELLE MÉTHODE NÉCESSAIRE POUR LA CORRECTION
    private Token peekNext() { 
        if (current + 1 >= tokens.size()) return tokens.get(tokens.size() - 1);
        return tokens.get(current + 1); 
    }

    private Token previous() { return tokens.get(current - 1); }
}