package com.german.boxtimer;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.german.boxtimer.dao.TimerDao;
import com.german.boxtimer.db.AppDatabase;
import com.german.boxtimer.entity.timer.TimerConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class TimerDaoTest {

    private AppDatabase db;
    private TimerDao dao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        // In memory: nothing is written to disk, and it disappears after each test
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // fine in tests only
                .build();
        dao = db.timerDao();
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void insert_thenReadBack() {
        TimerConfig timer = new TimerConfig();
        timer.setName("Pasta");
        Long durationMillis = 10L * 60L * 1000L;
        timer.setRoundDuration(durationMillis);

        long id = dao.insert(timer);
        TimerConfig loaded = dao.getById(id);
        Long expectedDuration = 600000L;

        assertNotNull(loaded);
        assertEquals("Pasta", loaded.getName());
        assertEquals(expectedDuration, loaded.getRoundDuration());
    }

    @Test
    public void delete_removesTimer() {
        TimerConfig timer = new TimerConfig();
        timer.setName("Temp");
        timer.setRoundDuration(1000L);
        long id = dao.insert(timer);

        TimerConfig saved = dao.getById(id);
        dao.delete(saved);

        assertNull(dao.getById(id));
    }

    @Test
    public void get_timer_byId() {
        TimerConfig timer = new TimerConfig();
        timer.setName("Temp");
        timer.setRoundDuration(1000L);
        long id = dao.insert(timer);
        TimerConfig saved = dao.getById(id);

        assertNotNull(dao.getById(id));
        assertEquals("Temp", saved.getName());
    }
}