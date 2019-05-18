package com.example.echo;
import com.example.echo.DTO.Algorithm;
import com.example.echo.DTO.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
@RunWith(SpringRunner.class)
@SpringBootTest

public class IsInBestClearBestTest {
    @Test
    public void testMetoda() throws Exception {
        Schedule sh = new Schedule(2,2,2,2);
        Algorithm temp = new Algorithm(2, 2, 2, sh);
        Boolean provjera = temp.IsInBest(1);
        assertTrue(provjera == true);
    }
}