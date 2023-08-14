import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Tile extends JLabel {

    private Entity entity;

    public Tile() { // Ustawienia dot. pola
        setPreferredSize(new Dimension(50, 50));
        setOpaque(true);
        setBackground(new Color(255, 255, 255));
        setBorder(new LineBorder(new Color(0,0,0)));
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void update() { // Ustawienia dot. koloru i zmiany miejsc obiektów
        if (entity != null) {
            if (entity instanceof Gardener) {
                setBackground(Color.BLUE);
            } else if (entity instanceof Worm) {
                setBackground(Color.RED);
            } else if (entity instanceof Mouse) {
                setBackground(Color.GRAY);
            } else if (entity instanceof Plant) {
                setBackground(Color.GREEN);
                setText(Integer.toString(((Plant) entity).getHp()));
                setFont(new Font("Arial", 0, 20));
            }
            revalidate();
            repaint();
        }
    }
}