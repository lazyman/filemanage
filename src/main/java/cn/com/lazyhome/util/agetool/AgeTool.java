package cn.com.lazyhome.util.agetool;

import java.util.Calendar;

import cn.huxi.tools.util.ChineseCalendar;

public class AgeTool {
	public static  ChineseCalendar darlinBirthday = new ChineseCalendar(1987, 11, 28);
	public static final ChineseCalendar meBirthday = new ChineseCalendar(1984, 2, 26);
	/**
	 * 公历
	 */
	public static final ChineseCalendar meBirthday2 = new ChineseCalendar(false, 1984, 2, 26);
	/**
	 * 非农历日期
	 */
	public static final ChineseCalendar yuejingDate = new ChineseCalendar(false, 2015, 1, 11);
	/**
	 * 造人日期，怀孕日期+10。
	 * 非农历日期
	 */
	public static  ChineseCalendar loveDate = new ChineseCalendar(false, 2015, 3, 27);

	public static void main(String[] args) {
		darlinBirthday = new ChineseCalendar(1961, 07, 25);
		loveDate = new ChineseCalendar(false, 1984, 2, 26);
		loveDate.add(Calendar.MONTH, -10);
		
		morf();
	}
	public static void morf() {
		int age = calcAge(darlinBirthday, loveDate);
		System.out.println(darlinBirthday);
		System.out.println(loveDate);
		System.out.println("当前年龄：" + age);
		
		System.out.println("当前农历：" + loveDate.getSimpleChineseDateString());
		int[] month = MorF.MFarr[age - 22];
		
		System.out.println("1 2 3 4 5 6 7 8 9 101112");
		for(int i=0; i<month.length; i++) {
			System.out.print(month[i] + " ");
		}
	}
	
	public static void me() {
		ChineseCalendar pubCalendar = new ChineseCalendar(1984, 02, 26);
		System.out.println(pubCalendar.toString());
		
		ChineseCalendar chineseCalendar = new ChineseCalendar(true, 1984, 01, 25);
		System.out.println(chineseCalendar.toString());
		
		ChineseCalendar notChineseCalendar = new ChineseCalendar(false, 1984, 2, 26);
		System.out.println(notChineseCalendar.toString());
	}
	
	public static void darlin() {
		ChineseCalendar pubCalendar = new ChineseCalendar(1987, 12, 28);
		System.out.println(pubCalendar.toString());
	}
	
	public static int calcAge(ChineseCalendar birthday) {
		ChineseCalendar now = new ChineseCalendar();
		
		return calcAge(birthday, now);
	}
	
	/**
	 * 从生日到指定日期时的年龄。
	 * 算法：已经忘记
	 * @param birthday 生日
	 * @param date 指定日期
	 * @return 虚岁
	 */
	public static int calcAgeOld(ChineseCalendar birthday, ChineseCalendar date) {
		ChineseCalendar newBirthday = new ChineseCalendar(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DATE));
		newBirthday.add(Calendar.MONTH, 9);
		
		int age = date.get(Calendar.YEAR) - newBirthday.get(Calendar.YEAR);
		newBirthday.add(Calendar.YEAR, age);
		if(date.after(newBirthday)) {
			// 当前日期（date）比生日（晚），说明已经过了生日
			age = age + 1;
		}
		
		return age;
	}
	
	/**
	 * 从生日到指定日期时的年龄。
	 * 算法：
	 * @param birthday 生日
	 * @param date 指定日期
	 * @return 虚岁
	 */
	public static int calcAge(ChineseCalendar birthday, ChineseCalendar date) {
		// birthday
		int byear = birthday.get(Calendar.YEAR);
		int bmonth = birthday.get(Calendar.MONTH);
		int bdate = birthday.get(Calendar.DATE);
		
		// now
		int nyear = date.get(Calendar.YEAR);
		int nmonth = date.get(Calendar.MONTH);
		int ndate = date.get(Calendar.DATE);
		
		int year = nyear - byear;
		int month = nmonth - bmonth + 9;
		int day = ndate - bdate;
		
		if(day < 0) {
			day = day+30;
			month--;
		}
		if(month < 0) {
			month = month + 12;
			year--;
		}
		if(month > 12) {
			month = month - 12;
			year++;
		}
		
		System.out.println(year + "." + month + "." + day);
		
		return year;
	}
}
