package com.app.dto;

import java.io.Serializable;

public class AnsModelDto implements Serializable {

	public AnsModelDto() {
	}

	/** 応答No */
	private String ansNo;

	/** 判定情報 正解:T */
	private String hanteiJoho;

	/** 回答分類 */
	private String ansBunrui;

	/** 応答文章 */
	private String ansSentence;
	
	/** HTMLパス */
    private String htmlPath;
	
	/** 決定関数値 */
    private double fxValue;

	public void setAnsNo(String ansNo){
        this.ansNo = ansNo;
    }

	public void setHanteiJoho(String hanteiJoho){
        this.hanteiJoho = hanteiJoho;
    }

	public void setAnsBunrui(String ansBunrui){
        this.ansBunrui = ansBunrui;
    }

	public void setAnsSentence(String ansSentence){
        this.ansSentence = ansSentence;
    }
	
    public void setFxValue(double fxValue){
        this.fxValue = fxValue;
    }
    
    public void setHtmlPath(String htmlPath){
        this.htmlPath = htmlPath;
    }

	public String getAnsNo() {
		return this.ansNo;
	}

	public String getHanteiJoho() {
		return this.hanteiJoho;
	}

	public String getAnsBunrui() {
		return this.ansBunrui;
	}

	public String getAnsSentence() {
		return this.ansSentence;
	}
	
    public double getFxValue() {
        return this.fxValue;
    }
    
    public String getHtmlPath() {
        return this.htmlPath;
    }

}
