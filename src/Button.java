import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static java.lang.Math.sqrt;

//класс описывающий кнопку
public class Button extends StackPane {
    private Cell cell;
    private Color opened = Color.GRAY;
    private Color closed = Color.BLUE;
    private Color marked = Color.PURPLE;
    private Color bomb = Color.RED;
    private Main main;

    //конструктор, который создает кнопку по указанным параметрам
    Button(Cell cell, int size, Main main) {
        this.main = main;
        this.cell = cell;
        double sideSize = size / sqrt(3);
        Polygon btn = new Polygon
                (
                        0, (size - sideSize) / 2,
                        size / 2, 0,
                        size, (size - sideSize) / 2,
                        size, (size - sideSize) / 2 + sideSize,
                        size / 2, size,
                        0, (size - sideSize) / 2 + sideSize
                );
        btn.setStroke(Color.BLACK);
        if (cell.isBomb() && cell.getState() == CellState.opened)
            btn.setFill(bomb);
        else
            btn.setFill(getColor(cell.getState()));

        Text text = new Text();
        text.setFont(Font.font(18));
        text.setFill(Color.BLACK);

        if (cell.getState() == CellState.opened) {
            if (cell.isBomb())
                text.setText("Bomb");
            else if (cell.getBombsNear() > 0)
                text.setText(Integer.toString(cell.getBombsNear()));
            else
                text.setText("");
            text.setVisible(true);
        }

        getChildren().addAll(btn, text);

        double curX;
        if (cell.getY() % 2 != 0)
            curX = cell.getX() + 0.5;
        else
            curX = cell.getX();

        setTranslateX(curX * size);
        setTranslateY(cell.getY() * 0.8 * size);

        setOnMousePressed(e -> {
            open(e.isSecondaryButtonDown());
            main.field.select(cell);
            main.updateField(main.field);
        });
    }

    private Color getColor(CellState state) {
        switch (state) {
            case opened:
                return opened;
            case closed:
                return closed;
            case marked:
                return marked;
            default:
                return closed;
        }
    }

    private void open(boolean secondary) {
        if (secondary) {
            cell.setState(CellState.marked);
        }
        main.field.cells[cell.getX()][cell.getY()] = cell;
    }
}
