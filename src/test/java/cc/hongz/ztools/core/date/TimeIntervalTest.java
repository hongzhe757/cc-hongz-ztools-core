package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.lang.Console;
import cc.hongz.ztools.core.thread.ThreadUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TimeInterval 时间间隔")

public class TimeIntervalTest {
    @Test
    @DisplayName("分组计时")
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
