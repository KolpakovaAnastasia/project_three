import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

//приложение
public class Main extends Application {
    Field field;
    private Stage stage;
    private Timer timer = new Timer(); // GAME_TIMER: таймер для обновления дисплея игровогоТаймера
    private GameTimer gameTimer = new GameTimer(); // GAME_TIMER: игровой таймер (вычисления)
    private Text timerDisplay = new Text(350, 658 + 25, "00:00:00"); // GAME_TIMER: дисплей таймера
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
            timer.cancel(); // GAME_TIMER: остановка таймера обновления дисплея игровогоТаймера
            gameTimer.interrupt(); // GAME_TIMER: остановка работы игровогоТаймера
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("BANG!");
            alert.setHeaderText(null);
            alert.setContentText("GAME OVER");
            alert.showAndWait();
        }
        timerDisplay.setFont(new Font(30)); // GAME_TIMER: установка шрифта
        root.getChildren().add(timerDisplay);  // добавление на root Pane

        for (int i = 0; i < field.size; i++) {
            for (int j = 0; j < field.size; j++) {
                Button tile = new Button(field.cells[i][j], size, this);
                root.getChildren().add(tile);
            }
        }
        root.setPrefSize(width, height + 30); // GAME_TIMER: +30px для таймера
        return root;
    }

    //создание поля
    private Parent CreateField(int width, int height, int size) {
        gameTimer.setDaemon(true); // GAME_TIMER: поток игровогоТаймера будет закрываться вместе с главным потоком
        field = new Field(size, bombs);
        Pane root = (Pane) drawField(field, width, height);
        TimerTask task = new TimerTask() { // GAME_TIMER: создание задачи обновления дисплея игровогоТаймера
            @Override
            public void run() {
                timerDisplay.setText(gameTimer.getTimeString()); // GAME_TIMER: обновление текста дисплея
            }
        };

        // GAME_TIMER: запуск таймера обновления дисплея
        // ждёт 1 сек (delay=1000) восле запуска - "нулевая секунда"
        // далее каждую сек. (period=1000) выполняет задачу task
        timer.schedule(task,1000,1000);

        gameTimer.start(); // GAME_TIMER: запуск игрового таймера
        root = root;
        root.setPrefSize(width, height + 30); // GAME_TIMER: + 30px для таймера
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
