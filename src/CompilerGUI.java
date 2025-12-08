import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class CompilerGUI extends JFrame {

    
    private JTextArea codeEditor;
    private JTable tokenTable;
    private DefaultTableModel tableModel;
    private JTextArea consoleOutput;
    private JButton runButton;

    public CompilerGUI() {
       
        setTitle("Mini-Compilateur C (While) - Ouari Hadjira");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

       
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.7); 

        
        JPanel editorPanel = new JPanel(new BorderLayout());
        JLabel editorLabel = new JLabel(" Éditeur Code C");
        editorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        editorPanel.add(editorLabel, BorderLayout.NORTH);

        codeEditor = new JTextArea();
        codeEditor.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        codeEditor.setText("int main() {\n    int i = 0;\n    while (i < 10) {\n        i = i + 1;\n    }\n}"); 
        editorPanel.add(new JScrollPane(codeEditor), BorderLayout.CENTER);

        
        JPanel lexerPanel = new JPanel(new BorderLayout());
        JLabel lexerLabel = new JLabel(" Analyse Lexicale (Tokens)");
        lexerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        lexerPanel.add(lexerLabel, BorderLayout.NORTH);

        String[] columnNames = {"Lexème", "Token"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tokenTable = new JTable(tableModel);
        tokenTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tokenTable.setRowHeight(20);
        lexerPanel.add(new JScrollPane(tokenTable), BorderLayout.CENTER);

        mainSplitPane.setLeftComponent(editorPanel);
        mainSplitPane.setRightComponent(lexerPanel);

        
        JPanel bottomPanel = new JPanel(new BorderLayout());

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runButton = new JButton("Compiler & Exécuter");
        runButton.setBackground(new Color(144, 238, 144)); 
        runButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JButton clearButton = new JButton("Effacer");
        
        buttonPanel.add(runButton);
        buttonPanel.add(clearButton);

        
        JPanel consolePanel = new JPanel(new BorderLayout());
        JLabel consoleLabel = new JLabel(" Sortie Console (Syntaxe)");
        consoleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        consolePanel.add(consoleLabel, BorderLayout.NORTH);

        consoleOutput = new JTextArea();
        consoleOutput.setEditable(false); 
        consoleOutput.setBackground(new Color(30, 30, 30)); 
        consoleOutput.setForeground(Color.WHITE); 
        consoleOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        consoleOutput.setRows(8);
        consolePanel.add(new JScrollPane(consoleOutput), BorderLayout.CENTER);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(consolePanel, BorderLayout.CENTER);

        
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplit.setTopComponent(mainSplitPane);
        verticalSplit.setBottomComponent(bottomPanel);
        verticalSplit.setResizeWeight(0.7); 

        add(verticalSplit, BorderLayout.CENTER);

        
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runCompiler();
            }
        });

        
        clearButton.addActionListener(e -> {
            codeEditor.setText("");
            tableModel.setRowCount(0);
            consoleOutput.setText("");
        });
    }

    private void runCompiler() {
        
        String sourceCode = codeEditor.getText();

        
        tableModel.setRowCount(0);
        consoleOutput.setText("");

        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        System.setOut(ps);
        System.setErr(ps); 

        try {
            System.out.println("🚀 Démarrage de l'analyse...");
            
            
            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = lexer.tokenize();

            
            for (Token token : tokens) {
                
                if (token.type != TokenType.EOF) {
                    tableModel.addRow(new Object[]{token.value, token.type});
                }
            }

            
            Parser parser = new Parser(tokens);
            parser.parse();

        } catch (Exception ex) {
            System.err.println("Erreur critique : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            
            System.out.flush();
            System.setOut(oldOut);
            System.setErr(oldErr);
            consoleOutput.setText(baos.toString());
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CompilerGUI().setVisible(true);
        });
    }
}