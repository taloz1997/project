package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> messageMap;
    private ConcurrentHashMap<Class<? extends Message>, LinkedList<MicroService>> typeMap;
    private ConcurrentHashMap<Event<?>, Future> futureMap;
    Object lock1;
    Object lock2;


    private MessageBusImpl() {
        messageMap = new ConcurrentHashMap<>();
        typeMap = new ConcurrentHashMap<>();
        futureMap = new ConcurrentHashMap<>();
        lock1 = new Object();
        lock2 = new Object();
    }

    private static class SingeltonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance() {
        return SingeltonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        subscribe(type, m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        subscribe(type, m);
    }

    private void subscribe(Class<? extends Message> type, MicroService m) {
        synchronized (type) {
            typeMap.putIfAbsent(type, new LinkedList<MicroService>());//if "type" had not initialized in the hashMap typeMap, add it to the map
            typeMap.get(type).addFirst(m);//update the list of "type"
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        if (futureMap.get(e) != null)
            futureMap.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        synchronized (lock1) {
            if (typeMap.containsKey(b.getClass())) {
                if (!typeMap.get(b.getClass()).isEmpty()) {
                    LinkedList<MicroService> l = typeMap.get(b.getClass());
                    if (l != null && !l.isEmpty())
                        for (int i = 0; i < l.size(); i++)   //add Broadcast b to all the subscribed microservices'.
                            if (messageMap.containsKey(l.get(i)))
                                messageMap.get(l.get(i)).add(b); //add Broadcast b to m's messageQueue.

                }
            }
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (lock2) {
            if (typeMap.containsKey(e.getClass())) {
                MicroService toSend = null;
                if (!typeMap.get(e.getClass()).isEmpty()) {
                    LinkedList<MicroService> l = typeMap.get(e.getClass());
                    toSend = l.removeLast();  //the first subscribed Microservice.
                    l.addFirst(toSend);
                    typeMap.replace(e.getClass(), l);  //to keep Round-Robin manner.
                    if (toSend != null && messageMap.containsKey(toSend)) {
                        Future<T> f = new Future<>();
                        futureMap.put(e, f); //add Future object to futureMap.
                        messageMap.get(toSend).add(e);   //add the event to toSend's messageQueue.
                        return f;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void register(MicroService m) {
        messageMap.putIfAbsent(m, new LinkedBlockingQueue<Message>());

    }

    @Override
    public void unregister(MicroService m) {
        synchronized (lock2) {
            synchronized (lock1) {
                for (LinkedList<MicroService> ms : typeMap.values()) {//remove one by one
                    for (int i = 0; i < ms.size(); i++)
                        if (ms.get(i).equals(m)) {
                            ms.remove(m);
                        }
                }

                for (int i = 0; i < messageMap.get(m).size(); i++) {//remove one by one
                    messageMap.get(m).remove();
                }
                messageMap.remove(m);
            }
        }
    }


    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        if(messageMap.containsKey(m)) {
            try {
                return (messageMap.get(m)).take();
            } catch (InterruptedException ie) {
            }
        }
        return null;
    }
}