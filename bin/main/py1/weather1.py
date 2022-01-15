#! python3
# -*- coding: utf-8 -*-

# 天気予報を送信します。


import smtplib
from email.header import Header
from email.mime.text import MIMEText
import json, requests

def sendGmail(tenkiMoji):
    charset = 'iso-2022-jp'

    msg = MIMEText(tenkiMoji, 'plain', charset)
    msg['Subject'] = Header('今日の天気'.encode(charset), charset)

    smtp_obj = smtplib.SMTP('smtp.gmail.com', 587)
    smtp_obj.ehlo()
    smtp_obj.starttls()
    smtp_obj.login('natu6075@gmail.com', 'mo607512')
    smtp_obj.sendmail('natu6075@gmail.com', 'okubo1504@gmail.com', msg.as_string())
    smtp_obj.quit()
    


# メインメソッド。

LOCATION = 'Tokyo,jp' # 場所を設定してください
APPID='de8940f9f25cc75800cd17380cd25ef8' # openweathermap のAPIキーを設定してください

# 天気のデータを取得する
url ='http://api.openweathermap.org/data/2.5/forecast?q={}&cnt=10&appid={}'.format(LOCATION, APPID)
response = requests.get(url)
response.raise_for_status()

weather_data = json.loads(response.text)
w = weather_data['list']

tenkiMoji = ''

for i in range(0,7,1):
    weather1 = w[i]['weather'][0]['main']
    time1 = w[i]['dt_txt']
    tenkiMoji = tenkiMoji + time1 + '   ' + 'Weather: ' + weather1 + '\n'
#    print('時間: ' + time1 + '　' + '天気: ' + weather1)
#    tenkiMoji = tenkiMoji + 'TIME: ' + time1 + '   ' + 'Weather: ' + weather1 + '\n'


print(tenkiMoji)

#sendGmail(tenkiMoji)

