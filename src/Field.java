import java.util.ArrayList;
import java.util.List;

//класс описывающий все клетки поля

class Field {
    Cell[][] cells;
    int size;
    boolean failed;
//создает поле

    Field(int size, int bombs) {
        failed = false;
        this.size = size;
        cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = ((int) Math.ceil(Math.random() * 100)) % 3;
                if (value == 1 && bombs > 0) {
                    cells[i][j] = new Cell(i, j, true);
                    bombs--;
                }
                else
                    cells[i][j] = new Cell(i, j, false);
                cells[i][j].setState(CellState.closed);
            }
        }
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                cells[i][j].setBombsNear(getBombsCount(cells[i][j]));
            }
    }

    //проверка координат клетки
    private boolean isCorrect(int value) {
        return value > -1 && value < size;
    }

    //получение соседних клеток, для открытия свободных областей
    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] points = (cell.getY() % 2 != 0) ? new int[]{
                0, -1,
                1, -1,
                -1, 0,
                1, 0,
                0, 1,
                1, 1
        } : new int[]{
                -1, -1,
                0, -1,
                -1, 0,
                1, 0,
                -1, 1,
                0, 1
        };
        for (int i = 0; i < points.length; i++) {
            int dx = points[i], dy = points[++i];
            int newX = cell.getX() + dx, newY = cell.getY() + dy;

            if (isCorrect(newX) && isCorrect(newY))
                neighbors.add(cells[(int) newX][(int) newY]);
        }
        return neighbors;
    }

    //подсчет соседних бомб для конкретной клетки
    private int getBombsCount(Cell cell) {
        int counter = 0;
        List<Cell> neighbours = getNeighbors(cell);
        for (Cell neighbour : neighbours)
            if (neighbour.isBomb())
                counter++;
        return counter;
    }

//обновление поля после нажатия по клетке

    void select(Cell cell) {
        if (cell.getState() == CellState.closed) {
            cell.setState(CellState.opened);
            if (cell.isBomb())
                failed = true;
            cells[cell.getX()][cell.getY()] = cell;
            List<Cell> list = getNeighbors(cell);
            for (Cell aList : list) {
                if (aList.getBombsNear() == 0) {
                    select(aList);
                }
                else if (!aList.isBomb()) {
                    cells[aList.getX()][aList.getY()].setState(CellState.opened);
                }
            }
        }
    }

//открытие всех клеток в конце игры

    void open() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (cells[i][j].getState() == CellState.closed ||
                        cells[i][j].getState() == CellState.marked && !cells[i][j].isBomb())
                    cells[i][j].setState(CellState.opened);
            }
    }
}
