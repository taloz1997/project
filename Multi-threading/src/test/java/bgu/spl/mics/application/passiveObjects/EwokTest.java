package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    private Ewok ewok;

    @BeforeEach
    void setUp() {
        ewok = new Ewok(1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void acquire() {
        assertTrue(ewok.available);
        ewok.acquire();
        assertFalse(ewok.available);
    }

    @Test
    void release() {
        if (!ewok.available) {
            ewok.release();
        }
        assertTrue(ewok.available);
    }

}