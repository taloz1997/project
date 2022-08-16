package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonInputReader;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.concurrent.CountDownLatch;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static void main(String[] args) {

        Gson g = new Gson();
        Reader r = null;
        try {
            r = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Input input = g.fromJson(r, Input.class);
        Ewoks ewoks = Ewoks.getInstance();
        ewoks.buildEwokList(input.getEwoks());
        Diary diary = Diary.getInstance();
        Thread leiaThread = new Thread(new LeiaMicroservice(input.getAttacks()));
        Thread hanSoloThread = new Thread(new HanSoloMicroservice());
        Thread C3POThread = new Thread(new C3POMicroservice());
        Thread R2D2Thread = new Thread(new R2D2Microservice(input.getR2D2()));
        Thread landoThread = new Thread(new LandoMicroservice(input.getLando()));

        landoThread.start();
        hanSoloThread.start();
        C3POThread.start();
        R2D2Thread.start();

        try {
            leiaThread.sleep(100);
            leiaThread.start();
        } catch (InterruptedException e) {
        }

        try {
            hanSoloThread.join();
            C3POThread.join();
            R2D2Thread.join();
            leiaThread.join();
            landoThread.join();
        } catch (InterruptedException e) {
        }


        //creating output:
        Gson testBuilderJson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fileWriter = new FileWriter(args[1]);

            testBuilderJson.toJson(diary, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (
                Exception fileWriteException) {
            fileWriteException.printStackTrace();
        }
    }
}

