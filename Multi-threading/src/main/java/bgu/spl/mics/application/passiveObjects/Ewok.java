package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
    int serialNumber;
    boolean available;

    public Ewok(int serial) {
        this.serialNumber = serial;
        available = true;
    }

    /**
     * Acquires an Ewok
     */
    public void acquire() {
        synchronized (this) {
            while (!available) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            available = false;
        }
    }

    /**
     * release an Ewok
     */
    public void release() {
        synchronized (this) {
            if (!available) {
                available = true;
                notifyAll();
            }
        }
    }
}
