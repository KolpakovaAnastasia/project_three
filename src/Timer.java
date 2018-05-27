import javafx.scene.text.Font;
import javafx.scene.text.Text;

// игровой таймер
class Timer extends Thread {
    private int hr = 0, min = 0, sec = 0;
    private Text display = new Text(350, 658 + 25, "00:00:00"); // "дисплей таймера"

    // создание таймера
    Timer() {
        display.setFont(new Font(30)); // размер шрифта дисплея (пиксели);
    }

    // запуск таймера
    public void run() {
        try {
            while (!this.isInterrupted()) {
                if (sec != 59)
                    sec++;
                else if (min != 59) {
                    min++;
                    sec = 0;
                } else {
                    hr++;
                    min = 0;
                    sec = 0;
                }
                sleep(1000);
                update();
            }
        } catch(InterruptedException e) {}
    }

    // обновление дисплея таймера
    private void update() {
        display.setText(getTimeString());
    }

    // формироваиние строки таймера формата "hh:mm:ss"
    public String getTimeString() {
        StringBuilder timestr = new StringBuilder("");
        if(hr < 10) timestr.append("0");
        timestr.append(hr + ":");
        if(min < 10) timestr.append("0");
        timestr.append(min + ":");
        if(sec < 10) timestr.append("0");
        timestr.append(sec);
        return timestr.toString();
    }

    public Text getDisplay() {
        return display;
    }
}
