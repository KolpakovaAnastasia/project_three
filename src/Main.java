import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//приложение
public class Main extends Application {
    Field field;
    private Stage stage;
    private Text timerDisplay = new Text(350, 658 + 25, "00:00:00"); // "дисплей таймера"
    GameTimer gameTimer = new GameTimer(timerDisplay); // TIMER: игровой таймер (создание)
    private int size = 80,w = 800 + size / 2, h = 658, count = 10, bombs = 100;

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
            gameTimer.interrupt(); // TIMER: остановка таймера
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("BANG!");
            alert.setHeaderText(null);
            alert.setContentText("GAME OVER");
            alert.showAndWait();
        }
        timerDisplay.setFont(new Font(30)); // TIMER: установка шрифта
        root.getChildren().add(timerDisplay);  // добавление на root Pane

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
        gameTimer.setDaemon(true); // TIMER: поток игровогоТаймера будет закрываться вместе с главным потоком
        field = new Field(size, bombs);
        Pane root = (Pane) drawField(field, width, height);
        gameTimer.start(); // TIMER: запуск таймер
        root.setPrefSize(width, height + 30); // TIMER: + 30px для таймера
        return root;
    }

    //создание окна приложения
    private void CreateStage(Stage stage) {
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
