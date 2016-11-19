package sample;

import java.util.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by karen on 17.11.2016.
 */
public class RndModel {
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

    public void saveToFile() {
        System.out.println("SAVED");
    }
}
