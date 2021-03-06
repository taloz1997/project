package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {
    private Attack a;

    public AttackEvent(Attack a) {
        this.a = a;
    }

    public AttackEvent() {
    }

    public Attack getAttack() {
        return a;
    }
}
