import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private Board board;
    private java.util.List<Player> players;
    private Dice dice;
    private int currentPlayerIndex = 0;
    private int diceRoll = 1;

    private JButton rollDiceBtn;
    private JButton restartBtn;
    private JLabel statusLabel;
    private Timer diceTimer, moveTimer;

    private GameFrame parentFrame;

    private java.util.List<String> diceHistoryText = new ArrayList<>();
    private java.util.List<Color> diceHistoryColor = new ArrayList<>();

    private boolean showSnakeEffect = false;
    private boolean showLadderEffect = false;
    private Timer effectTimer;

    private Image bgImage;

    public GamePanel(int numPlayers, GameFrame frame) {
        this.parentFrame = frame;
        setLayout(null);

        // ✅ 1. เริ่มเล่นเพลงพื้นหลังทันทีเมื่อเข้าหน้าเกม
        SoundManager.playBGM("S1.wav");

        board = new Board();
        dice = new Dice();
        players = new ArrayList<>();

        try {
            java.net.URL bgURL = getClass().getResource("/game_bg.png");
            if (bgURL != null) {
                bgImage = new ImageIcon(bgURL).getImage();
            }
        } catch (Exception e) {
            System.out.println("Error loading background image");
        }

        Color[] colors = {Color.RED, Color.BLUE, new Color(0, 150, 0)};
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(colors[i % colors.length]));
        }

        rollDiceBtn = new JButton("ROLL DICE");
        rollDiceBtn.setBounds(740, 100, 130, 50);
        rollDiceBtn.setBackground(new Color(41, 128, 185));
        rollDiceBtn.setForeground(Color.WHITE);
        rollDiceBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        rollDiceBtn.setFocusPainted(false);
        rollDiceBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        add(rollDiceBtn);

        restartBtn = new JButton("BACK TO MENU");
        restartBtn.setBounds(740, 560, 130, 40);
        restartBtn.setBackground(new Color(231, 76, 60));
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        restartBtn.setFocusPainted(false);
        restartBtn.addActionListener(e -> {
            // ✅ 2. หยุดเพลงเมื่อกลับหน้าเมนู
            SoundManager.stopBGM();
            parentFrame.returnToMenu();
        });
        add(restartBtn);

        statusLabel = new JLabel("Player 1's turn");
        statusLabel.setBounds(730, 50, 200, 30);
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        statusLabel.setForeground(players.get(0).getColor());
        add(statusLabel);

        diceTimer = new Timer(50, e -> {
            diceRoll = (int)(Math.random() * 6) + 1;
            dice.roll(diceRoll);
            repaint();
        });

        rollDiceBtn.addActionListener(e -> {
            if (!diceTimer.isRunning() && (moveTimer == null || !moveTimer.isRunning())) {
                // ✅ 3. เล่นเสียงตอนกดทอยลูกเต๋า
                SoundManager.playSFX("S2.wav");

                rollDiceBtn.setEnabled(false);
                diceTimer.start();
                Timer stopDice = new Timer(1000, ev -> {
                    diceTimer.stop();
                    Player current = players.get(currentPlayerIndex);
                    diceHistoryText.add("P" + (currentPlayerIndex + 1) + " rolled: " + diceRoll);
                    diceHistoryColor.add(current.getColor());
                    if (diceHistoryText.size() > 8) {
                        diceHistoryText.remove(0);
                        diceHistoryColor.remove(0);
                    }
                    startMovingPlayer();
                });
                stopDice.setRepeats(false);
                stopDice.start();
            }
        });
    }

    private void startMovingPlayer() {
        Player current = players.get(currentPlayerIndex);
        current.setTargetPosition(diceRoll, board);
        moveTimer = new Timer(150, e -> {
            if (!current.hasReachedTarget()) {
                current.moveStep(board);
            }
            else if (!current.isMovingExtra()) {
                if (board.hasSnakeOrLadder(current.getPosition())) {
                    int nextPos = board.getNewPosition(current.getPosition());
                    if (nextPos < current.getPosition()) showSnakeEffect = true;
                    else showLadderEffect = true;

                    if (effectTimer != null) effectTimer.stop();
                    effectTimer = new Timer(1200, ev -> {
                        showSnakeEffect = false;
                        showLadderEffect = false;
                        repaint();
                    });
                    effectTimer.setRepeats(false);
                    effectTimer.start();
                    current.applySnakesAndLadders(board);
                } else {
                    finishTurn((Timer)e.getSource());
                }
            }
            if (current.isMovingExtra()) {
                current.moveExtraStep();
                if (!current.isMovingExtra()) {
                    finishTurn((Timer)e.getSource());
                }
            }
            repaint();
        });
        moveTimer.start();
    }

    private void finishTurn(Timer timer) {
        timer.stop();
        if (players.get(currentPlayerIndex).getPosition() >= 100) {
            JOptionPane.showMessageDialog(this, "Player " + (currentPlayerIndex + 1) + " Wins!");
            rollDiceBtn.setEnabled(false);
            return;
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        statusLabel.setText("Player " + (currentPlayerIndex + 1) + "'s turn");
        statusLabel.setForeground(players.get(currentPlayerIndex).getColor());
        rollDiceBtn.setEnabled(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(230, 235, 240));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        board.draw(g);

        for (Player p : players) {
            p.draw(g, board);
        }

        if (dice.getIcon() != null) {
            g.drawImage(dice.getIcon().getImage(), 770, 180, 70, 70, this);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255, 180));
        g2.fillRoundRect(725, 275, 160, 265, 15, 15);

        g2.setFont(new Font("Tahoma", Font.BOLD, 15));
        g2.setColor(Color.BLACK);
        g2.drawString("DICE HISTORY", 750, 300);
        g2.drawLine(745, 305, 865, 305);

        int yPos = 340;
        for (int i = diceHistoryText.size() - 1; i >= 0; i--) {
            g2.setColor(diceHistoryColor.get(i));
            g2.setFont(new Font("Tahoma", Font.BOLD, 16));
            g2.drawString(diceHistoryText.get(i), 755, yPos);
            yPos += 25;
        }

        if (showSnakeEffect) {
            g2.setColor(new Color(255, 0, 0, 100));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Tahoma", Font.BOLD, 70));
            g2.drawString("OH NO! SNAKE!", 200, 350);
        }
        if (showLadderEffect) {
            g2.setColor(new Color(0, 255, 0, 100));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Tahoma", Font.BOLD, 70));
            g2.drawString("WOW! LADDER!", 200, 350);
        }
    }
}