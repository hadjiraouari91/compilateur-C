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
                // Si on trouve un 'while', on analyse
                if (match(TokenType.WHILE)) {
                    whileStatement();
                } else {
                    advance(); // On ignore le reste (int main, declarations, etc.)
                }
            } catch (Exception e) {
                synchronize();
            }
        }
        
        if (!hasError) {
            System.out.println("\n[SUCCÈS] Analyse syntaxique terminée sans erreurs !");
        } else {
            System.out.println("\n[TERMINÉ] Analyse terminée avec des erreurs.");
        }
    }

    // --- NOUVELLE MÉTHODE PRINCIPALE ---
    private void whileStatement() {
        System.out.println("-> Début analyse structure WHILE");

        // 1. La parenthèse ouvrante
        consume(TokenType.LPAREN, "Attendu '(' après 'while'");

        // 2. La Condition (ex: i < 10)
        // On accepte : Variable/Nombre  OPERATEUR  Variable/Nombre
        parseCondition();

        // 3. La parenthèse fermante
        consume(TokenType.RPAREN, "Attendu ')' après la condition");

        // 4. L'accolade ouvrante
        consume(TokenType.LBRACE, "Attendu '{' pour le bloc while");

        // 5. Le corps de la boucle (on lit jusqu'à l'accolade fermante)
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
             // Ici, on pourrait valider les instructions intérieures
             // Pour l'instant, on avance juste pour consommer le contenu
             advance(); 
        }

        // 6. L'accolade fermante
        consume(TokenType.RBRACE, "Attendu '}' fin du bloc while");
        
        System.out.println("-> Fin analyse structure WHILE");
    }

    private void parseCondition() {
        // Une condition simple : x < 10
        if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER) || check(TokenType.MON_PRENOM)) {
            advance(); // Premier terme
        } else {
            error(peek(), "Condition invalide (attendu variable ou nombre)");
        }

        // Opérateur optionnel ( <, >, == )
        if (match(TokenType.LT, TokenType.GT, TokenType.EQUALS)) {
            // Deuxième terme
             if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER)) {
                advance();
            } else {
                error(peek(), "Attendu une valeur après l'opérateur de comparaison");
            }
        }
    }

    // --- UTILITAIRES (Identiques à avant) ---
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        error(peek(), message);
        return peek(); // Retourne le token erreur pour éviter crash
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
            
            // On s'arrête si on retrouve un mot clé de début d'instruction
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
    private Token previous() { return tokens.get(current - 1); }
}