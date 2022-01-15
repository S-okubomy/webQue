package rcgaBatch1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日付処理を行うためのUtilクラス
 * @author t_okubomy
 *
 */
public class CaluculateDateUtil {

	/**
	 * インスタンス化禁止のため、コンストラクタをprivateにしている。
	 */
	private CaluculateDateUtil() { }


	/**
	 * 実行日から加算した日付を計算するメソッド
	 * @param dateTerm   加算日
	 * @return 加算された日付
	 */
	public static String caluculateDate(int dateTerm) {

		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'/'MM'/'dd");
		String today = sdf1.format(date);

		/**
		 * 実行した年
		 */
		int year = Integer.parseInt(today.substring(0,4));
		/**
		 * 実行した月（0から始まる）
		 */
		int month = Integer.parseInt(today.substring(5,7)) -1;
		/**
		 * 実行した日
		 */
		int day = Integer.parseInt(today.substring(8,10));

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		cal.add(Calendar.DATE, dateTerm);
		String caluculateDate = sdf1.format(cal.getTime());

		return caluculateDate;

	}

	/**
	 * 実行日から加算した日付を計算するメソッド
	 * @param dateTerm   加算日
	 * @return 加算された日付
	 */
	public static String caluculateDate2(int dateTerm) {

		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
		String today = sdf1.format(date);

		SimpleDateFormat sdfreturn = new SimpleDateFormat("yyyy'年'M'月'd'日'");

		/**
		 * 実行した年
		 */
		int year = Integer.parseInt(today.substring(0,4));
		/**
		 * 実行した月（0から始まる）
		 */
		int month = Integer.parseInt(today.substring(5,7)) -1;
		/**
		 * 実行した日
		 */
		int day = Integer.parseInt(today.substring(8,10));

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		cal.add(Calendar.DATE, dateTerm);
		String caluculateDate = sdfreturn.format(cal.getTime());

		return caluculateDate;

	}

}
