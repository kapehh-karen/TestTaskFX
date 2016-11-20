package sample;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    public interface UIUpdate {
        void updateUI();
    }

    private static final int CONST_SAVE_INTERVAL = 10 * 1000; // 1 sec

    private RndModel model = new RndModel();
    private long lastSaveTime = 0;
    private ArrayList<String> stringOfItems;
    private Object syncObj = new Object();

    // Добавить N чисел
    public void addItems(int count, UIUpdate uiUpdate, ProgressBar progressBar) {
        Runnable runnable = () -> {
            synchronized (syncObj) {
                for (int i = 0; i < count; i++) {
                    model.addItem();
                }
                this.updateListOfItems();
                Platform.runLater(() -> uiUpdate.updateUI());
                this.save(progressBar);
            }
        };
        new Thread(runnable).start();
    }

    public void removeItems(int count, UIUpdate uiUpdate, ProgressBar progressBar) {
        Runnable runnable = () -> {
            synchronized (syncObj) {
                // Если число N больше или равно количеству значений
                if (model.getMap().size() <= count) {
                    model.getMap().clear();
                } else {
                    for (int i = 0; i < count; i++) {
                        model.removeItem();
                    }
                }
                this.updateListOfItems();
                Platform.runLater(() -> uiUpdate.updateUI());
                this.save(progressBar);
            }
        };
        new Thread(runnable).start();
    }

    // Сохранение
    public void save(ProgressBar progressBar) {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastSaveTime) > CONST_SAVE_INTERVAL) {
            try {
                model.saveToFile(progress -> Platform.runLater(() -> progressBar.setProgress(progress)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            lastSaveTime = currentTime;
        }
    }

    private void updateListOfItems() {
        if (stringOfItems != null)
            stringOfItems.clear();

        stringOfItems = new ArrayList<>();
        for (Map.Entry<String, Double> e : model.getMap().entrySet()) {
            stringOfItems.add(String.format("%s -> %g", e.getKey(), e.getValue()));
        }
    }

    public List<String> getListOfItems() {
        return stringOfItems;
    }
}
