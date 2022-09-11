package cn.lijilong.zauth.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {

    /**
     * 时间戳转换指定格式
     * @param time 时间戳
     * @param format 输出格式
     * @return 输出字符串
     */
    public String getDateStrByTime(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        String dateStr = null;
        try {
            date = sdf.parse(sdf.format(time));
            dateStr = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

}
