package com.app.dto;

import java.io.Serializable;

public class AnsModelDto implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

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
    
// Hash setでの重複判定のため、オーバライド
// ※注意 ansSentenceのみ同じ内容であれば、同値と判定
    @Override
    public boolean equals(Object obj){

        if (obj==null) return false;

        if (! (obj instanceof AnsModelDto) ) return false;
        if (this==obj) return true;

        AnsModelDto other = (AnsModelDto)obj;
        if(!(ansSentence.replaceAll("　", "").replaceAll(" ", ""))
        		.equals(other.ansSentence.replaceAll("　", "").replaceAll(" ", ""))) return false;
        if(ansSentence.replaceAll("　", "").replaceAll(" ", "")
        		.equals(other.ansSentence.replaceAll("　", "").replaceAll(" ", ""))) return true;
        
        return false;
    }

    @Override
    public int hashCode() {
        final int oddPrime = 31;
        int result = oddPrime;
        result *= oddPrime;
        result += (ansSentence == null) ? 0 : ansSentence.replaceAll("　", "").replaceAll(" ", "").hashCode();
        return result;
    }

}
