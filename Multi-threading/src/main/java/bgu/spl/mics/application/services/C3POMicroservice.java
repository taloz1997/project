package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.Collections;
import java.util.List;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    private Ewoks ewoks = Ewoks.getInstance();
    private Diary diary = Diary.getInstance();

    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast terminationBroadcast) -> {
            terminate();
            diary.setC3POTerminate(System.currentTimeMillis());
        });

        subscribeEvent(AttackEvent.class, attackEvent -> {
            List<Integer> listOfEwoks = attackEvent.getAttack().getSerials();

            Collections.sort(listOfEwoks); //sorting the ewoks' list
            Integer serial;

            for (int i = 0; i < listOfEwoks.size(); i++) { //acquire all the needed ewoks
                serial = listOfEwoks.get(i);
                ewoks.isExistsAndAquire(serial);
            }
            int duration = attackEvent.getAttack().getDuration();
            Thread.sleep(duration);
            diary.setC3POFinish(System.currentTimeMillis()); //update in the diary

            for (int i = 0; i < listOfEwoks.size(); i++) { //release all the needed ewoks
                serial = listOfEwoks.get(i);
                ewoks.release(serial);
            }
            this.complete(attackEvent,true);

            diary.setTotalAttacks(); //update in the diary
        });
    }
}
