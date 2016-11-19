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
    private boolean genNewItem() {
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

    private void removeItem() {
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

    // --------------------------------

    public synchronized HashMap<String, Double> getMap() {
        return map;
    }

    // Добавить N чисел
    public synchronized int addItems(int count) {
        int createdItems = 0;

        for (int i = 0; i < count; i++) {
            if (genNewItem())
                createdItems++;
        }

        return createdItems;
    }

    // Удалить N чисел
    public synchronized void removeItems(int count) {

        // Если число N больше или равно количеству значений
        if (map.size() <= count) {
            map.clear();
            return;
        }

        for (int i = 0; i < count; i++) {
            removeItem();
        }
    }

    public synchronized List<String> listOfItems() {
        ArrayList<String> ret = new ArrayList<>();

        for (Map.Entry<String, Double> e : map.entrySet()) {
            ret.add(String.format("%s -> %g", e.getKey(), e.getValue()));
        }

        return ret;
    }
}
