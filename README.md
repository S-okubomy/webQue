# 質問に回答するWEBアプリ

## 詳細
質問文を入力すると、質問に対する回答を自動で作成します。  
質問文の解析及び回答作成のために機械学習を使用してます。  
まず、質問文を入力すると、質問タイプに分類します。（what,why,how等）  
そして、質問文を検索ワードとして、検索サイト（Google）より、URLを取得します。  
取得したサイト内の文章を解析し、質問タイプに一致する文章を抽出します。  
抽出した文章を回答とします。 また、サイトから画像を取得します。  
画像を取得する処理にEinstein Visionを使用します。  
ラベルで分類しカウント数が上位ラベルの画像をサイト内容を示す画像として抽出します。

## 処理の流れ
質問解析（質問タイプ分類、検索キーワード作成）  
　　　　　　　　　　　↓  
ネット検索（回答作成のための元データ）   
　　　　　　　　　　　↓  
回答文作成（質問タイプに対応する回答パターンを使用）  
　　　　　　　　　　　↓  
画像取得（ラベルで分類し、サイト内容を示す画像を抽出）  
　　　　　　　　　　　↓  
　　　　　ブラウザに回答文と画像表示  

## ツールなど
言語：Java  
自然言語処理：[Java製形態素解析ライブラリ「lucene-gosen」](http://www.mwsoft.jp/programming/munou/lucene_gosen.html)  
 　　　　　　　サポートベクトル、n-gram  
画像取得：Einstein Vision  
ネットワーク接続：[jsoup](http://qiita.com/opengl-8080/items/d4864bbc335d1e99a2d7)

## 現状と今後
現状は、質問タイプ及び回答抽出に自作の機械学習（サポートベクター）を使用しているので、  
処理がすごく遅く、よくリクエストタイムアウトになってしまいます。  
時間の取れる日に直したい と思っています。        

試作画面URL：[https://webque.herokuapp.com/webqa/index](https://webque.herokuapp.com/webqa/index)