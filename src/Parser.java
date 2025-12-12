import java.util.Arrays;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private boolean hasError = false;

    
    private static final List<String> IGNORED_KEYWORDS = Arrays.asList(
        "int", "float", "char", "void", "return", "if", "else", "for", "do", "main", "printf", "scanf", "include"
    );

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            try {
                
                if (match(TokenType.WHILE)) {
                    whileStatement();
                } 
                
                
                else if (check(TokenType.LPAREN)) {
                    
                    error(peek(), "Syntaxe invalide : Instruction manquante avant '('. S'agit-il d'un 'while' ?");
                    synchronize();
                }

                
                else if (check(TokenType.IDENTIFIER)) {
                    String val = peek().value;

                    
                    if (IGNORED_KEYWORDS.contains(val)) {
                        advance();
                    }
                    
                    else if (isTypoOfWhile(val)) {
                        error(peek(), "Instruction inconnue '" + val + "'. Vouliez-vous dire 'while' ?");
                        synchronize(); 
                    }
                    
                    else {
                        
                        advance(); 
                    }
                }

            
                else {
                    advance(); 
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

    
    private void whileStatement() {
        System.out.println("-> Début analyse structure WHILE");

        consume(TokenType.LPAREN, "Attendu '(' après 'while'");
        parseCondition();
        consume(TokenType.RPAREN, "Attendu ')' après la condition");
        consume(TokenType.LBRACE, "Attendu '{' pour le bloc while");

        
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
             
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
        
        if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER) || check(TokenType.MON_PRENOM)) {
            advance(); 
        } else {
            error(peek(), "Condition invalide : attendu une variable, un nombre ou '" + TokenType.MON_PRENOM + "'");
        }

        
        if (match(TokenType.LT, TokenType.GT, TokenType.EQUALS)) {
            
             if (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER)) {
                advance();
            } else {
                error(peek(), "Attendu une valeur après l'opérateur de comparaison");
            }
        }
    }

    
    
    
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