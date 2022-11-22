package com.gaia.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: maobc-small_routine
 * @description: app用户登陆
 * @author: z.hw
 **/
@Log4j2
public class WeixinLoginUtils {
    /**
     * 微信登陆通过code获取accessToken
     *
     * @param appId
     * @param userAppSecret
     * @param code
     * @return
     * @throws Exception
     */
    public static StringBuilder getAccessTokenBycode(String appId, String userAppSecret, String code) throws Exception {
        //查看官方文档 https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317853&token=&lang=
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" +
                userAppSecret + "&code=" + code + "&grant_type=authorization_code";
        URI uri = URI.create(url);
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
        StringBuilder sb = new StringBuilder();
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                sb.append(temp);
            }
        }
        return sb;
    }


    /**
     * access_token是否有效的验证
     *
     * @param accessToken
     * @param openID
     * @return
     */
    public static boolean isAccessTokenIsInvalid(String accessToken, String openID) throws Exception {
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openID;
        URI uri = URI.create(url);
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();

            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();

            for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                sb.append(temp);
            }
            JSONObject object = JSONObject.parseObject(sb.toString().trim());
            int errcode = object.getInteger("errcode");
            if (errcode == 0) {
                //未失效
                return true;
            }
        }
        return false;
    }

    /**
     * access_token       接口调用凭证
     * expires_in        access_token接口调用凭证超时时间，单位（秒）
     * refresh_token     用户刷新access_token
     * openid           授权用户唯一标识
     * scope          用户授权的作用域，使用逗号（,）分隔
     *
     * @param APP_ID
     */
    public static JSONObject refreshAccessToken(String APP_ID, String refreshToken) throws Exception {
        /**
         * access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
         *
         * 1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
         *
         * 2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
         *
         * refresh_token拥有较长的有效期（30天）且无法续期，当refresh_token失效的后，需要用户重新授权后才可以继续获取用户头像昵称。
         */
        String uri = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + APP_ID + "&grant_type=refresh_token&refresh_token=" + refreshToken;
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(URI.create(uri));
        HttpResponse response = client.execute(get);
        JSONObject object = new JSONObject();
        if (response.getStatusLine().getStatusCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                builder.append(temp);
            }
            object = JSONObject.parseObject(builder.toString().trim());
        }
        return object;
    }

    /**
     * 得到用户基本信息
     *
     * @param accessToken
     * @param openid
     * @param tClass
     * @return
     * @throws Exception
     */
    public static <T> T getAppWeiXinUserInfo(String accessToken, String openid, Class<T> tClass) throws Exception {
        String uri = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(URI.create(uri));
        HttpResponse response = client.execute(get);

        if (response.getStatusLine().getStatusCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
                log.debug(temp);
                builder.append(temp);
            }
            return JSONObject.parseObject(builder.toString(), tClass);
        }
        return null;
    }

    public static JSONObject getSessionKeyOrOpenId(String code) {
        //微信端登录code
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        //GET https://api.weixin.qq.com/sns/jscode2session
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", "wxe5fd569af24a0ec3");//小程序appId
        requestUrlParam.put("secret", "e1eb3a4d6617191286c1cd4e0ed2a4d7");
        requestUrlParam.put("js_code", wxCode);//小程序端返回的code
        requestUrlParam.put("grant_type", "authorization_code");//默认参数
        //发送post请求读取调用微信接口获取openid用户唯一标识
        //appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
            /*String param = "appid="+"wxe5fd569af24a0ec3"+"&secret="+"e400d7f6f843566b48bb75316ff5c228"+"&js_code="+code+"&grant_type="+"authorization_code";
            log.error(param);
            JSONObject jsonObject = JSON.parseObject(HttpUtils.sendGET(requestUrl, param));*/
        //JSONObject jsonObject = JSON.parseObject(HttpUtils.sendPost(requestUrl, requestUrlParam));
        return null;
    }

    /**
     * 微信小程序获取openid
     *
     * @author Mr.Lin
     */
    // 网页授权接口
//    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";//
//    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";

    public static String oauth2GetOpenid(String code) {
        String requestUrl = GetPageAccessTokenUrl.replace("APPID", "wxe5fd569af24a0ec3").replace("SECRET", "e1eb3a4d6617191286c1cd4e0ed2a4d7").replace("CODE", code);
        HttpClient client = null;
        Map<String, Object> result = new HashMap<String, Object>();
        String response = null;
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = client.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return response;
    }

    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte =  new byte[]{};//= Base64.Decoder.decode(encryptedData.getBytes());
        // 加密秘钥
        byte[] keyByte =  new byte[]{};// = Base64.Decoder.decode(sessionKey.getBytes());
        // 偏移量
        byte[] ivByte  =  new byte[]{};//= Base64.Decoder.decode(iv.getBytes());
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            log.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

/*
    public static Map<String, Object> oauth2GetOpenid(String code) {
        String requestUrl = GetPageAccessTokenUrl.replace("APPID", "wxe5fd569af24a0ec3").replace("SECRET", "e1eb3a4d6617191286c1cd4e0ed2a4d7").replace("CODE", code);
        HttpClient client = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSON.parseObject(response);
            String openid = String.valueOf(OpenidJSONO.get("openid"));
            String session_key = String.valueOf(OpenidJSONO.get("session_key"));
            String unionid = String.valueOf(OpenidJSONO.get("unionid"));
            String errcode = String.valueOf(OpenidJSONO.get("errcode"));
            String errmsg = String.valueOf(OpenidJSONO.get("errmsg"));

            result.put("openid", openid);
            result.put("sessionKey", session_key);
            result.put("unionid", unionid);
            result.put("errcode", errcode);
            result.put("errmsg", errmsg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }*/

}