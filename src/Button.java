import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static java.lang.Math.sqrt;

//класс описывающий кнопку
public class Button extends StackPane {
    Cell cell;
    Color opened = Color.GRAY, closed = Color.BLUE, marked = Color.PURPLE, bomb = Color.RED;
    Main main;

    //конструктор, который создает кнопку по указанным параметрам
    Button(Cell _cell, int size, Main _main) {
        main = _main;
        cell = _cell;
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
        if (cell.isBomb && cell.state == CellState.opened)
            btn.setFill(bomb);
        else
            btn.setFill(GetColor(cell.state));

        Text text = new Text();
        text.setFont(Font.font(18));
        text.setFill(Color.BLACK);

        if (cell.state == CellState.opened) {
            if (cell.isBomb)
                text.setText("Bomb");
            else if (cell.bombsNear > 0)
                text.setText(Integer.toString(cell.bombsNear));
            else
                text.setText("");
            text.setVisible(true);
        }

        getChildren().addAll(btn, text);

        double curX;
        if (cell.y % 2 != 0)
            curX = cell.x + 0.5;
        else
            curX = cell.x;

        setTranslateX(curX * size);
        setTranslateY(cell.y * 0.8 * size);

        setOnMousePressed(e -> {
            Open(e.isSecondaryButtonDown());
            main.field.Select(cell);
            main.UpdateField(main.field);
        });
    }

    Color GetColor(CellState state) {
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

    void Open( boolean secondary) {
        if (secondary) {
            cell.state = CellState.marked;
        }
        main.field.cells[cell.x][cell.y] = cell;
    }
}
