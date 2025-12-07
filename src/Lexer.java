import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;
    private int line = 1;
    private final int length;
    private boolean hasError = false;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < length) {
            char current = peek();

            // 1. GESTION DES COMMENTAIRES (// et /* */)
            if (current == '/') {
                if (peekNext() == '/') {
                    skipComment(); // Commentaire simple ligne //
                    continue; // On passe à la suite de la boucle
                } else if (peekNext() == '*') {
                    skipMultiLineComment(); // Commentaire multi-lignes /* */
                    continue; // On passe à la suite
                }
            }
            
            // 2. Ignorer les espaces blancs
            if (Character.isWhitespace(current)) {
                if (current == '\n') line++;
                advance();
            } 
            // 3. Reconnaître les Mots 
            else if (Character.isLetter(current)) {
                tokens.add(scanWord());
            } 
            // 4. Reconnaître les Nombres
            else if (Character.isDigit(current)) {
                tokens.add(scanNumber());
            } 
            // 5. Reconnaître les Strings
            else if (current == '"' || current == '\'') {
                tokens.add(scanString(current));
            }
            // 6. Reconnaître les Symboles (incluant le / de la division)
            else {
                tokens.add(scanSymbol());
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line));

        if (!hasError) {
            System.out.println("[SUCCÈS] Analyse lexicale terminée sans erreurs !");
        }

        return tokens;
    }

    // Ignorer les commentaires simples (//)
    private void skipComment() {
        while (peek() != '\n' && pos < length) {
            advance();
        }
    }

    // NOUVEAU : Ignorer les commentaires multi-lignes (/* ... */)
    private void skipMultiLineComment() {
        advance(); // Consomme '/'
        advance(); // Consomme '*'

        // Tant qu'on n'a pas trouvé "*/" et qu'on n'est pas à la fin du fichier
        while (pos < length && !(peek() == '*' && peekNext() == '/')) {
            if (peek() == '\n') line++; // Important : on compte les lignes même dans le commentaire
            advance();
        }

        // On consomme le "*/" final
        if (pos < length) advance(); // Consomme '*'
        if (pos < length) advance(); // Consomme '/'
    }

    private Token scanString(char quoteType) {
        advance(); 
        StringBuilder sb = new StringBuilder();
        while (pos < length && peek() != quoteType && peek() != '\n' && peek() != '\r') {
            sb.append(advance());
        }
        if (pos < length && peek() == quoteType) {
            advance(); 
            return new Token(TokenType.STRING, sb.toString(), line);
        } else {
            System.err.println("[ERREUR LEXICALE] Ligne " + line + " : Chaîne non terminée.");
            hasError = true;
            return new Token(TokenType.UNKNOWN, sb.toString(), line);
        }
    }

    private Token scanNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < length && Character.isDigit(peek())) {
            sb.append(advance());
        }
        if (peek() == '.' && Character.isDigit(peekNext())) {
            sb.append(advance()); 
            while (pos < length && Character.isDigit(peek())) {
                sb.append(advance());
            }
        }
        return new Token(TokenType.NUMBER, sb.toString(), line);
    }

    private Token scanWord() {
        StringBuilder sb = new StringBuilder();
        while (pos < length && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        String text = sb.toString();

        switch (text) {
            case "while":  return new Token(TokenType.WHILE, text, line);
            case "if":     return new Token(TokenType.IF, text, line);
            case "else":   return new Token(TokenType.ELSE, text, line);
            case "return": return new Token(TokenType.RETURN, text, line);
            case "for":    return new Token(TokenType.FOR, text, line);
            case "do":     return new Token(TokenType.DO, text, line);
            case "int":    return new Token(TokenType.INT, text, line);
            case "float":  return new Token(TokenType.FLOAT, text, line);
            case "char":   return new Token(TokenType.CHAR, text, line);
            case "void":   return new Token(TokenType.VOID, text, line);
            case "include":return new Token(TokenType.INCLUDE, text, line);
            
            case "Hadjira": return new Token(TokenType.MON_PRENOM, text, line);
            case "Ouari":return new Token(TokenType.MON_NOM, text, line);
            
            default: return new Token(TokenType.IDENTIFIER, text, line);
        }
    }

    private Token scanSymbol() {
        char current = advance();
        switch (current) {
            case '(': return new Token(TokenType.LPAREN, "(", line);
            case ')': return new Token(TokenType.RPAREN, ")", line);
            case '{': return new Token(TokenType.LBRACE, "{", line);
            case '}': return new Token(TokenType.RBRACE, "}", line);
            case ':': return new Token(TokenType.COLON, ":", line);
            case ';': return new Token(TokenType.SEMICOLON, ";", line);
            case ',': return new Token(TokenType.COMMA, ",", line);
            case '.': return new Token(TokenType.DOT, ".", line); 
            case '=': 
                if (peek() == '=') { advance(); return new Token(TokenType.EQUALS, "==", line); }
                return new Token(TokenType.EQUALS, "=", line);
            case '+': return new Token(TokenType.PLUS, "+", line);
            case '-': return new Token(TokenType.MINUS, "-", line);
            case '<': return new Token(TokenType.LT, "<", line);
            case '>': return new Token(TokenType.GT, ">", line);
            case '#': return new Token(TokenType.HASHTAG, "#", line);
            case '/': return new Token(TokenType.UNKNOWN, "/", line); // Division seule (si ce n'est pas un commentaire)
            
            default:  
                System.err.println("Erreur Lexicale : Caractère inconnu '" + current + "' à la ligne " + line);
                hasError = true;
                return new Token(TokenType.UNKNOWN, String.valueOf(current), line);
        }
    }

    private char peek() { return (pos < length) ? input.charAt(pos) : '\0'; }
    private char peekNext() { 
        if (pos + 1 >= length) return '\0';
        return input.charAt(pos + 1); 
    }
    private char advance() { return (pos < length) ? input.charAt(pos++) : '\0'; }
}