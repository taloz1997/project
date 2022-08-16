package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBusImpl messageBus;
    private MicroService microService1;
    private MicroService microService2;
    private AttackEvent e;
    private Broadcast b;//why broadcast? this is interface

    @BeforeEach
    void setUp() {
        messageBus = MessageBusImpl.getInstance();
        microService1 = new MicroService("") {
            @Override
            protected void initialize() {
            }
        };
        microService2 = new MicroService("") {
            @Override
            protected void initialize() {
            }
        };
        e = new AttackEvent();
        b = new Broadcast() {
        };
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void subscribeEvent() {
        //acts like "sendEvent" Test
        sendEvent();
    }

    @Test
    void subscribeBroadcast() {
        //acts like "sendBroadcast" Test
        sendBroadcast();
    }

    @Test
    void complete() {
        messageBus.register(microService1);
        messageBus.subscribeEvent(e.getClass(), microService1);
        messageBus.sendEvent(e);

        Future<Event> f = microService1.sendEvent((Event) e);
        try {
            messageBus.awaitMessage(microService1);
            microService1.complete((Event) e, "result");
            if (f != null)
                assertEquals("result", f.get());
        } catch (InterruptedException ex) {
        }
        messageBus.unregister(microService1);
    }

    @Test
    void sendBroadcast() {
        messageBus.register(microService1);
        messageBus.subscribeBroadcast(b.getClass(), microService1);
        messageBus.register(microService2);
        messageBus.subscribeBroadcast(b.getClass(), microService2);
        messageBus.sendBroadcast(b);
        try {
            assertEquals(b, messageBus.awaitMessage(microService1));
            assertEquals(b, messageBus.awaitMessage(microService2));
        } catch (InterruptedException ex) {
        }
        messageBus.unregister(microService1);
        messageBus.unregister(microService2);
    }


    @Test
    void sendEvent() {
        messageBus.register(microService1);
        messageBus.subscribeEvent(e.getClass(), microService1);
        messageBus.sendEvent(e);

        try {
            assertEquals(e, messageBus.awaitMessage(microService1));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        messageBus.unregister(microService1);
    }

    @Test
    void register() {
        messageBus.register(microService1);
        messageBus.subscribeEvent(e.getClass(), microService1);
        messageBus.sendEvent(e);
        try {
            assertEquals((Message) e, messageBus.awaitMessage(microService1));
        } catch (InterruptedException ex) {
        }
        messageBus.unregister(microService1);
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
        messageBus.register(microService1);
        messageBus.subscribeEvent(e.getClass(), microService1);
        messageBus.sendEvent(e);
        try {
            Message m = messageBus.awaitMessage(microService1);
        } catch (InterruptedException ex) {
        }
        messageBus.unregister(microService1);
    }
}