package gtu.util

import java.io.IOException
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.io.StringReader
import java.io.BufferedReader
import java.util.ArrayList
import java.io.File
import com.oracle.util.Checksums.update
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.UUID
import sun.text.normalizer.UTF16.append

import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

fun main(args: Array<String>) {
    println(StringUtil.oracleDecodeAsString("X", "A", "=A", "B", "=B", "C"))
    println("done...")
}


class StringUtil {
    companion object {
        /**
         * 抓全形&中文字
         *
         * @param str
         * @param isChineseBool
         * @return
         */
        fun getChineseWord2(str: String, isChineseBool: Boolean): String {
            val sb = StringBuffer()
            val cs = str.toCharArray()
            for (c in cs) {
                val x = String(charArrayOf(c))
                if (isChineseBool && x.toByteArray().size > 1) {
                    sb.append(c)
                } else if (!isChineseBool && x.toByteArray().size == 1) {
                    sb.append(c)
                }
            }
            return sb.toString()
        }

        /**
         * 抓中文字(注音不抓)
         *
         * @param str
         * @param isChineseBool
         * @return
         */
        fun getChineseWord(str: String, isChineseBool: Boolean): String {
            val isChinese = if (isChineseBool) "" else "^"
            val chinesePattern = Pattern.compile("[$isChinese\u4e00-\u9fa5]")
            val sb = StringBuilder()
            val mth = chinesePattern.matcher(str)
            while (mth.find()) {
                sb.append(mth.group())
            }
            return sb.toString()
        }

        /**
         * 抓中文字(注音不抓)
         *
         * @param str
         * @param isChineseBool
         * @return
         */
        fun hasChineseWord(str: String): Boolean {
            val mth = Pattern.compile("[\u4e00-\u9fa5]").matcher(str)
            return if (mth.find()) {
                true
            } else false
        }

        fun testScanner() {
            val str: String? = null
            val scn = Scanner(str)
            while (scn.hasNextLine()) {
                System.out.println(scn.nextLine())
            }
            scn.close()
        }

        /**
         * 清除所有半形空白
         *
         * @param values
         * @return
         */
        fun trimAllSpace(values: String): String {
            val `val` = values.toCharArray()
            val sb = StringBuffer()
            for (ii in `val`.indices) {
                if (`val`[ii] != ' ') {
                    sb.append(`val`[ii])
                }
            }
            return sb.toString()
        }

        /**
         * 比較字串相似度
         *
         * @param mime
         * @param theies
         * @return
         */
        fun compareString(mime: String, theies: String): Float {
            var flag: Int
            var sp = 0
            var icount = 0
            var cp = 0
            for (i in 0 until mime.length) {
                flag = 1
                for (j in 0 until theies.length) {
                    icount = 0
                    if (mime[i] == theies[j]) {
                        if (flag == 1) {
                            sp++
                            flag = 0
                        }
                        while (i + icount < mime.length && j + icount < theies.length && mime[i + icount] == theies[j + icount]) {
                            icount++
                        }
                        if (icount > cp)
                            cp = icount
                        icount = 0
                    }
                }
            }
            println(sp.toString() + "ff" + cp)
            return Math.min(sp, cp) / mime.length.toFloat()
        }

        fun testMD5() {
            try {
                System.out.println("1 -> " + StringUtil_.toHexString("A".toByteArray()))
                val md = MessageDigest.getInstance("md5")
                md.update("A".toByteArray())
                val digest = md.digest()

                System.out.println("2 -> " + StringUtil_.toHexString(digest))

                System.out.println("3 -> " + StringUtil_.digest("A", "md5"))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        /**
         * 將字串轉成UTF8編碼
         *
         * @param str
         * @return
         */
        fun toUTF8(str: String): String {
            try {
                return String(str.toByteArray(), "UTF-8")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return ""
        }

        /**
         * 中文轉換編碼
         *
         * @param unicodeString
         * 字串
         * @param code
         * 欲轉換的編碼
         * @return
         */
        fun exchangeCode(unicodeString: String?, code: String): String {
            var rtnString = ""
            try {
                if (unicodeString != null) {
                    val b = unicodeString.toByteArray(charset(code))
                    rtnString = String(b)
                }
            } catch (e: Exception) {
                System.err.println(e.message)
            }

            return rtnString
        }

        /**
         * 傳回一個雜湊長度36的字串ID
         *
         * @return
         */
        fun getUUID(): String {
            return UUID.randomUUID().toString()
        }

        /**
         * 將字串內容複製給另一個字串變數,不會傳參考值
         *
         * @param copy
         * 欲複製的字串
         * @return
         */
        fun copyString(copy: String): String {
            return copy
        }

        /**
         * 檢核是否為數值 <br></br>
         * Ex:134.23 or 452 return true <br></br>
         * Ex:323bd32 or 2334.3f return false
         *
         * @param data
         * @return
         */
        fun chkNumber(data: String?): Boolean {
            return if (data == null || data == "") false else Pattern.matches("\\-?(\\d+\\.\\d+|\\d+)", data)
        }

        /**
         * 將字串填補所需字元長度 Ex:fillChar("777","R",'x',5) rtn : "xx777"
         *
         * @param str
         * 字串
         * @param putLR
         * L靠左 / R靠右
         * @param c
         * 填補字元
         * @param len
         * 填補長度
         * @return
         */
        fun fillChar(str: String?, putLR: Char, c: Char, len: Int): String {
            var str = str
            var len = len
            try {
                if (str == null)
                    str = ""
                else {
                    str = str.trim { it <= ' ' }
                    len = len - str.length
                    if (putLR == 'r' || putLR == 'R') {
                        str = fillChar(c, len) + str
                    } else {
                        str = str + fillChar(c, len)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return str
        }

        /**
         * 將字串填補所需字元長度(全形計算長度２) Ex:fillChar("呵呵","R",'x',10) rtn : "xxxxxx呵呵"
         *
         * @param str
         * 字串
         * @param putLR
         * Ｌl靠左 / Ｒr 靠右
         * @param c
         * 填補字元
         * @param len
         * 填補長度
         * @return
         */
        fun fillCharForByte(str: String?, putLR: String, c: Char, len: Int): String {
            var str = str
            var len = len
            try {
                if (str == null)
                    str = ""
                else {
                    str = str.trim { it <= ' ' }
                    val strlen = str.toByteArray().size
                    println(strlen > len)
                    if (strlen > len) {
                        val tmp = ByteArray(len)
                        System.arraycopy(str.toByteArray(), 0, tmp, 0, len)
                        str = String(tmp)
                    } else if (strlen < len) {
                        len = len - str.length
                        if (putLR.equals("R", ignoreCase = true)) {
                            str = fillChar(c, len) + str
                        } else {
                            str = str + fillChar(c, len)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return str
        }

        private fun fillChar(c: Char, len: Int): String {
            val sb = StringBuffer()
            for (i in 0 until len) {
                sb.append(c)
            }
            return sb.toString()
        }

        /**
         * 檢查字串若為null傳回空字串
         *
         * @param str
         * @return
         */
        fun chkNull(str: String?): String {
            return str ?: ""
        }

        /**
         * @param num
         * 數值字串 ex:"4347620043"
         * @param complicated
         * true : 則中文為金融複雜用字 Ex:壹貳參肆伍陸柒捌玖<br></br>
         * false : 一二三四五六七八九
         * @return 肆參肆柒陸貳零零肆參 / 四三四七六二００四三
         */
        fun numToChnEasy(num: String, complicated: Boolean): String {
            val number = arrayOf(
                arrayOf("零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖"),
                arrayOf("０", "一", "二", "三", "四", "五", "六", "七", "八", "九")
            )
            val comp = if (complicated == true) 0 else 1
            val tmpnum = num.toCharArray()
            val sb = StringBuffer()
            for (ii in tmpnum.indices) {
                sb.append(number[comp][Integer.parseInt(tmpnum[ii].toString())])
            }
            return sb.toString()
        }

        /**
         * @param num
         * 數值字串 ex:"4347620043"
         * @param complicated
         * true : 則中文為金融複雜用字 Ex:壹貳參肆伍陸柒捌玖<br></br>
         * false : 一二三四五六七八九
         * @return 肆拾參億肆仟柒佰陸拾貳萬肆拾參 / 四十三億四千七百六十二萬四十三
         */
        fun numToChn(num: String, complicated: Boolean): String {
            var num = num
            val betweenNum = arrayOf(arrayOf("", ""), arrayOf("拾", "十"), arrayOf("佰", "百"), arrayOf("仟", "千"))
            val betweenDef = arrayOf("", "萬", "億", "兆", "兆兆")
            val number = arrayOf(
                arrayOf("", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖"),
                arrayOf("", "一", "二", "三", "四", "五", "六", "七", "八", "九")
            )

            val numary = ArrayList<String>()
            val comp = if (complicated == true) 0 else 1

            while (num.length > 4) {
                val c = num.substring(num.length - 4, num.length).toCharArray()
                var pos: Int? = null
                run {
                    var ii = 0
                    while (c[ii] == '0') {
                        c[ii] = ' '
                        pos = ii
                        ii++
                    }
                }
                if (pos != null)
                    c[pos] = '0'
                var ii = c.size - 1
                while (c[ii] == '0') {
                    c[ii] = ' '
                    ii--
                }
                numary.add(String(c))
                num = num.substring(0, num.length - 4)
            }
            if (num != null)
                numary.add(num)

            val sb = StringBuffer()
            for (ar in numary.indices.reversed()) {
                val cha = numary[ar].trim { it <= ' ' }.toCharArray()
                var ii = 0
                var jj = cha.size - 1
                while (ii < cha.size) {
                    val tmpnum = number[comp][Integer.parseInt(cha[ii].toString())]
                    var tmpbet = String()
                    if (cha[ii] == '0' && jj != 0) {
                        tmpbet = "零"
                    } else {
                        tmpbet = betweenNum[jj][comp]
                    }
                    sb.append(tmpnum + tmpbet)
                    ii++
                    jj--
                }
                sb.append(betweenDef[ar])
            }
            return sb.toString()
        }

        // 将金额整数部分转换为中文大写
        fun integer2rmb(integer: String): String {
            var integer = integer
            val RMB_NUMS = "零壹貳參肆伍陸柒捌玖".toCharArray()
            val UNITS = arrayOf("元", "角", "分", "整")
            val U1 = arrayOf("", "拾", "佰", "仟")
            val U2 = arrayOf("", "萬", "億")
            val buffer = StringBuilder()
            // 从个位数开始转换

            val dotPlace = integer.indexOf(".")
            if (dotPlace > 0) {
                integer = integer.substring(0, dotPlace)
            }

            var i: Int
            var j: Int
            i = integer.length - 1
            j = 0
            while (i >= 0) {
                val n = integer[i]
                if (n == '0') {
                    // 当n是0且n的右边一位不是0时，插入[零]
                    if (i < integer.length - 1 && integer[i + 1] != '0') {
                        buffer.append(RMB_NUMS[0])
                    }
                    // 插入[万]或者[亿]
                    if (j % 4 == 0) {
                        if (i > 0 && integer[i - 1] != '0' || i > 1 && integer[i - 2] != '0' || i > 2 && integer[i - 3] != '0') {
                            buffer.append(U2[j / 4])
                        }
                    }
                } else {
                    if (j % 4 == 0) {
                        buffer.append(U2[j / 4]) // 插入[万]或者[亿]
                    }
                    buffer.append(U1[j % 4]) // 插入[拾]、[佰]或[仟]
                    buffer.append(RMB_NUMS[n - '0']) // 插入数字
                }
                i--
                j++
            }
            return buffer.reverse().toString() + "元整"
        }

        /**
         * 將字串分散 每個字間隔相當
         *
         * @param str
         * 字串
         * @param len
         * 總長度
         * @return
         */
        fun splitStringSpace(str: String?, len: Int): String {
            if (str == null || str == "")
                return ""
            val tmplen = (len - str.toByteArray().size) / (str.length + 1)
            val sb = StringBuffer()
            val cstr = str.toCharArray()
            for (ii in cstr.indices) {
                sb.append(fillChar(' ', tmplen) + cstr[ii])
            }
            return sb.toString() + fillChar(' ', len - sb.toString().length)
        }

        /**
         * 將數值格式化 每三位加逗點 若有小數則顯示兩位
         *
         * @param <T>
         * 可接受 字串 , double , long
         * @param ttt
         * 預備處理的數值
         * @return
        </T> */
        fun <T> formatNumber(ttt: T): String {
            return formatNumber("##,##0.##", ttt)
        }

        /**
         * 將數值格式化
         *
         * @param <T>
         * 可接受 字串 , double , long
         * @param format
         * format格式
         * @param ttt
         * 預備處理的數值
         * @return
        </T> */
        fun <T> formatNumber(format: String, ttt: T): String {
            var rtn = String()
            try {
                val df = DecimalFormat()
                df.applyPattern(format)
                if (ttt is String) {
                    rtn = df.format(java.lang.Double.parseDouble(ttt.toString()))
                } else {
                    rtn = df.format(ttt)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return rtn
        }

        /**
         * 字串轉16進位
         *
         * @param b
         * @return
         */
        fun toHexString(b: ByteArray): String {
            val hexString = StringBuffer()
            var plainText: String
            for (ii in b.indices) {
                plainText = Integer.toHexString(0xFF and b[ii])
                if (plainText.length < 2) {
                    plainText = "0$plainText"
                }
                hexString.append(plainText)
            }
            return hexString.toString()
        }

        /**
         * 加密
         *
         * @param cleartext
         * @param algorithm
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun digest(cleartext: String, algorithm: String): String {
            val hex = "0123456789abcdef".toCharArray()
            val md = MessageDigest.getInstance(algorithm)
            md.update(cleartext.toByteArray())
            val digest = md.digest()
            val sb = StringBuffer(2 * digest.size)
            for (i in digest.indices) {
                val high = digest[i] and 0xf0 shr 4
                val low = digest[i] and 0xf
                sb.append(hex[high])
                sb.append(hex[low])
            }
            return sb.toString()
        }

        /**
         * 將package path格式成檔案路徑 Ex : "aaa.bbb.ccc.ddd.eee" -> aaa\bbb\ccc\ddd\eee\
         *
         * @param packageStr
         * @return
         */
        fun parsePackageToPath(packageStr: String): String {
            if (StringUtils.isBlank(packageStr)) {
                return StringUtils.EMPTY
            }
            val packages = packageStr.split("\\.".toRegex()).toTypedArray()
            val sb = StringBuilder()
            for (tempP in packages) {
                sb.append(tempP + File.separator)
            }
            return sb.toString()
        }

        /**
         * 依照pattern切割字串 並取得分割得符合的MatchResult
         */
        abstract class ScannerWithDelimiter {
            abstract fun appendReplacement(inputStr: String, matcher: MatchResult)

            abstract fun appendTail(inputStr: String)

            fun execute(pattern: Pattern, inputStr: String) {
                val matcher = pattern.matcher(inputStr)
                var sb = StringBuffer()
                while (matcher.find()) {
                    matcher.appendReplacement(sb, "")
                    appendReplacement(sb.toString(), matcher)
                    sb = StringBuffer()
                }
                matcher.appendTail(sb)
                appendTail(sb.toString())
            }
        }

        /**
         * 去掉全形空白
         */
        fun trimFullSpace(str: String): String {
            val sb = StringBuilder()
            val arry = str.toCharArray()
            for (c in arry) {
                if (c == (161 + 64).toChar() || c == 12288.toChar()) {
                    // do nothing
                } else {
                    sb.append(c)
                }
            }
            return sb.toString()
        }

        /**
         * DB限制長度時使用
         */
        fun getDBLimitStr(strValue: String, maxLength: Int): String {
            var strValue = strValue
            val bs = strValue.toByteArray()
            val maxLen = Math.min(maxLength, bs.size)
            strValue = String(bs, 0, maxLen)
            return strValue
        }

        fun halfCharToFullChar() {
            var outStr = ""
            val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()
            var tranTemp = 0
            for (i in chars.indices) {
                tranTemp = chars[i].toInt()
                if (tranTemp != 45)
                // ASCII碼:45 是減號 -
                    tranTemp += 65248 // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差
                outStr += tranTemp.toChar()
            }
            println("outStr : $outStr")
        }

        fun isUUID(str: String): Boolean {
            val UUID_PATTERN = Pattern.compile("^\\w{8}\\-\\w{4}\\-\\w{4}\\-\\w{4}\\-\\w{12}$")
            return UUID_PATTERN.matcher(str).find()
        }

        /**
         * 字串轉成原始型別物件
         */
        fun <T> stringToPrimitive(value: String?, targetClz: Class<T>): Any? {
            return if (value == null) {
                null
            } else ConvertUtils.convert(value, targetClz)
        }

        fun appendReplacementEscape(content: String): String {
            try {
                return AppendReplacementEscaper(content).result
            } catch (ex: Exception) {
                throw RuntimeException(
                    String.format(
                        "[appendReplacementEscape][content]\n ERR : %s \n %s", //
                        ex.message, content
                    ), ex
                )
            }

        }

        private class AppendReplacementEscaper internal constructor(internal var content: String) {
            private var result: String
                internal set

            init {
                result = StringUtils.defaultString(content).toString()
                if (StringUtils.isBlank(result)) {
                    return
                }
                if (result.indexOf('$') != -1) {
                    result = replaceChar(result, '$')
                }
                if (result.indexOf('/') != -1) {
                    result = replaceChar(result, '/')
                }
                if ("\\" == result) {
                    result = "\\\\"
                }
                /*
             * if (result.indexOf('\\') != -1) { result = replaceChar(result,
             * '\\'); }
             */
            }

            private fun replaceChar(content: String, from: Char): String {
                if (content.indexOf(from.toInt()) == -1) {
                    return content
                }
                val sb = StringBuffer()
                val arry = content.toCharArray()
                for (ii in arry.indices) {
                    val a = arry[ii]
                    if (a == from) {
                        if (from != '\\') {
                            if (ii - 1 >= 0 && arry[ii - 1] != '\\') {
                                sb.append("\\" + a)
                            } else if (ii == 0) {
                                sb.append("\\" + a)
                            }
                        } else if (from == '\\') {
                            if (ii == arry.size - 1) {
                                sb.append("\\\\")
                            } else if (ii + 1 < arry.size) {
                                val b = arry[ii + 1]
                                if (!ArrayUtils.contains(ESCAPE_ARRY, b)) {
                                    sb.append("\\\\")
                                }
                            }
                        }
                    } else {
                        sb.append(a)
                    }
                }
                return sb.toString()
            }

            companion object {
                private val ESCAPE_ARRY = charArrayOf('t', 'b', 'n', 'r', 'f', '\'', '\"', '\\')
            }
        }

        fun readContentToList(content: String, trim: Boolean, emptyIgnore: Boolean, distinct: Boolean): List<String> {
            val lst = ArrayList<String>()
            var bufreader: BufferedReader? = null
            try {
                bufreader = BufferedReader(StringReader(content))
                var line: String? = null
                while ((line = bufreader.readLine()) != null) {
                    if (trim) {
                        line = StringUtils.trimToEmpty(line)
                    }
                    if (emptyIgnore && StringUtils.isBlank(line)) {
                        continue
                    }
                    if (distinct) {
                        if (!lst.contains(line)) {
                            lst.add(line)
                            continue
                        }
                    } else {
                        lst.add(line)
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                try {
                    bufreader!!.close()
                } catch (e: IOException) {
                }

            }
            return lst
        }

        fun oracleDecodeAsString(vararg strs: Any): String? {
            if (strs == null) {
                throw RuntimeException("decode args 不可為空!")
            }
            val `val` = strs[0]
            var index = 1
            while (true) {
                if (index > strs.size - 1) {
                    if (strs.size - 1 and 1 == 0) {
                        return null
                    }
                    return if (strs[strs.size - 1] != null) strs[strs.size - 1].toString() else ""
                } else if (index == strs.size - 1) {
                    return if (strs[index] != null) strs[index].toString() else ""
                }
                val strVal = if (strs[index] != null) strs[index].toString() else ""
                val strVal2 = `val`?.toString() ?: ""
                if (StringUtils.equals(strVal, strVal2)) {
                    return if (strs[index + 1] != null) strs[index + 1].toString() else ""
                }
                index += 2
            }
        }

        fun oracleDecode(vararg strs: Any): Any? {
            if (strs == null) {
                throw RuntimeException("decode args 不可為空!")
            }
            val `val` = strs[0]
            var index = 1
            while (true) {
                if (index > strs.size - 1) {
                    return if (strs.size - 1 and 1 == 0) {
                        null
                    } else strs[strs.size - 1]
                } else if (index == strs.size - 1) {
                    return strs[index]
                }
                if (ObjectUtils.equals(`val`, strs[index])) {
                    return strs[index + 1]
                }
                index += 2
            }
        }
    }
}