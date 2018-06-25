//класс описывающий клетку
class Cell {
    private int x;
    private int y;

    private int bombsNear;
    private boolean isBomb;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBombsNear() {
        return bombsNear;
    }

    public void setBombsNear(int bombsNear) {
        this.bombsNear = bombsNear;
    }

    boolean isBomb(){
        return isBomb;
    };

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    private CellState state;

    //конструктор для ячейки
    Cell(int x, int y, boolean isBomb) {
        this.x = x;
        this.y = y;
        this.isBomb = isBomb;
    }
}