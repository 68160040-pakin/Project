import java.awt.*;
import javax.swing.*;

// ✅ แก้ไขตรงนี้: เพิ่ม extends GameObject (หัวข้อ: Inheritance)
public class Board extends GameObject {
    private Image boardImage;
    private final int BOARD_X = 60, BOARD_Y = 60;
    private final int BOARD_W = 600, BOARD_H = 600;
    private final int TILE_SIZE = 60;

    public Board() {
        java.net.URL imgURL = getClass().getResource("/board.png");
        if (imgURL != null) boardImage = new ImageIcon(imgURL).getImage();
    }

    // ✅ เพิ่ม @Override (หัวข้อ: Overriding)
    // เพราะเราไปเขียนทับเมธอด draw ที่ได้มาจาก GameObject
    @Override
    public void draw(Graphics g) {
        if (boardImage != null) {
            g.drawImage(boardImage, BOARD_X, BOARD_Y, BOARD_W, BOARD_H, null);
        }

        // วาดตารางจางๆ
        g.setColor(new Color(0, 0, 0, 10));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.drawRect(BOARD_X + j * TILE_SIZE, BOARD_Y + i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // --- เมธอดอื่นๆ (getTilePosition, getNewPosition) เหมือนเดิมเป๊ะ ---
    public Point getTilePosition(int tile) {
        if (tile <= 0) tile = 1;
        if (tile > 100) tile = 100;
        int row = (tile - 1) / 10;
        int col = (tile - 1) % 10;
        if (row % 2 == 1) col = 9 - col;

        int x = BOARD_X + (col * TILE_SIZE) + 12;
        int y = (BOARD_Y + BOARD_H) - ((row + 1) * TILE_SIZE) + 15;
        return new Point(x, y);
    }

    public int getNewPosition(int pos) {
        switch(pos) {
            case 19: return 43;  case 5: return 46;  case 11: return 66;
            case 24: return 94; case 31: return 73; case 58: return 82;
            case 47: return 4;  case 34: return 7;  case 65: return 3;
            case 99: return 40; case 97: return 56; case 87: return 22; case 92: return 70;
            default: return pos;
        }
    }

    public boolean hasSnakeOrLadder(int pos) { return getNewPosition(pos) != pos; }
}