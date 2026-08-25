package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.lang.Console;
import cc.hongz.ztools.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

public class TimeIntervalTest {
    @Test
    public void intervalGroupTest(){
        final TimeInterval timer = new TimeInterval();
        timer.start("1");
        ThreadUtil.sleep(800);
        timer.start("2");
        ThreadUtil.sleep(900);


        Console.log("Timer 1 took {} ms", timer.intervalMs("1"));
        Console.log("Timer 2 took {} ms", timer.intervalMs("2"));
    }
}
