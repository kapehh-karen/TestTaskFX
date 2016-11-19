package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    private static final int CONST_SAVE_INTERVAL = 10 * 1000; // 1 sec

    private RndModel model = new RndModel();
    private long lastSaveTime = 0;

    // Добавить N чисел
    public synchronized int addItems(int count) {
        int createdItems = 0;

        for (int i = 0; i < count; i++) {
            if (model.addItem())
                createdItems++;
        }

        this.save();
        return createdItems;
    }

    public synchronized void removeItems(int count) {
        // Если число N больше или равно количеству значений
        if (model.getMap().size() <= count) {
            model.getMap().clear();
        } else {
            for (int i = 0; i < count; i++) {
                model.removeItem();
            }
        }

        this.save();
    }

    // Сохранение
    public void save() {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastSaveTime) > CONST_SAVE_INTERVAL) {
            model.saveToFile();
            lastSaveTime = currentTime;
        }
    }

    public synchronized List<String> getListOfItems() {
        ArrayList<String> ret = new ArrayList<>();

        for (Map.Entry<String, Double> e : model.getMap().entrySet()) {
            ret.add(String.format("%s -> %g", e.getKey(), e.getValue()));
        }

        return ret;
    }
}
