package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private LinkedList<Ewok> ewokList;

    private Ewoks() {
        ewokList = new LinkedList<>();
    }

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }

    public void isExistsAndAquire(Integer serial) throws InterruptedException {
        ewokList.get(serial-1).acquire();
    }

    public void buildEwokList(int number) {
        for (int i = 1; i <= number; i++) {
            ewokList.addLast(new Ewok(i));
        }
    }

    public void release(Integer serial) {
        ewokList.get(serial-1).release();
    }

}

