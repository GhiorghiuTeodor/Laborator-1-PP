import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

class Nod {
    String val;
    Nod left, right;

    public Nod(String value) {
        this.val = value;
        left = right = null;
    }
}

public class Calculator extends JFrame {
    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) ")
    };

    String oper_values[] = {"+", "-", "*", "/", "=", "", "(", ")"};

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(280, 280);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++)
            buttonpanel.add(digits[i]);

        for (int i = 0; i < 8; i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i = 0; i < 8; i++) {
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 5)
                        area.setText("");
                    else if (finalI == 4) {
                        String sir = area.getText();
                        sir = sir.replaceAll(" ", "");
                        try {
                            Nod rad = buildTree(sir, 0, sir.length() - 1);
                            double result = calcul(rad);
                            area.append(" = " + result);
                        } catch (Exception e) {
                            area.append(" !!!Problem!!!");
                        }
                    } else {
                        area.append(oper_values[finalI]);
                    }
                }
            });
        }
    }

    private Nod buildTree(String sir, int start, int end) {
        if (start > end) {
            return null;
        }

        if (sir.charAt(start) == '(' && sir.charAt(end) == ')') { //luat din laboratorul 1 de paoo
            boolean valid = true;
            int contorParanteze = 1;
            for (int i = start + 1; i < end; i++) {
                if (sir.charAt(i) == '(') contorParanteze++;
                else if (sir.charAt(i) == ')') contorParanteze--;
                if (contorParanteze == 0) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return buildTree(sir, start + 1, end - 1);
            }
        }

        int opIndex =opPrincipal(sir, start, end);
        if (opIndex == -1) {
            return new Nod(sir.substring(start, end + 1));
        }

        String op = String.valueOf(sir.charAt(opIndex));
        Nod node = new Nod(op);

        node.left = buildTree(sir, start, opIndex - 1);
        node.right = buildTree(sir, opIndex + 1, end);
        return node;
    }

    private int opPrincipal(String expr, int start, int end) {
        int gradmin = 22;
        int opIndex = -1;
        int contorParanteze = 0;

        for (int i = start; i <= end; i++) {
            char ch = expr.charAt(i);
            if (ch == '(') {
                contorParanteze++;
            } else if (ch == ')') {
                contorParanteze--;
            }
            else if (contorParanteze == 0 && oper(String.valueOf(ch))) {
                int grd = gradPrioritate(ch);
                if (grd <= gradmin) {
                    gradmin = grd;
                    opIndex = i;
                }
            }
        }
        return opIndex;
    }

    public boolean oper(String value) {
        if (value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")) {
            return true;
        }
        return false;
    }

    private int gradPrioritate(char op) {
        if (op == '+' || op == '-') {
            return 1;
        } else if (op == '*' || op == '/') {
            return 2;
        } else {
            return 22;
        }
    }

    public double calcul(Nod nod) {
        if (nod == null) {
            return 0;
        }
        if (!oper(nod.val)) {
            return Double.parseDouble(nod.val);
        }
        double left = calcul(nod.left);
        double right = calcul(nod.right);

        switch (nod.val) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                if (right == 0) {
                    System.out.println("Nu se poate imparti la zero");
                    return Double.POSITIVE_INFINITY;
                }
                return left / right;
            default:
                return 0;
        }
    }
}