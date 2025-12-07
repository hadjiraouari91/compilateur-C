public enum TokenType {
    // Mots-clés du C
    WHILE, IF, ELSE, RETURN, FOR, DO,
    INT, FLOAT, CHAR, VOID, // Types du C (remplace var)
    INCLUDE, // Pour #include (bonus)
    
    // Vos identifiants spéciaux (toujours requis par le TP)
    MON_NOM, MON_PRENOM,

    // Identifiants et valeurs
    IDENTIFIER, NUMBER, STRING,

    // Symboles
    LPAREN, RPAREN,     // ( )
    LBRACE, RBRACE,     // { }
    COLON, SEMICOLON,   // : ;
    COMMA, DOT,             // ,
    EQUALS,            // =
    
    // Comparateurs et Opérateurs
    PLUS, MINUS, LT, GT,
    HASHTAG, // Pour le # de #include

    EOF, UNKNOWN
}