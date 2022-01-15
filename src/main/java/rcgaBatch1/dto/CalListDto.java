package rcgaBatch1.dto;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * データベースより求めた統計値を保存するDTO
 * @author t_okubomy
 *
 */
public class CalListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 計算基準日
	 */
	public Timestamp kabuDate;

	/**
	 * 株コード
	 */
	public String kabuCode;

	/**
	 * 銘柄
	 */
	public String kabuName;

	/**
	 * 基準日 終値
	 */
	public double kabuClose;

	/**
	 * 移動平均（20日）
	 */
	public double transAve20;

	/**
	 * 標準偏差（20日）
	 */
	public double sigma20;

	/**
	 * ボリンジャ計算値（上部バンド 売りかも）
	 */
	public double bolCalUp;

	/**
	 * ボリンジャ計算値（下部バンド 買いかも）
	 */
	public double bolCalLow;


	/**
	 * 移動平均乖離率
	 */
	public double kairi;





}
