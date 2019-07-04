/**
 * �����ϳ� WebAPI �ӿڵ���ʾ�� 
 * ���з�����ֱ������ main() ����
 * ����� ���ϳɳɹ�������Ƶ������resouceĿ¼���ļ���Ϊ sid�� ���ϳ�ʧ�ܣ�����̨���������Ϣ 
 * @author iflytek
 * 
 */
 package webapi_tts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * �����ϳ� WebAPI �ӿڵ���ʾ�� �ӿ��ĵ����ؿ�����https://doc.xfyun.cn/msc_android/%E8%AF%AD%E9%9F%B3%E5%90%88%E6%88%90.html
 * webapi �ϳɷ���ο����ӣ�http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=38997&extra=
 * webapi�ǵ���ֻ֧��1000���ֽڣ����忴���ı����ʽ������һ�¾���֧�ֶ�������
 * �ϳɷ������Զ���ӻ�ȡ����Ȩ��ʹ�÷�������½����ƽ̨https://www.xfyun.cn/��--�ҵ�Ӧ�ã�����Ϊwebapi����Ӧ�ã�--������������ϳɣ�����ӵĲ�����ӣ�--�����˹���---��ӷ�����--���Դ��������޸ķ����˲��� 
 *��Very Important��������webapiӦ����Ӻϳɷ���֮��һ��Ҫ����ip���������ҵ�����̨--�ҵ�Ӧ��--����ip��������������òο���http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
 * ���������ӣ�https://www.xfyun.cn/document/error-code ��code���ش�����ʱ�ؿ���
 * @author iflytek
 */


public class WebTTs {
    // �ϳ�webapi�ӿڵ�ַ
    private static final String WEBTTS_URL = "http://api.xfyun.cn/v1/service/v1/tts";
    // Ӧ��APPID������Ϊwebapi����Ӧ�ã�����ͨ�����ϳɷ��񣬲ο�������δ���һ��webapiӦ�ã�http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481��
    private static final String APPID = "5d0d9d0a";
    // �ӿ���Կ��webapi����Ӧ�ÿ�ͨ�ϳɷ���󣬿���̨--�ҵ�Ӧ��---�����ϳ�---��Ӧ�����apikey��
    private static final String API_KEY = "7239a136749c6e8a8593d5e9fc98c350";
    // ���ϳ��ı�
    private static final String TEXT = "����һ�¾���֧�ֶ������ֺϳɷ������Զ���ӻ�ȡ����Ȩ��ʹ�÷�������½����ƽ̨";
    // ��Ƶ����(raw�ϳɵ���Ƶ��ʽpcm��wav,lame�ϳɵ���Ƶ��ʽMP3)
    private static final String AUE = "lame";
    // ������
    private static final String AUF = "audio/L16;rate=16000";
    // ���٣�ȡֵ��Χ0-100��
    private static final String SPEED = "50";
    // ������ȡֵ��Χ0-100��
    private static final String VOLUME = "50";
    // ������ȡֵ��Χ0-100��
    private static final String PITCH = "50";
    // �����ˣ���½����ƽ̨https://www.xfyun.cn/��--�ҵ�Ӧ�ã�����Ϊwebapi����Ӧ�ã�--������������ϳɣ�����ӵĲ�����ӣ�--�����˹���---��ӷ�����--�޸ķ����˲�����
    private static final String VOICE_NAME = "xiaoyan";
    // ��������
    private static final String ENGINE_TYPE = "intp65";
    // �ı����ͣ�webapi�ǵ���ֻ֧��1000���ֽڣ����忴���ı����ʽ������һ�¾���֧�ֶ������֣�
    private static final String TEXT_TYPE = "text";

    /**
     * �ϳ� WebAPI ����ʾ������
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(TEXT.length());
        Map<String, String> header = buildHttpHeader();
        
        Map<String, Object> resultMap = HttpUtil.doPost2(WEBTTS_URL, header, "text=" + URLEncoder.encode(TEXT, "utf-8"));
        System.out.println("ռ���ڴ��С�� "+ URLEncoder.encode(TEXT, "utf-8").getBytes().length);
        if ("audio/mpeg".equals(resultMap.get("Content-Type"))) { // �ϳɳɹ�
            if ("raw".equals(AUE)) {
                FileUtil.save("resource\\", resultMap.get("sid") + ".wav", (byte[]) resultMap.get("body"));
                System.out.println("�ϳ� WebAPI ���óɹ�����Ƶ����λ�ã�resource\\" + resultMap.get("sid") + ".wav");
            } else {
                FileUtil.save("resource\\", "111111.mp3", (byte[]) resultMap.get("body"));
                //FileUtil.save("resource\\", resultMap.get("sid") + ".mp3", (byte[]) resultMap.get("body"));
                System.out.println("�ϳ� WebAPI ���óɹ�����Ƶ����λ�ã�resource\\" + resultMap.get("sid") + ".mp3");
            }
        } else { // �ϳ�ʧ��
            System.out.println("�ϳ� WebAPI ����ʧ�ܣ�������Ϣ��" + resultMap.get("body").toString());//����codeΪ������ʱ�����ѯhttps://www.xfyun.cn/document/error-code�������
        }
    }

    /**
     * ��װhttp����ͷ
     */
    private static Map<String, String> buildHttpHeader() throws UnsupportedEncodingException {
        String curTime = System.currentTimeMillis() / 1000L + "";
        String param = "{\"auf\":\"" + AUF + "\",\"aue\":\"" + AUE + "\",\"voice_name\":\"" + VOICE_NAME + "\",\"speed\":\"" + SPEED + "\",\"volume\":\"" + VOLUME + "\",\"pitch\":\"" + PITCH + "\",\"engine_type\":\"" + ENGINE_TYPE + "\",\"text_type\":\"" + TEXT_TYPE + "\"}";
        String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
        String checkSum = DigestUtils.md5Hex(API_KEY + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", APPID);
        return header;
    }
}
