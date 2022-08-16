package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private Future<?>[] futureAttacks;
    private Future<?> deactivationFuture;
    private Future<?> bombFuture;

    private Diary diary = Diary.getInstance();

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futureAttacks = new Future[attacks.length];
    }

    @Override
    protected void initialize() {
        AttackEvent attack;
        Future<?> f;

        subscribeBroadcast(TerminationBroadcast.class,terminationBroadcast -> {
            terminate();
            diary.setLeiaTerminate(System.currentTimeMillis()); //update in the diary
        });

        for (int i = 0; i < attacks.length; i++) { //send all the attack events
            attack = new AttackEvent(attacks[i]);
            f = sendEvent(attack);
            futureAttacks[i] = f; //update the future array
        }

        for (int i = 0; i < futureAttacks.length; i++) {
            futureAttacks[i].get();//means that futureAttaks[i] is done.
        }

        //R2D2 acts:
        deactivationFuture = sendEvent(new DeactivationEvent());
        deactivationFuture.get(); //means that deactivationFuture is done.

        //Lando acts:
        bombFuture = sendEvent(new BombDestroyerEvent());
        bombFuture.get(); //means that bombFuture is done.

        sendBroadcast(new TerminationBroadcast());
    }
}

