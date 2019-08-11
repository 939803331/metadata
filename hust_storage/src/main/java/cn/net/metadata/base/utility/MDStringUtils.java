package cn.net.metadata.base.utility;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDStringUtils {

    /**
     * @Description: 字符串集合拼成字符串
     * @param: [arg, regex]
     * @return: java.lang.String
     */
    public static String collectionsToString(List<String> arg, String regex) {
        if (arg == null || arg.isEmpty()) return "";
        return arg.stream().reduce((acc, item) -> {
            if (MDStringUtils.isNull(item)) return acc;
            if (MDStringUtils.isNotBlank(acc)) {
                acc = acc + regex + item;
            } else {
                acc = item;
            }
            return acc;
        }).orElse("");
    }

    /**
     * @Description: 获取起始页
     * @param: [arg]
     * @return: java.lang.String
     */
    public static String getBP(String arg) {
        if (MDStringUtils.isNull(arg)) return "";
        String result = "";
        if (arg.isEmpty()) return result;
        if (!arg.contains("-") || arg.contains("+")) {
            return arg;
        }
        return arg.substring(0, arg.indexOf("-"));
    }

    /**
     * @Description: 获取结束页
     * @param: [arg]
     * @return: java.lang.String
     */
    public static String getEP(String arg) {
        if (MDStringUtils.isNull(arg)) return "";
        String result = "";
        if (arg.isEmpty() || arg.contains("+")) return result;
        return arg.substring(arg.indexOf("-") + 1, arg.length());
    }

    /**
     * @Description: 获取年卷期页
     * @param: [arg]
     * @return: java.lang.String
     */
    public static String getYVIP(String year, String vol, String issue, String page) {
        if (MDStringUtils.isNull(year) && MDStringUtils.isNull(vol) && MDStringUtils.isNull(issue) && MDStringUtils.isNull(page))
            return "";
        Optional<String> result = Arrays.stream(new String[]{year, vol, issue, page})
                .reduce((acc, item) -> {
                    if (MDStringUtils.isNull(item)) return acc;
                    if (MDStringUtils.isNotBlank(acc)) {
                        acc = acc + ", " + item;
                    } else {
                        acc = item;
                    }
                    return acc;
                });
        return result.get();
    }

    /**
     * @Description: 是否为整数
     * @param: [str]
     * @return: boolean
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 字符串首字母改为大写
     *
     * @param name
     * @return
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static String prefixUUID(String prefix) {
        return prefix + "_" + UUID.randomUUID();
    }

    public static String[] getArray(String arg) {
        if (MDStringUtils.isNull(arg)) return new String[]{};
        if (isContainChinese(arg)) {
            return Arrays.stream(arg.split("\\s|,|;|，|；|/")).filter(MDStringUtils::isNotBlank).toArray(String[]::new);
        } else {
            String[] res = arg.split("[;；]");
            for (int i = 0; i < res.length; i++) {
                res[i] = res[i].trim();
            }
            return res;
        }
    }

    public static Boolean isContainChinese(String arg) {
        if (MDStringUtils.isNull(arg)) return false;
        //判断中英文
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        return p.matcher(arg).find();
    }

    /**
     * Description : 适用于String Null、“” | String[] Null length | List Null size |
     * Map Null size
     *
     * @param obj
     * @return
     * @Author: XiaopoSu
     * @Create Date: 2014-1-26
     * @Modified By：
     * @Modified Date:
     */
    public static boolean isNotNull(Object obj) {
        boolean f = false;
        if (obj != null) {
            if (obj instanceof String && obj.toString() != null && !obj.toString().trim().equals(""))
                f = true;
            if (obj instanceof Integer && obj.toString() != null && !obj.toString().trim().equals(""))
                f = true;
            else if (obj instanceof String[] && ((String[]) obj).length > 0)
                f = true;
            else if (obj instanceof List<?> && ((List<?>) obj).size() > 0)
                f = true;
            else if (obj instanceof Map<?, ?> && ((Map<?, ?>) obj).size() > 0)
                f = true;
        }
        return f;
    }

    public static boolean isNull(Object obj) {
        return !isNotNull(obj);
    }

    public static boolean contains(String[] strs, String str) {
        boolean f = false;
        for (String st : strs) {
            if (st.equals(str)) {
                f = true;
                break;
            }
        }
        return f;
    }

    public static String splitString(List<?> t, String field, String side) {
        String res = "";
        if (t != null && t.size() > 0) {
            for (Object obj : t) {
                Field[] fs = obj.getClass().getDeclaredFields();
                for (Field fi2 : fs) {
                    if (fi2.getName().equals(field)) {
                        try {
                            fi2.setAccessible(true);
                            res += side + fi2.get(obj).toString() + side + ",";
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }
            if (res.length() > 0)
                res = res.substring(0, res.lastIndexOf(","));
        }
        return res;
    }

    public static String splitString(String[] strs, String side) {
        String res = "";
        if (strs != null && strs.length > 0) {
            for (String str : strs) {
                res += side + str + side + ",";
            }
            if (res.length() > 0)
                res = res.substring(0, res.lastIndexOf(","));
        }
        return res;
    }

    public static String splitString(String[] strs) {
        return splitString(strs, "");
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }

    /**
     * @param @param  data
     * @param @return
     * @return String
     * @throws
     * @Title: cometDataJSWrap
     * @Description: 数据推送包装
     * @author: chencheng
     * @date： 2014-7-11 下午2:32:53
     */
    public static String cometDataJSWrap(String iframe_id, String data) {
        StringBuffer sb = new StringBuffer();
        if (isNotNull(data)) {
            sb.append("<script type=\"text/javascript\">window.parent.frames[\"" + iframe_id + "\"].cometDataFromParent('").append(data)
                    .append("');</script>");
        }
        return sb.toString();
    }

    /**
     * @param @param  data
     * @param @return
     * @return String
     * @throws
     * @Title: cometDataJSWrap
     * @Description: 数据推送包装
     * @author: chencheng
     * @date： 2014-7-11 下午2:32:53
     */
    public static String cometSearchTextJSWrap(String iframe_id, String searchText) {
        StringBuffer sb = new StringBuffer();
        if (isNotNull(searchText)) {
            sb.append("<script type=\"text/javascript\">window.parent.frames[\"" + iframe_id + "\"].cometSearchTextFromParent('").append(searchText)
                    .append("');</script>");
        }
        return sb.toString();
    }

    /**
     * @param @param  data
     * @param @return
     * @return String
     * @throws
     * @Title: cometDataJSWrap
     * @Description: 数据推送包装
     * @author: chencheng
     * @date： 2014-7-11 下午2:32:53
     */
    public static String cometCompleteJSWrap(String iframe_id) {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type=\"text/javascript\">window.parent.frames[\"" + iframe_id + "\"].cometCompleteFromParent();</script>");
        return sb.toString();
    }

    /**
     * @param @param  data
     * @param @return
     * @return String
     * @throws
     * @Title: cometDataJSWrap
     * @Description: 数据推送包装
     * @author: chencheng
     * @date： 2014-7-11 下午2:32:53
     */
    public static String cometErrorJSWrap(String iframe_id) {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type=\"text/javascript\">window.parent.frames[\"" + iframe_id + "\"].cometErrorJSWrap();</script>");
        return sb.toString();
    }

    /**
     * @param @param  text
     * @param @return
     * @return String
     * @throws
     * @Title: cometAlertJSWrap
     * @Description: 返回alert框字符串
     * @author: chencheng
     * @date： 2014-8-4 上午11:42:45
     */
    public static String cometAlertJSWrap(String text) {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type=\"text/javascript\">alert(\"" + text + "\");</script>");
        return sb.toString();
    }

    /**
     * 全角转半角的 转换函数
     *
     * @param QJstr
     * @return String
     * @Methods Name full2HalfChange
     * @Create In 2012-8-24 By v-jiangwei
     */
    public static final String full2HalfChange(String QJstr) {
        StringBuffer outStrBuf = new StringBuffer("");
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            Tstr = QJstr.substring(i, i + 1);
            // 全角空格转换成半角空格
            if (Tstr.equals("　")) {
                outStrBuf.append(" ");
                continue;
            }
            try {
                b = Tstr.getBytes("unicode");
                // 得到 unicode 字节数据
                if (b[2] == -1) {
                    // 表示全角
                    b[3] = (byte) (b[3] + 32);
                    b[2] = 0;
                    outStrBuf.append(new String(b, "unicode"));
                } else {
                    outStrBuf.append(Tstr);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } // end for.
        return outStrBuf.toString();
    }

    /**
     * @param @param  unitEnName
     * @param @return
     * @return String
     * @throws
     * @Title: divideEnWords
     * @Description: 拆分英文机构名称，修改为通配符检索式
     * @author: chencheng
     * @date： 2014-8-7 下午1:43:36
     */
    public static final String divideEnWords(String unitEnName) {
        unitEnName = full2HalfChange(unitEnName);
        // unitEnName = unitEnName.toUpperCase();
        // unitEnName = unitEnName.replaceAll("UNIVERSITY", "UNIV");
        unitEnName = unitEnName.replaceAll("([^a-zA-Z^;^\\s])", "");
        // unitEnName = unitEnName.replaceAll("UNIV", "UNIV* ");
        /*String[] eiNameArray = unitEnName.split(";");
        for (String ei : eiNameArray) {
			if (ei.indexOf("UNIV*") > -1) {
				changedName = ei;
				break;
			}
		}*/
        return unitEnName;
    }

    public static String getStrFromObject(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }

    public static int getIntFromObject(Object o) {
        if (o != null) {
            try {
                String temp = "";
                if (o != null) {
                    temp = o.toString().replaceAll(";", "");
                }
                Integer i = Integer.valueOf(temp);
                return i;
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static String str2DoubleWithScale(String doubleStr) {
        DecimalFormat df = new DecimalFormat("#.00");
        String result = df.format(Double.valueOf(doubleStr));
        return result;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyyMMddHHmmssSSSS
     */
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    /**
     * yyyyMMddHHmm
     */
    public static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmm");
    /**
     * yyyyMMddHHmm
     */
    public static final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd");


    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     *
     * @param s
     * @return
     */
    public static String removeAllBlank(String s) {
        String result = "";
        if (null != s && !"".equals(s)) {
            result = s.replaceAll("[　*| *| *|//s*]*", "");
        }
        return result;
    }

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     *
     * @param s
     * @return
     */
    public static String trim(String s) {
        String result = "";
        if (null != s && !"".equals(s)) {
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }

    public static void main(String[] args) {
        /*String a = "15.399599996566772";
        System.out.println(MDStringUtils.str2DoubleWithScale(a));*/
    }


    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append(Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * 判断首字符是否为数字
     */
    public static boolean isNumber(String str) {
        if (!Character.isDigit(str.charAt(0))) {
            return false;
        }

        return true;
    }

    /**
     * 判断首字符是否为字母
     */
    public static boolean isLetter(String str) {
        if (!str.matches("^[A-Za-z]+$")) {
            return false;
        }

        return true;
    }

    /**
     * 去掉所有符号获取首字符
     */
    public static String getFirstStr(String keyword) {
        keyword = keyword.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×！!¥]", "")
                .replaceAll("[　*| *| *|//s*]*", "")
                .trim();
        keyword = keyword.replaceAll("=", "");
        if (MDStringUtils.isNotBlank(keyword)) {
            return keyword.substring(0, 1);
        }
        return null;
    }

    public static String replaceSome(String keyword) {
        keyword = keyword.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×！!¥]", "")
                .replaceAll("；", ";").replaceAll(" ", ";")
                .replaceAll("“", "").replaceAll("”", "")
                .trim();
        keyword = keyword.replaceAll("=", "");
        return keyword;
    }

//    public List<char> getCharFromStr(String str){
//        for(Character ch : str.toCharArray()){
//
//        }
//
//    }

    public static boolean isNotBlank(String str) {
        if (str != null) {
            if (!str.trim().equals("")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 下划线转驼峰法(默认小驼峰)
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰(驼峰，第一个字符是大写还是小写)
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean... smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if ((smallCamel.length == 0 || smallCamel[0]) && matcher.start() == 0) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }


}