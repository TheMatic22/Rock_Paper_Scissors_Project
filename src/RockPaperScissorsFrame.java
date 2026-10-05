import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RockPaperScissorsFrame<Strategy> extends JFrame
{
    private JButton rockButton, paperButton, scissorsButton, quitButton;
    private JTextField playerWinsField, computerWinsField, tiesField;
    private JTextArea resultsArea;

    private int rockCount = 0, paperCount = 0, scissorsCount = 0;
    private String lastPlayerMove = "";
    private int playerWins = 0, computerWins = 0, ties = 0;

    private final Random rnd = new Random();
    public final Cheat cheat = new Cheat();
    private final RandomStrat randomStrat = new RandomStrat();
    private final LeastUsed leastUsed = new LeastUsed();
    private final MostUsed mostUsed = new MostUsed();
    private final LastUsed lastUsed = new LastUsed();
    private String strategyName;

    public RockPaperScissorsFrame()
    {
        setTitle("Rock Paper Scissors Game");
        setLayout(new BorderLayout());

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createStatsPanel(), BorderLayout.CENTER);
        add(createResultsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Choose your move"));

        rockButton = new JButton("Rock", loadIcon("rock.png"));
        paperButton = new JButton("Paper", loadIcon("paper.png"));
        scissorsButton = new JButton("Scissors", loadIcon("scissors.png"));
        quitButton = new JButton("Quit", loadIcon("quit.png"));

        rockButton.setActionCommand("R");
        paperButton.setActionCommand("P");
        scissorsButton.setActionCommand("S");

        ActionListener playListener = new PlayListener();   // single listener for R, P, S
        rockButton.addActionListener(playListener);
        paperButton.addActionListener(playListener);
        scissorsButton.addActionListener(playListener);
        quitButton.addActionListener(e -> System.exit(0));

        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);
        panel.add(quitButton);
        return panel;
    }

    private JPanel createStatsPanel()
    {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Stats"));

        playerWinsField = makeReadOnlyField();
        computerWinsField = makeReadOnlyField();
        tiesField = makeReadOnlyField();

        panel.add(new JLabel("Player Wins:"));   panel.add(playerWinsField);
        panel.add(new JLabel("Computer Wins:")); panel.add(computerWinsField);
        panel.add(new JLabel("Ties:"));          panel.add(tiesField);
        return panel;
    }

    private JPanel createResultsPanel()
    {
        resultsArea = new JTextArea(12, 50);
        resultsArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultsArea); // lets you scroll the whole session

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Results"));
        panel.add(scroll);
        return panel;
    }

    private JTextField makeReadOnlyField()
    {
        JTextField field = new JTextField("0", 5);
        field.setEditable(false);
        return field;
    }

    private ImageIcon loadIcon(String fileName)
    {
        java.net.URL url = getClass().getResource(fileName);
        return (url == null) ? null : new ImageIcon(url);
    }

    private class PlayListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            String playerMove = ae.getActionCommand(); // "R", "P" or "S"

            int roll = rnd.nextInt(100) + 1;
            Strategy strategy = null;
            String strategyName;
            if (roll <= 10)
            {
                strategyName = "Cheat";
            }
            else if (roll <= 30)
            {
                strategyName = "Least Used";
            }
            else if (roll <= 50)
            {
                strategyName = "Most Used";
            }
            else if (roll <= 70)
            {
                strategyName = "Last Used";
            }
            else
            {
                strategyName = "Random";
            }

            String computerMove = "";
            recordPlayerMove(playerMove);
            showResult(playerMove, computerMove, strategyName);
        }
    }

    private void recordPlayerMove(String move)
    {
        switch (move)
        {
            case "R": rockCount++; break;
            case "P": paperCount++; break;
            case "S": scissorsCount++; break;
        }
        lastPlayerMove = move;
    }

    private void showResult(String playerMove, String computerMove, String strategyName)
    {
        String text;
        if (playerMove.equals(computerMove))
        {
            ties++;
            text = name(playerMove) + " equals " + name(computerMove).toLowerCase()
                    + " (Tie! Computer: " + strategyName + ")";
        }
        else if (beats(playerMove, computerMove))
        {
            playerWins++;
            text = describeWin(playerMove, computerMove)
                    + " (Player wins! Computer: " + strategyName + ")";
        }
        else
        {
            computerWins++;
            text = describeWin(computerMove, playerMove)
                    + " (Computer wins! Computer: " + strategyName + ")";
        }

        resultsArea.append(text + "\n");
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    private boolean beats(String a, String b)
    {
        return (a.equals("R") && b.equals("S"))
                || (a.equals("P") && b.equals("R"))
                || (a.equals("S") && b.equals("P"));
    }

    private String describeWin(String winner, String loser)
    {
        String verb = winner.equals("R") ? "breaks" : winner.equals("P") ? "covers" : "cut";
        return name(winner) + " " + verb + " " + name(loser).toLowerCase();
    }

    private String name(String move)
    {
        switch (move)
        {
            case "R": return "Rock";
            case "P": return "Paper";
            default:  return "Scissors";
        }
    }

    private class LeastUsed extends JFrame {
        public String getMove(String playerMove)
        {
            String least = "R";
            int min = rockCount;
            if (paperCount < min)    { least = "P"; min = paperCount; }
            if (scissorsCount < min) { least = "S"; }
            return name(least);
        }
    }

    private class MostUsed extends JFrame {
        public String getMove(String playerMove)
        {
            String most = "R";
            int max = rockCount;
            if (paperCount > max)    { most = "P"; max = paperCount; }
            if (scissorsCount > max) { most = "S"; }
            return name(most);
        }
    }

    private class LastUsed extends JFrame
    {
        public String getMove(String playerMove)
        {
            if (lastPlayerMove.isEmpty())
            {
            }
            return lastPlayerMove;
        }
    }
}

