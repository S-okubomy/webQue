package com.app.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.dto.AnsModelDto;
import com.app.dto.ResultQAModelDto;
import com.app.form.IndexForm;
import com.app.service.CreateInfoService;
import com.app.service.WebIndependExeService;
import com.app.service.WebQAExeService;

import org.springframework.stereotype.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@EnableAutoConfiguration
@RequestMapping("/webqa")
public class WebQaSysProtoController 
{
    @Autowired
    WebQAExeService webQAExeService;
    
    @Autowired
    WebIndependExeService webIndependExeService;
    
    @Autowired
    CreateInfoService createInfoService;
    
    @ModelAttribute
    IndexForm setUpIndexForm() {
        return new IndexForm();
    }
    
    
    private static final Logger logger = LoggerFactory.getLogger(WebQaSysProtoController.class);
    
    /**
     * Json テストデータの配列を返却する。
     */
    @RequestMapping(value = "getWebQAJsonData", method = RequestMethod.POST)
    @ResponseBody
    public String[] getWebQAJsonData(Locale locale, @Validated IndexForm indexForm
            , BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("hasErrorFlag", "1");
            return null;
        }
        
        IndexForm index = new IndexForm();
        BeanUtils.copyProperties(indexForm, index);
        
        // ログを出力する
        logger.info("call getWebQAData");
        String[] datas = {"test1", "test2", "test3"};
     
        return datas;
    }
    
    /**
     * テストデータの配列を返却する。
     * @throws Exception 
     */
    @RequestMapping(value = "getWebQAData", method = RequestMethod.POST)
    public String getWebQAData(Locale locale, @Validated IndexForm indexForm
            , BindingResult result, Model model) throws Exception {
        
        if (result.hasErrors()) {
            model.addAttribute("hasErrorFlag", "1");
            return inputArea(locale, model);
        }
        
        // ログを出力する
        logger.info("call getWebQAData");
        
        IndexForm index = new IndexForm();
        BeanUtils.copyProperties(indexForm, index);
        String[] question = new String[1];
        question[0] = index.getInputQueText();
        ResultQAModelDto resultQADto = webQAExeService.getWebQA(question);
        model.addAttribute("resultQADto", resultQADto);
     
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        model.addAttribute("serverTime", formattedDate );
        
        model.addAttribute("hasErrorFlag", "0");

        return "window/anserList";
    }
    
    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Locale locale, IndexForm indexForm, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        
        ResultQAModelDto resultQADto = new ResultQAModelDto();
        model.addAttribute("resultQADto", resultQADto);
        model.addAttribute("serverTime", formattedDate );
        
        return "webQAindex";
    }
    
    @RequestMapping(value = "/indexOld", method = RequestMethod.GET)
    public String indexOld(Locale locale, IndexForm indexForm, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);
        
