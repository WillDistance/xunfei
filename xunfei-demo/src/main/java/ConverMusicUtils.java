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
    
    //Ѷ�ɿ���ƽ����Ӧ�õ�APPID
    private static final String APPID     = Configuration.getString("XunfeiConfig.appid");
    
    //�ϳ������ļ��洢·��
    private static final String CONVERURL = Configuration.getString("XunfeiConfig.converurl", System.getProperty("user.dir")+"/WebRoot/upload/");
    
    //ִ�гɹ��Ļص�
    private CallbackEntity successFunc = null;
    
    //ִ���쳣�Ļص�
    private CallbackEntity errorFunc = null;
    
    
    //���Է���
    public void execute1(String articleId,DataRow data){
        System.out.println("execute1����ִ�У�"+articleId+" , "+data.getString("articleId"));
    }
    //���Է���
    public void execute2(String articleId,DataRow data){
        System.out.println("execute1����ִ�У�"+articleId+" , "+data.getString("articleId"));
    }
    public static void main(String[] args) throws Exception
    {
        long start = System.currentTimeMillis();
        String content = "���κ����鶼�С��ż�������Ҫ�����������ռ�ʻ���ɣ����β����˵����˽�ҽѧ֪ʶ����ƱͶ��Ҳ��һ����Ϊ����������м�Ǳ��Ͷ����ϵͳ��ȡ��Ʊ����֪ʶ�����Ͷ�������ر��Ƴ���Ͷ���������ֲᣨ��Ʊƪ���������ڴ˻����Ͼ���Ϊ����������300�ʡ�ϵ�����¡���ƪΪ�ڰ�ƪ��һ����������"
                + "�����������г��������һЩ������ɫ��Ʒ��Ϊ�����͸�������ѣ�ȥ������ʲô����ô���Ǿ������������⡣��Ϊһ��Ͷ���ߣ�������ƱҲһ��������µ������׳ɽ�ԭ���������ģ�����Ҫ�����˽�ѧϰ�������ͨ���⼸��С���⣬һ�𿴿��ɡ�"
                + "�𣺣�1�����ַ����ż۸��걨�����걨���뽻������ʱ�����걨���ж��ַ����е����ż۸�Ϊ���걨�۸�Ͷ�����Ը÷�ʽ����ί�У����Զ��ַ����ŵļ۸���ΪͶ���ߵ��걨�۸����磬��Ϊ�򷽣��Ը÷�ʽί������ʱ�����Ե�ʱ�����걨�����������ŵļ۸���Ϊ���������걨�ۣ�ͨ��Ϊ��һ�ļ۸�"
                + "��2���������ż۸��걨�����걨���뽻������ʱ�����걨���б������е����ż۸�Ϊ���걨�۸�Ͷ�����Ը÷�ʽ����ί�У����Ա������ŵļ۸���ΪͶ���ߵ��걨�۸����磬��Ϊ�򷽣��Ը÷�ʽί������ʱ�����Ե�ʱ�����걨���������ŵļ۸���Ϊ���������걨�ۣ�ͨ��Ϊ��һ�ļ۸�"
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
        System.out.println("·����"+str);
        System.out.println("���ݿ�洢·��"+str.substring(str.indexOf("/upload")));
        System.out.println(ConverMusicUtils.getSynthesisResult(str));
        System.out.println("end");    
        long end = System.currentTimeMillis();
        System.out.println("���ȣ�"+content.length());
        System.out.println("��ʱ��"+(end-start));
    }
    
    /**
     * 
     * @��������ʼ�� �ص�����
     * @���ߣ�����
     * @ʱ�䣺2019��6��24�� ����10:10:24
     */
    public void initSynthesisCallback()
    {
        
    }
    
    /**
     * 
     * @��������ʼ�� �ص�����
     * @���ߣ�����
     * @ʱ�䣺2019��6��24�� ����10:10:46
     * @param cla �ص�����������
     * @param methodName �ص�������
     * @param params �ص���������
     * @param parameterTypes �ص�������������
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
     * @���������������ϳ��ļ�����ȡ�ϳɽ����������100��
     * @���ߣ�����
     * @ʱ�䣺2019��6��22�� ����1:45:48
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
                                logger.error("�ϳ������ļ��ɹ�"+file.length());
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
                logger.error("���������ļ���ַΪ��");
            }
        }
        catch (Exception e)
        {
            logger.error("���������ϳɽ�������쳣",e);
        }
        return false;
    }
    
    
    /**
     * 
     * @�����������ϳ�
     * @���ߣ�����
     * @ʱ�䣺2019��6��22�� ����12:47:23
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
                logger.error("����ϳ����������ַ���Ϊ��");
                errorProcessor();
            }
        }
        catch (Exception e)
        {
            logger.error("�ϳ������ļ�ʧ��",e);
            errorProcessor();
        }
        return resultStr;
    }
    
    /**
     * 
     * @�������ϳ��쳣����
     * @���ߣ�����
     * @ʱ�䣺2019��7��10�� ����5:51:34
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
                logger.error("�ı����ºϳ���Ƶ״̬Ϊʧ�ܳ���", e1);
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
     * �ϳ�
     */
    private void Synthesize(String content, String filename)
    {
        //1.����SpeechSynthesizer����
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer();
        //2.�ϳɲ������ã������MSC Reference Manual��SpeechSynthesizer ��
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//���÷�����
        mTts.setParameter(SpeechConstant.SPEED, "50");//�������٣���Χ0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//�����������Χ0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//������������Χ0~100
        //3.��ʼ�ϳ�
        //���úϳ���Ƶ����λ��
        mTts.synthesizeToUri(content, filename, synthesizeToUriListener);
    }
    
    /**
     * �ϳɼ�����
     */
    SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener()
    {
        
        public void onBufferProgress(int progress)
        {
            if(progress%5 == 0)
            {
                logger.info("pcm��Ƶ�ϳɽ���:" + progress+"%");
            }
        }
        
        //�Ự�ϳ���ɻص��ӿ�
        //uriΪ�ϳɱ����ַ��errorΪ������Ϣ��Ϊnullʱ��ʾ�ϳɻỰ�ɹ�
        public void onSynthesizeCompleted(String uri, SpeechError error)
        {
            if ( error == null )
            {
                logger.info("*************�ϳɳɹ�*************");
                logger.info("�ϳ���Ƶ����·����" + uri);
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
                                    //ת���ɹ�����ɾ������Դ�ļ���
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
//                                    logger.error(filename + ".mp3������");
//                                }
//                            }
//                            else
//                            {
//                                logger.error(filename + ".amr������");
//                            }
                        }
                        else
                        {
                            logger.error(filename + ".wav������");
                            errorProcessor();
                        }
                    }
                    else
                    {
                        logger.error(uri+"������");
                        errorProcessor();
                    }
                }
                catch (Exception e)
                {
                    logger.error("pcm��ʽ�ļ�תmp3����");
                    errorProcessor();
                }
            }
            else
            {
                logger.error("*************תpcm��Ƶ���������룺" + error.getErrorCode() + "*************");
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
     * @������pcm ��ʽת wav
     * @���ߣ�����
     * @ʱ�䣺2019��6��22�� ����12:55:33
     * @param sourcePath
     * @param targetPath
     * @throws EncoderException
     */
    static void pcmToWav(String src, String target) throws Exception
    {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);
        
        // ���㳤��
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1)
        {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();
        
        // ��������������ʵȵȡ������õ���16λ������ 8000 hz
        WaveHeader header = new WaveHeader();
        // �����ֶ� = ���ݵĴ�С��PCMSize) + ͷ���ֶεĴ�С(������ǰ��4�ֽڵı�ʶ��RIFF�Լ�fileLength�����4�ֽ�)
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
        
        assert h.length == 44; // WAV��׼��ͷ��Ӧ����44�ֽ�
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
        logger.info("PCMת��Wav���!");
    }
    
    
    /**
     * 
     * @������wav ��ʽת amr
     * @���ߣ�����
     * @ʱ�䣺2019��6��22�� ����12:55:33
     * @param sourcePath
     * @param targetPath
     * @throws EncoderException
     */
    public static void wavToAmr(String sourcePath, String targetPath) throws EncoderException
    {
        File source = new File(sourcePath);
        File target = new File(targetPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libamr_nb"); //�������
        audio.setBitRate(new Integer(12200)); //������
        audio.setChannels(new Integer(1)); //����
        audio.setSamplingRate(new Integer(8000)); //������
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
        logger.info("Wavת��Amr���!");
    }
    
    /**
     * 
     * @������amr ��ʽת mp3
     * @���ߣ�����
     * @ʱ�䣺2019��6��22�� ����12:55:33
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
        logger.info("Amrת��MP3���!");
    }
    
    /**
     * 
     * @����������ļ��Ƿ����
     * @���ߣ�����
     * @ʱ�䣺2019��6��22�� ����12:54:55
     * @param url
     * @return
     */
    public boolean checkFile(String url)
    {
        File file = new File(url);
        return file.exists();
    }
    
}
