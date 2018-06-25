import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

//приложение
public class Main extends Application {
    Field field;
    private Stage stage;
    private Timer timer = new Timer(); // TIMER: игровой таймер (создание)
    private int size = 80;
    private int w = 800 + size / 2;
    private int h = 658;

    //перерисовывает поле
    void updateField(Field field) {
        Scene scene = new Scene(drawField(field, w, h));
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Minesweeper");
        stage.getIcons().add(new Image("cell.png"));
        stage.setResizable(true);
    }

    //рисует поле
    private Parent drawField(Field field, int width, int height) {
        Pane root = new Pane();
        if (field.failed) {
            field.open();
            timer.interrupt(); // TIMER: остановка таймера
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("BANG!");
            alert.setHeaderText(null);
            alert.setContentText("GAME OVER");
            alert.showAndWait();
        }

        root.getChildren().add(timer.getDisplay());

        for (int i = 0; i < field.size; i++) {
            for (int j = 0; j < field.size; j++) {
                Button tile = new Button(field.cells[i][j], size, this);
                root.getChildren().add(tile);
            }
        }
        root.setPrefSize(width, height + 30); // TIMER: +30px для таймера
        return root;
    }

    //создание поля
    private Parent CreateField(int width, int height, int size) {
        timer.setDaemon(true); // TIMER: поток таймера будет закрываться вместе с главным потоком
        int bombs = 100;
        field = new Field(size, bombs);
        Pane root = (Pane) drawField(field, width, height);
        timer.start(); // TIMER: запуск таймера

        root = root;
        root.setPrefSize(width, height + 30); // TIMER: + 30px для таймера
        return root;
    }

    //создание окна приложения
    private void CreateStage(Stage stage) {
        int count = 10;
        Scene scene = new Scene(CreateField(w, h, count));
        this.stage = stage;
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Minesweeper");
        stage.getIcons().add(new Image("cell.png"));
        stage.setResizable(true);
    }

    //запуск игры
    public void start(Stage stage) throws Exception {
        CreateStage(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
