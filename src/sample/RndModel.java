package sample;

import java.io.*;
import java.util.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by karen on 17.11.2016.
 */
public class RndModel {
    public interface ProgressSave {
        void updateProgress(double progress);
    }

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private ThreadLocalRandom rnd = ThreadLocalRandom.current();
    private HashMap<String, Double> map = new HashMap<>();

    // Добавить новое случаное значение
    public boolean addItem() {
        boolean successGenKey = false; // Успешна ли генерация ключа
        int limit = 100; // Количество попыток генерации ключа

        // Значение
        double valuePart = rnd.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);

        // Ключ
        String keyPart = "";

        while (limit-- > 0) {

            // 64-битное целое число от 0 до Long.MAX_VALUE
            long firstPart = rnd.nextLong(0, Long.MAX_VALUE);

            // Строка из 5ти символов
            char[] charsOfString = new char[5];
            for (int i = 0; i < 5; i++) {
                charsOfString[i] = ALPHABET.charAt(rnd.nextInt(0, ALPHABET.length()));
            }
            String secondPart = new String(charsOfString);

            keyPart = firstPart + secondPart;

            if (!map.containsKey(keyPart)) {
                successGenKey = true;
                break;
            }
        }

        // Если не удалось найти уникальное значение ключа
        if (!successGenKey)
            return false;

        map.put(keyPart, valuePart);
        return true;
    }

    // УДалить случайное значение
    public void removeItem() {
        if (!map.isEmpty()) {
            Iterator<Map.Entry<String, Double>> it = map.entrySet().iterator();
            int index = rnd.nextInt(0, map.size());
            int currentIndex = 0;

            while (it.hasNext()) {
                it.next();
                if (index == currentIndex) {
                    it.remove();
                    break;
                }
                currentIndex++;
            }
        }
    }

    public HashMap<String, Double> getMap() {
        return map;
    }

    public void saveToFile(ProgressSave progressSave) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
        oos.close();

        FileOutputStream fos = new FileOutputStream(new File("data.db"));
        try {
            byte[] bytesToSave = baos.toByteArray();
            int chunkSize = map.size() > 0 ? bytesToSave.length / map.size() : bytesToSave.length;
            int offset = 0;
            while (offset < bytesToSave.length) {
                int len = Math.min(chunkSize, bytesToSave.length - offset);
                fos.write(bytesToSave, offset, len);
                offset += chunkSize;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
                double progress = Math.min(offset * 100 / bytesToSave.length, 100) / 100.0;
                progressSave.updateProgress(progress);
            }
        } catch (Throwable e){
            e.printStackTrace();
        }
        fos.close();
    }
}
