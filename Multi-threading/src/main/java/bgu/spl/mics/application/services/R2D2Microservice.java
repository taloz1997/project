package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private Diary diary = Diary.getInstance();
    private long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast terminationBroadcast) -> {
            terminate();
            diary.setR2D2DTerminate(System.currentTimeMillis()); //update in the diary
        });

        subscribeEvent(DeactivationEvent.class, deactivationEvent -> {
            Thread.sleep(duration);
            diary.setR2D2Deactivate(System.currentTimeMillis()); //update in the diary
            this.complete(deactivationEvent, true);
        });
    }
}