//        TimeZone tzn = TimeZone.getTimeZone("Asia/Tokyo");
//        SimpleDateFormat fmt = new SimpleDateFormat();
//        fmt.setTimeZone(tzn);
//        Date datet = new Date();
//        fmt.format(datet);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        ResultQAModelDto resultQADto = new ResultQAModelDto();
        model.addAttribute("resultQADto", resultQADto);
        
        return "webQAindexOld";
    }
    
    private String inputArea(Locale locale, Model model) {
        logger.info("Welcome home ERROR! The client locale is {}.", locale);
        
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        
        ResultQAModelDto resultQADto = new ResultQAModelDto();
        model.addAttribute("resultQADto", resultQADto);
        model.addAttribute("serverTime", formattedDate );
        
        return "window/inputArea";
    }
    
    /**
     * 今日のうんちくを表示するための処理.
     * @throws Exception 
     */
    @RequestMapping(value = "/unchiku", method = RequestMethod.GET)
    public String unchiku(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);

        // 書式を変換
        SimpleDateFormat sdf1 = new SimpleDateFormat("M'月'd'日'");
        String today = sdf1.format(dateJp);
        
        String[] question = new String[1];
        question[0] = today + "のうんちくを教えてください。";
        
        List<AnsModelDto> resultAnsList = createInfoService.getCreateInfo(question, "unchiku.txt");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", "今日（" + today + "）" + "のうんちく教えてわん　※日付はサーバー設置場所");

        
        return "infopage1";
    }
    
    /**
     * 健康に過ごすためにはどうしたらいいですか？
     * @throws Exception 
     */
    @RequestMapping(value = "/kenko", method = RequestMethod.GET)
    public String kenko(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        model.addAttribute("serverTime", formattedDate );
        
        String[] question = new String[1];
        question[0] = "健康に過ごすためにはどうしたらいいですか？";
        
        List<AnsModelDto> resultAnsList = createInfoService.getCreateInfo(question, "kenko.txt");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * 人工知能を作るにはどうしたらいいですか？
     * @throws Exception 
     */
    @RequestMapping(value = "/makeAI", method = RequestMethod.GET)
    public String makeAI(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String formattedDate = dateFormat.format(date);
        model.addAttribute("serverTime", formattedDate );
        
        String[] question = new String[1];
        question[0] = "人工知能を作るにはどうしたらいいですか？";
        
        List<AnsModelDto> resultAnsList = createInfoService.getCreateInfo(question, "makeAI.txt");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * 今日のニュースを教えてください。
     * @throws Exception 
     */
    @RequestMapping(value = "/news1", method = RequestMethod.GET)
    public String news1(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        String[] question = new String[1];
        question[0] = "今日のニュースを教えてください。";
        
        List<AnsModelDto> resultAnsList = createInfoService.getCreateInfo(question, "news1.txt");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * お勧めの映画を教えてください。
     * @throws Exception 
     */
    @RequestMapping(value = "/movie1", method = RequestMethod.GET)
    public String movie1(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        String[] question = new String[1];
        question[0] = "お勧めの映画を教えてください。";
        
        List<AnsModelDto> resultAnsList = createInfoService.getCreateInfo(question, "movie1.txt");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * お勧めの映画を教えてください。
     * @throws Exception 
     */
    @RequestMapping(value = "/tv1", method = RequestMethod.GET)
    public String tv1(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        String[] question = new String[1];
        question[0] = "お勧めのテレビ番組を教えてください。";
        
        List<AnsModelDto> resultAnsList = createInfoService.getCreateInfo(question, "tv1.txt");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * 明日の天気（Python 実行）。
     * @throws Exception 
     */
    @RequestMapping(value = "/weather1", method = RequestMethod.GET)
    public String Weather1(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        String[] question = new String[1];
        question[0] = "今日の東京の天気を教えてください。";
        
        List<AnsModelDto> resultAnsList = createInfoService
                .getPyCreateInfo(question, "weather1.txt", "weather1.py");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * メール 送信 明日の天気（Python 実行）。
     * @throws Exception 
     */
    @RequestMapping(value = "/mailWeather1", method = RequestMethod.GET)
    public String mailWeather1(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        String[] question = new String[1];
        question[0] = "今日の東京の天気を教えてください。";
        
        List<AnsModelDto> resultAnsList = createInfoService
                .getPyCreateInfo(question, "weather1.txt", "mailWeather.py");
        model.addAttribute("resultAnsList", resultAnsList);
        
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
    /**
     * お金を稼ぐ方法を教えてください。
     * @throws Exception 
     */
    @RequestMapping(value = "/getMoney", method = RequestMethod.GET)
    public String getMoney(Locale locale, IndexForm indexForm, Model model) throws Exception {
        logger.info("Welcome home! The client locale is {}.", locale);
        
        // 日付型を日本時間にする。
        ZonedDateTime nowZonedDt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        Date dateJp = Date.from(nowZonedDt.toInstant());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String fmtJpDate = fmt.format(dateJp);
        model.addAttribute("serverTime", fmtJpDate);
        
        String[] question = new String[1];
        question[0] = "お金を稼ぐ方法";
        String independAnsKey = "getMoney";
        List<AnsModelDto> resultAnsList = webIndependExeService.getWebQA(question, independAnsKey)
                                              .getResultAnsList();
        model.addAttribute("resultAnsList", resultAnsList);
        model.addAttribute("questionWeb", question[0]);

        return "infopage1";
    }
    
}