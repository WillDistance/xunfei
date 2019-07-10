package com.thinkive.utils;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.AudioUtils;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.codec.EncoderException;
import org.apache.log4j.Logger;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechEvent;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.thinkive.base.config.Configuration;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class ConverMusicUtils
{
    private static Logger       logger    = Logger.getLogger(ConverMusicUtils.class);
    
    //讯飞开放平申请应用的APPID
    private static final String APPID     = Configuration.getString("XunfeiConfig.appid");
    
    //合成语音文件存储路径
    private static final String CONVERURL = Configuration.getString("XunfeiConfig.converurl", System.getProperty("user.dir")+"/WebRoot/upload/");
    
    //执行成功的回调
    private CallbackEntity successFunc = null;
    
    //执行异常的回调
    private CallbackEntity errorFunc = null;
    
    
    //测试方法
    public void execute1(String articleId,DataRow data){
        System.out.println("execute1方法执行："+articleId+" , "+data.getString("articleId"));
    }
    //测试方法
    public void execute2(String articleId,DataRow data){
        System.out.println("execute1方法执行："+articleId+" , "+data.getString("articleId"));
    }
    public static void main(String[] args) throws Exception
    {
        long start = System.currentTimeMillis();
        String content = "做任何事情都有“门槛”：想要开车得先掌握驾驶技巧，想治病救人得先了解医学知识，股票投资也是一样。为帮助初入股市及潜在投资者系统获取股票基础知识，深交所投教中心特别推出《投资者入市手册（股票篇）》，并在此基础上精编为“股市入门300问”系列文章。本篇为第八篇，一起来看看。"
                + "我们在旅行中常常会会买一些当地特色物品作为手信送给亲朋好友，去哪买、买什么、怎么买，是经常遇到的问题。作为一名投资者，买卖股票也一样，如何下单、交易成交原则是怎样的，都需要我们了解学习。下面就通过这几个小问题，一起看看吧。"
                + "答：（1）对手方最优价格申报，以申报进入交易主机时集中申报簿中对手方队列的最优价格为其申报价格。投资者以该方式进行委托，会以对手方最优的价格作为投资者的申报价格。例如，您为买方，以该方式委托买入时，会以当时集中申报薄中卖方最优的价格作为您的买入申报价，通常为卖一的价格。"
                + "（2）本方最优价格申报，以申报进入交易主机时集中申报簿中本方队列的最优价格为其申报价格。投资者以该方式进行委托，会以本方最优的价格作为投资者的申报价格。例如，您为买方，以该方式委托买入时，会以当时集中申报薄中买方最优的价格作为您的买入申报价，通常为买一的价格。"
                +""; 
        String floder = DateHelper.formatDate(new Date(), "yyyyMMdd");
        String filename = DateHelper.formatDateTime(new Date());
        ConverMusicUtils cmu = new ConverMusicUtils();
        DataRow param1 = new DataRow();
        param1.set("articleId","11111111");
        CallbackEntity successF = new CallbackEntity(ConverMusicUtils.class, "execute1", new Object[]{"execute1 article_id",param1}, new Class<?>[]{String.class,DataRow.class});
        DataRow param2 = new DataRow();
        param2.set("articleId","22222222");
        CallbackEntity errorF = new CallbackEntity(ConverMusicUtils.class, "execute2", new Object[]{"execute2 article_id",param2}, new Class<?>[]{String.class,DataRow.class});
        cmu.initSynthesisCallback(successF, errorF);
        //cmu.initSynthesisCallback();
        String str = cmu.XFSpeechSynthesis(content,floder,filename);
        System.out.println("路径："+str);
        System.out.println("数据库存储路径"+str.substring(str.indexOf("/upload")));
        System.out.println(ConverMusicUtils.getSynthesisResult(str));
        System.out.println("end");    
        long end = System.currentTimeMillis();
        System.out.println("长度："+content.length());
        System.out.println("耗时："+(end-start));
    }
    
    /**
     * 
     * @描述：初始化 回调配置
     * @作者：严磊
     * @时间：2019年6月24日 上午10:10:24
     */
    public void initSynthesisCallback()
    {
        
    }
    
    /**
     * 
     * @描述：初始化 回调配置
     * @作者：严磊
     * @时间：2019年6月24日 上午10:10:46
     * @param cla 回调方法所属类
     * @param methodName 回调方法名
     * @param params 回调方法参数
     * @param parameterTypes 回调方法参数类型
     */
    public void initSynthesisCallback(CallbackEntity successFunc,CallbackEntity errorFunc)
    {
        try
        {
            this.successFunc = successFunc;
            this.errorFunc = errorFunc; 
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 
     * @描述：监听语音合成文件，获取合成结果。最多监听100秒
     * @作者：严磊
     * @时间：2019年6月22日 下午1:45:48
     * @param filePath
     * @return
     */
    public static boolean getSynthesisResult(String filePath){
        try
        {
            if(StringHelper.isNotBlank(filePath))
            {
                long i=100;
                do
                {
                    File file = new File(filePath);
                    if(file.exists())
                    {
                        if(file.isFile())
                        {
                            if(file.length()>0)
                            {
                                logger.error("合成语音文件成功"+file.length());
                                return true;
                            }
                        }
                    }
                    Thread.sleep(1000);
                    i--;
                }
                while(i>0);
            }
            else
            {
                logger.error("传入语音文件地址为空");
            }
        }
        catch (Exception e)
        {
            logger.error("监听语音合成结果出现异常",e);
        }
        return false;
    }
    
    
    /**
     * 
     * @描述：语音合成
     * @作者：严磊
     * @时间：2019年6月22日 下午12:47:23
     */
    public String XFSpeechSynthesis(String content,String floder,String filename){
        
        String resultStr = "";
        try
        {
            if(StringHelper.isNotBlank(content)&&StringHelper.isNotBlank(floder)&&StringHelper.isNotBlank(filename))
            {
                resultStr = initTextConverMusic(content,floder,filename);
            }
            else
            {
                logger.error("传入合成语音参数字符串为空");
                errorProcessor();
            }
        }
        catch (Exception e)
        {
            logger.error("合成语音文件失败",e);
            errorProcessor();
        }
        return resultStr;
    }
    
    /**
     * 
     * @描述：合成异常处理
     * @作者：严磊
     * @时间：2019年7月10日 下午5:51:34
     */
    public void errorProcessor(){
        if(errorFunc != null)
        {
            try
            {
                Method method = errorFunc.getMethod();
                Class executeClass = errorFunc.getExecuteClass();
                method.invoke(executeClass.newInstance(), errorFunc.getMethodParams());
            }
            catch (Exception e1)
            {
                logger.error("改变文章合成音频状态为失败出错", e1);
            }
        }
    }
    
    public String initTextConverMusic(String content,String floder,String filename) throws Exception
    {
        SpeechUtility.createUtility("appid=" + APPID);
        floder = CONVERURL +"/"+ floder;
        File file = new File(floder);
        if ( !file.exists() )
        {
            file.mkdir();
        }
        String filePath = floder + "/" + filename + ".pcm";
        content = StringHelper.clearHtml(content);
        Synthesize(content, filePath);
        return floder + "/" + filename + ".wav";
    }
    
    
    public String modifyTextConverMusic(String content,String file) throws Exception
    {
        content = StringHelper.clearHtml(content);
        Synthesize(content, file);
        return file;
    }
    
    /**
     * 合成
     */
    private void Synthesize(String content, String filename)
    {
        //1.创建SpeechSynthesizer对象
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer();
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100
        //3.开始合成
        //设置合成音频保存位置
        mTts.synthesizeToUri(content, filename, synthesizeToUriListener);
    }
    
    /**
     * 合成监听器
     */
    SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener()
    {
        
        public void onBufferProgress(int progress)
        {
            if(progress%5 == 0)
            {
                logger.info("pcm音频合成进度:" + progress+"%");
            }
        }
        
        //会话合成完成回调接口
        //uri为合成保存地址，error为错误信息，为null时表示合成会话成功
        public void onSynthesizeCompleted(String uri, SpeechError error)
        {
            if ( error == null )
            {
                logger.info("*************合成成功*************");
                logger.info("合成音频生成路径：" + uri);
                try
                {
                    String filename = uri.substring(0, uri.indexOf(".pcm"));
                    String[] formats = { ".pcm", ".wav", ".amr", ".mp3" };
                    if ( checkFile(uri) )
                    {
                        pcmToWav(uri, filename + ".wav");
                        if ( checkFile(filename + ".wav") )
                        {
//                            wavToAmr(filename + ".wav", filename + ".amr");
//                            if ( checkFile(filename + ".amr") )
//                            {
//                                changeToMp3(filename + ".wav", filename + ".mp3");
//                                if ( checkFile(filename + ".mp3") )
//                                {
                                    //转换成功，则删除其他源文件。
                                    for (int i = 0; i < 1; i++)
                                    {
                                        File file = new File(filename + formats[i]);
                                        if ( file.exists() )
                                        {
                                            file.delete();
                                        }
                                    }
                                    if(successFunc != null)
                                    {
                                        Method method = successFunc.getMethod();
                                        Class executeClass = successFunc.getExecuteClass();
                                        method.invoke(executeClass.newInstance(), successFunc.getMethodParams());
                                    }
//                                }
//                                else
//                                {
//                                    logger.error(filename + ".mp3不存在");
//                                }
//                            }
//                            else
//                            {
//                                logger.error(filename + ".amr不存在");
//                            }
                        }
                        else
                        {
                            logger.error(filename + ".wav不存在");
                            errorProcessor();
                        }
                    }
                    else
                    {
                        logger.error(uri+"不存在");
                        errorProcessor();
                    }
                }
                catch (Exception e)
                {
                    logger.error("pcm格式文件转mp3出错");
                    errorProcessor();
                }
            }
            else
            {
                logger.error("*************转pcm音频出错，错误码：" + error.getErrorCode() + "*************");
                errorProcessor();
            }
                
        }
        
        
        public void onEvent(int eventType, int arg1, int arg2, int arg3, Object obj1, Object obj2)
        {
            if ( SpeechEvent.EVENT_TTS_BUFFER == eventType )
            {
                logger.info("onEvent: type=" + eventType + ", arg1=" + arg1 + ", arg2=" + arg2 + ", arg3=" + arg3
                        + ", obj2=" + (String) obj2);
                ArrayList<?> bufs = null;
                if ( obj1 instanceof ArrayList<?> )
                {
                    bufs = (ArrayList<?>) obj1;
                }
                else
                {
                    logger.info("onEvent error obj1 is not ArrayList !");
                } //end of if-else instance of ArrayList
                
                if ( null != bufs )
                {
                    for (final Object obj : bufs)
                    {
                        if ( obj instanceof byte[] )
                        {
                            final byte[] buf = (byte[]) obj;
                            logger.info("onEvent buf length: " + buf.length);
                        }
                        else
                        {
                            logger.info("onEvent error element is not byte[] !");
                        }
                    }
                }
            }
        }
        
        
    };
    
    /**
     * 
     * @描述：pcm 格式转 wav
     * @作者：严磊
     * @时间：2019年6月22日 下午12:55:33
     * @param sourcePath
     * @param targetPath
     * @throws EncoderException
     */
    static void pcmToWav(String src, String target) throws Exception
    {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);
        
        // 计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1)
        {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();
        
        // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        // 长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 1;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 16000;
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;
        
        byte[] h = header.getHeader();
        
        assert h.length == 44; // WAV标准，头部应该是44字节
        // write header
        fos.write(h, 0, h.length);
        // write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1)
        {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        logger.info("PCM转换Wav完毕!");
    }
    
    
    /**
     * 
     * @描述：wav 格式转 amr
     * @作者：严磊
     * @时间：2019年6月22日 下午12:55:33
     * @param sourcePath
     * @param targetPath
     * @throws EncoderException
     */
    public static void wavToAmr(String sourcePath, String targetPath) throws EncoderException
    {
        File source = new File(sourcePath);
        File target = new File(targetPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libamr_nb"); //编解码器
        audio.setBitRate(new Integer(12200)); //比特率
        audio.setChannels(new Integer(1)); //渠道
        audio.setSamplingRate(new Integer(8000)); //采样率
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("amr");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (InputFormatException e)
        {
            e.printStackTrace();
        }
        catch (it.sauronsoftware.jave.EncoderException e)
        {
            e.printStackTrace();
        }
        logger.info("Wav转换Amr完毕!");
    }
    
    /**
     * 
     * @描述：amr 格式转 mp3
     * @作者：严磊
     * @时间：2019年6月22日 下午12:55:33
     * @param sourcePath
     * @param targetPath
     * @throws EncoderException
     */
    public static void changeToMp3(String sourcePath, String targetPath) throws EncoderException
    {
        File source = new File(sourcePath);
        File target = new File(targetPath);
        //AudioUtils.amrToMp3(source, target);
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        audio.setCodec("libmp3lame");
//        audio.setBitRate(new Integer(36000));
//        audio.setChannels(new Integer(2));
//        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (InputFormatException e)
        {
            e.printStackTrace();
        }
        catch (it.sauronsoftware.jave.EncoderException e)
        {
            e.printStackTrace();
        }
        logger.info("Amr转换MP3完毕!");
    }
    
    /**
     * 
     * @描述：检查文件是否存在
     * @作者：严磊
     * @时间：2019年6月22日 下午12:54:55
     * @param url
     * @return
     */
    public boolean checkFile(String url)
    {
        File file = new File(url);
        return file.exists();
    }
    
}
