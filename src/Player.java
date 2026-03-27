import java.awt.*;

// ✅ ใช้ implements Drawable (Interface)
public class Player implements Drawable {
    private int position = 0;
    private int targetPosition = 0;
    private int extraTarget = -1;
    private Color color;
    private String name; // หัวใจของ Overloading

    // --------------------------------------------------
    // ✅ หัวข้อ: Overloading (ชื่อ Constructor เหมือนกันแต่รับค่าต่างกัน)
    // --------------------------------------------------
    public Player(Color color) {
        this.color = color;
        this.name = "Player";
    }

    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public int getPosition() { return position; }
    public Color getColor() { return color; }

    public void setTargetPosition(int steps, Board board) {
        this.targetPosition = this.position + steps;
        if (this.targetPosition > 100) this.targetPosition = 100;
    }

    public boolean hasReachedTarget() {
        return position == targetPosition;
    }

    public void moveStep(Board board) {
        if (position < targetPosition) position++;
    }

    public void applySnakesAndLadders(Board board) {
        if (board.hasSnakeOrLadder(position)) {
            extraTarget = board.getNewPosition(position);
            targetPosition = extraTarget;
        }
    }

    public boolean isMovingExtra() {
        return extraTarget != -1;
    }

    public void moveExtraStep() {
        if (extraTarget == -1) return;
        if (position < extraTarget) position++;
        else if (position > extraTarget) position--;

        if (position == extraTarget) {
            extraTarget = -1;
        }
    }

    // --------------------------------------------------
    // ✅ หัวข้อ: Overriding (เขียนทับ Method จาก Interface Drawable)
    // --------------------------------------------------
    @Override
    public void draw(Graphics g, Board board) {
        Point p = board.getTilePosition(position);
        g.setColor(color);
        g.fillOval(p.x, p.y, 25, 25);
        g.setColor(Color.BLACK);
        g.drawOval(p.x, p.y, 25, 25);
    }
}