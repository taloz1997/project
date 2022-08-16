package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private int totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private Diary() {
        totalAttacks = 0;
        HanSoloFinish = 0;
        C3POFinish = 0;
        R2D2Deactivate = 0;
        LeiaTerminate = 0;
        HanSoloTerminate = 0;
        C3POTerminate = 0;
        R2D2Terminate = 0;
        LandoTerminate = 0;
    }

    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    public static Diary getInstance() {
        return SingletonHolder.instance;
    }

    public void setTotalAttacks() {
        totalAttacks = totalAttacks + 1;
    }

    public void setLeiaTerminate(long number) {
        LeiaTerminate = number;
    }

    public void setHanSoloTerminate(long number) {
        HanSoloTerminate = number;
    }

    public void setC3POTerminate(long number) {
        C3POTerminate = number;
    }

    public void setR2D2DTerminate(long number) {
        R2D2Terminate = number;
    }

    public void setLandoTerminate(long number) {
        LandoTerminate = number;
    }

    public void setHanSoloFinish(long number) {
        HanSoloFinish = number;
    }

    public void setC3POFinish(long number) {
        this.C3POFinish = number;
    }

    public void setR2D2Deactivate(long number) {
        R2D2Deactivate = number;
    }

    //getters
    public int getNumberOfAttacks() {
        return totalAttacks;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getHanSoloTerminate(){return HanSoloTerminate;}

    public long getC3POTerminate(){return C3POTerminate;}

    public long getLandoTerminate(){return LandoTerminate;}

    public long getR2D2Terminate(){return R2D2Terminate;}

    public void resetNumberAttacks(){
        totalAttacks=0;
    }

}
