package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.bean.copier.CopyOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link BeanUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("回归测试")
class Issue3702Test {
    /**
     * 验证字段值编辑器将空串转为 null 后再忽略 null 值拷贝.
     */
    @Test
    @DisplayName("编辑后忽略 null")
    void mapToMapTest() {
        Map<String,String> map= new HashMap<>();
        map.put("a","");
        map.put("b","b");
        map.put("c","c");
        map.put("d","d");

        Map<String,String> map2= new HashMap<>();
        map2.put("a","a1");
        map2.put("b","b1");
        map2.put("c","c1");
        map2.put("d","d1");

        CopyOptions option= CopyOptions.create()
            .setIgnoreNullValue(true)
            .setIgnoreError(true)
            .setFieldValueEditor((name, value)->{
            if(value.equals("")){
                value=null;
            }
            return value;
        });
        BeanUtil.copyProperties(map,map2,option);
        Assertions.assertEquals("{a=a1, b=b, c=c, d=d}", map2.toString());
        System.out.println(map2);
    }
}
