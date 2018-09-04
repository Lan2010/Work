package core.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String nowTimestampToString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String str = df.format(now);
		return str;
	}

	public static Timestamp nowTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date());
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}

	public static String timestampToString(Timestamp ts) {
		if (ts == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(ts);
		return str;
	}

	public static String timestampToStringYYYYMMDDHHMMSS(Timestamp ts) {
		if (ts == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = df.format(ts);
		return str;
	}

	public static void main(String[] args) {
	}
}
