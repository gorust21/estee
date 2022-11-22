package com.gaia.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.jettison.json.JSONObject;

import com.aliyuncs.utils.StringUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CommonUtil {

    public static boolean initialized = false;

    public static final String KEY_NAME = "AES";
    // 加解密算法/模式/填充方式
    // ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个iv
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";


    private static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'Z' };

    public static Integer getMemberType(Integer type) {
        return null;
    }

    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte)
            throws InvalidAlgorithmParameterException {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            log.info("1:" + e.getMessage());
        } catch (NoSuchPaddingException e) {
            log.info("2:" + e.getMessage());
        } catch (InvalidKeyException e) {
            log.info("3:" + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            log.info("4:" + e.getMessage());
        } catch (BadPaddingException e) {
            log.info("5:" + e.getMessage());
        } catch (NoSuchProviderException e) {
            log.info("6:" + e.getMessage());
        } catch (Exception e) {
            log.info("7:" + e.getMessage());
        }
        return null;
    }

    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);

            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = new JSONObject(buffer.toString());
        } catch (ConnectException ce) {
            log.info("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    /**
     * 去尾部空格
     *
     * @param value
     * @return
     */
    public static String trimLast(String value) {
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return (len < value.length()) ? value.substring(st, len) : value;
    }

    /**
     * 获取订单编号
     *
     * @param device  设备  1:ios;2:android
     * @param payType 支付方式    1:支付宝;2微信;3苹果
     * @param channel 支付渠道    1会员；2空间
     * @return
     */
    public static String getOrderId(int device, int payType, int channel) {
        String str = "" + device + payType + channel + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS")) + (new Random().nextInt(100000) + 10000);
        return str;
    }


    //获取本地ip拼接音視頻url
    public static String getIp() {
        InetAddress ia = null;
        String localip = "";
        try {
            ia = ia.getLocalHost();
            String localname = ia.getHostName();
            localip = ia.getHostAddress();
            log.info("本机名称是：" + localname);
            log.info("本机的ip是 ：" + localip);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return localip;
    }

    //计算年龄
    public static int getAge(String birthString) throws Exception {
        log.info("生日： " + birthString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = sdf.parse(birthString);
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    /**
     * 根据生日字符串计算星座
     *
     * @param birthday
     * @return
     */
    public static String getConstellation(String birthday) {
        //2019-09-06
        log.info("birthday生日： " + birthday);
        int month = Integer.parseInt(birthday.substring(5, 7));
        int day = Integer.parseInt(birthday.substring(8, 10));
        String[] starArr = {"魔羯座", "水瓶座", "双鱼座", "牡羊座",
                "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};
        int[] DayArr = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return starArr[index];
    }

    public static String getInviteCode() {
        String s = "123456789012345678901234567890";
        String str = "qwertyuiopasdfghjklzxcvbnm";
        String upstr = str.toUpperCase();
        String word = str + upstr + s;
        char[] c = word.toCharArray();
        Random rd = new Random();
        String code = "";
        for (int k = 0; k <= 7; k++) {
            int index = rd.nextInt(c.length);//随机获取数组长度作为索引
            code += c[index];//循环添加到字符串后面
        }
        return code;
    }

    public static String getAttestationCode() {
        String s = "123456789012345678901234567890";
        char[] c = s.toCharArray();
        Random rd = new Random();
        String code = "";
        for (int k = 0; k <= 9; k++) {
            int index = rd.nextInt(c.length);//随机获取数组长度作为索引
            code += c[index];//循环添加到字符串后面
        }
        return code;
    }


    public static String defaultUserName(){
        String [] nameArr = {"休思","浮尘","宿醉","安好","凉城","心囚","渡风","光影","南征","晨曦","昭文","问道","不凡",      //13
                "云高风清","酒薄易醒","默语白书","一剑西来","鹤归孤山","烟斜雾横","风起天阑","青山入怀","几盏烟火","人间无味",  //10
                "故巷笑别","心若向阳","星光坠落","皓月长歌","清风煮酒","金笺淡墨","半盏流年","独往归途","一世长安","无问西东",  //10
                "阡南陌北","耿耿于怀","夜未眠","听风夜","千山雾","夜归鹿","关山酒","书斋客","君不见","已惘然","羡明月","终未归","尽余生",//13
                "终不遇","两相知","莫思归","君莫笑","故人归","风清隐","拾荒者","酒自斟","长相思","万骨枯","旧人序","愁千缕",    //13
                "只一人","木头人","歌未央","凡","桑","卿","炽","辰","空","劫","殇","尘","岛","幻","默","泽","陌","宇","羽","轩","轩"}; //20
        Random random = new Random();
        return nameArr[random.nextInt(80)];
    }


    /**
     * 排序
     *
     * @param list
     * @param fieldName 排序的字段
     * @param asc       如果为true，是正序；为false，为倒序
     * @param <T>
     */
/*    public static <T> void sort(List<T> list, String fieldName, boolean asc) {
        Comparator<?> mycmp = ComparableComparator.getInstance();
        mycmp = ComparatorUtils.nullLowComparator(mycmp); // 允许null
        if (!asc) {
            mycmp = ComparatorUtils.reversedComparator(mycmp); // 逆序
        }
        BeanComparator comparator = new BeanComparator(fieldName, mycmp);
        ComparatorUtils.nullLowComparator(comparator);
        Collections.sort(list, comparator);
    }*/


    /**
     * 除法
     */
    public static BigDecimal divide(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big3 = new BigDecimal("0.00");
        if (Double.parseDouble(arg2) != 0) {
            BigDecimal big1 = new BigDecimal(arg1);
            BigDecimal big2 = new BigDecimal(arg2);
            big3 = big1.divide(big2, 2, BigDecimal.ROUND_HALF_EVEN);
        }
        return big3;
    }

    /**
     * 乘法
     */
    public static BigDecimal mul(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.multiply(big2);
        return big3;
    }

    /**
     * 减法
     */
    public static BigDecimal sub(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.subtract(big2);
        return big3;
    }

    /**
     * 加法
     */
    public static BigDecimal add(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.add(big2);
        return big3;
    }

    /**
     * 加法
     */
    public static String add2(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.add(big2);
        return big3.toString();
    }

    /**
     * 四舍五入保留N位小数 先四舍五入在使用double值自动去零
     */
    public static String setScare(BigDecimal arg, int scare) {
        BigDecimal bl = arg.setScale(scare, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bl.doubleValue());
    }

    public static double setDifScare(double arg) {
        BigDecimal bd = new BigDecimal(arg);
        BigDecimal bl = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return Double.parseDouble(bl.toString());
    }

    /**
     * 四舍五入保留两位小数 先四舍五入在使用double值自动去零
     */
    public static String setDifScare(String arg) {
        BigDecimal bd = new BigDecimal(arg);
        BigDecimal bl = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bl.toString();
    }

    /**
     * 四舍五入保留N位小数 先四舍五入在使用double值自动去零（传参String类型）
     */
    public static String setDifScare(String arg, int i) {
        BigDecimal bd = new BigDecimal(arg);
        BigDecimal bl = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
        return bl.toString();
    }

    /**
     * 转化成百分数 先四舍五入在使用double值自动去零
     */
    public static String setFenScare(BigDecimal arg) {
        BigDecimal bl = arg.setScale(3, BigDecimal.ROUND_HALF_UP);
        String scare = String.valueOf(mul(bl.toString(), "100").doubleValue());
        String fenScare = scare + "%";
        return fenScare;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }
}