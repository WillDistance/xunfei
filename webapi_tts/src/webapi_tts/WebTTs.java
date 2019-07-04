/**
 * 语音合成 WebAPI 接口调用示例 
 * 运行方法：直接运行 main() 即可
 * 结果： 若合成成功，则音频保存在resouce目录，文件名为 sid； 若合成失败，控制台输出错误信息 
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
 * 语音合成 WebAPI 接口调用示例 接口文档（必看）：https://doc.xfyun.cn/msc_android/%E8%AF%AD%E9%9F%B3%E5%90%88%E6%88%90.html
 * webapi 合成服务参考帖子：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=38997&extra=
 * webapi是单次只支持1000个字节，具体看您的编码格式，计算一下具体支持多少文字
 * 合成发音人自动添加获取测试权限使用方法：登陆开放平台https://www.xfyun.cn/后--我的应用（必须为webapi类型应用）--添加在线语音合成（已添加的不用添加）--发音人管理---添加发音人--测试代码里需修改发音人参数 
 *（Very Important）创建完webapi应用添加合成服务之后一定要设置ip白名单，找到控制台--我的应用--设置ip白名单，如何设置参考：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
 * 错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * @author iflytek
 */


public class WebTTs {
    // 合成webapi接口地址
    private static final String WEBTTS_URL = "http://api.xfyun.cn/v1/service/v1/tts";
    // 应用APPID（必须为webapi类型应用，并开通语音合成服务，参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481）
    private static final String APPID = "5d0d9d0a";
    // 接口密钥（webapi类型应用开通合成服务后，控制台--我的应用---语音合成---相应服务的apikey）
    private static final String API_KEY = "7239a136749c6e8a8593d5e9fc98c350";
    // 待合成文本
    private static final String TEXT = "计算一下具体支持多少文字合成发音人自动添加获取测试权限使用方法：登陆开放平台";
    // 音频编码(raw合成的音频格式pcm、wav,lame合成的音频格式MP3)
    private static final String AUE = "lame";
    // 采样率
    private static final String AUF = "audio/L16;rate=16000";
    // 语速（取值范围0-100）
    private static final String SPEED = "50";
    // 音量（取值范围0-100）
    private static final String VOLUME = "50";
    // 音调（取值范围0-100）
    private static final String PITCH = "50";
    // 发音人（登陆开放平台https://www.xfyun.cn/后--我的应用（必须为webapi类型应用）--添加在线语音合成（已添加的不用添加）--发音人管理---添加发音人--修改发音人参数）
    private static final String VOICE_NAME = "xiaoyan";
    // 引擎类型
    private static final String ENGINE_TYPE = "intp65";
    // 文本类型（webapi是单次只支持1000个字节，具体看您的编码格式，计算一下具体支持多少文字）
    private static final String TEXT_TYPE = "text";

    /**
     * 合成 WebAPI 调用示例程序
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(TEXT.length());
        Map<String, String> header = buildHttpHeader();
        
        Map<String, Object> resultMap = HttpUtil.doPost2(WEBTTS_URL, header, "text=" + URLEncoder.encode(TEXT, "utf-8"));
        System.out.println("占用内存大小： "+ URLEncoder.encode(TEXT, "utf-8").getBytes().length);
        if ("audio/mpeg".equals(resultMap.get("Content-Type"))) { // 合成成功
            if ("raw".equals(AUE)) {
                FileUtil.save("resource\\", resultMap.get("sid") + ".wav", (byte[]) resultMap.get("body"));
                System.out.println("合成 WebAPI 调用成功，音频保存位置：resource\\" + resultMap.get("sid") + ".wav");
            } else {
                FileUtil.save("resource\\", "111111.mp3", (byte[]) resultMap.get("body"));
                //FileUtil.save("resource\\", resultMap.get("sid") + ".mp3", (byte[]) resultMap.get("body"));
                System.out.println("合成 WebAPI 调用成功，音频保存位置：resource\\" + resultMap.get("sid") + ".mp3");
            }
        } else { // 合成失败
            System.out.println("合成 WebAPI 调用失败，错误信息：" + resultMap.get("body").toString());//返回code为错误码时，请查询https://www.xfyun.cn/document/error-code解决方案
        }
    }

    /**
     * 组装http请求头
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
