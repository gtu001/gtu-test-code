from _decimal import Decimal
import decimal
import math
import numbers
import re
from gtu.reflect import checkSelf

def roundHalfUp2(value, scale):
    '''四捨五入,小數點後多的零會保留'''
    context = decimal.getcontext()
    context.rounding = decimal.ROUND_HALF_UP
    val = round(decimal.Decimal(value), scale)
    return val


def roundHalfUp3(num, scale):
    '''四捨五入,小數點後多的零會保留'''
    return round(num, scale)


def roundHalfUp4(strVal, scale):
    '''四捨五入後, 若為 ?.00 後面.00捨棄 '''
    if str(strVal).isnumeric() :
        return strVal
    val = roundHalfUp2(strVal, scale)
    mth = re.match(r"^([-]?\d+)\.[0]+$", str(val), re.DOTALL | re.MULTILINE)
    if mth:
        return mth.group(1)
    return val


def roundHalfUp(strVal, scale):
    '''四捨五入,小數點後多的零不會保留'''
    if not isNumber(strVal):
        raise Exception("非數值 : " + strVal)
    if str(strVal).isnumeric() :
        return strVal
    newVal = trimDotRightZero(round(Decimal(strVal), scale))
    return newVal


def isNumber(s):
    try:
        float(s)
        return True
    except ValueError:
        pass
    try:
        import unicodedata
        unicodedata.numeric(s)
        return True
    except (TypeError, ValueError):
        pass
    return False


def isNumberType(val):
    if isinstance(val, numbers.Number):
        return True
    return False


def floor(num):
    return math.floor(num)


def ceil(num):
    return math.ceil(num)


def trimDotRightZero(strVal):
    '''去掉小數點右邊多的0'''
    strVal = str(strVal)
    mth = re.match(r"^([-]?\d+\.[0-9]*?)[0]+$", strVal, re.DOTALL | re.MULTILINE)
    if mth:
        return mth.group(1)
    else :
        return strVal


def roundTest(value, scale):
    scaleStr = "0."
    for i in range(scale) :
        scaleStr += "0"
    print("ROUND_05UP\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_05UP))
    print("ROUND_CEILING\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_CEILING))
    print("ROUND_DOWN\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_DOWN))
    print("ROUND_FLOOR\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_FLOOR))
    print("ROUND_HALF_DOWN\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_HALF_DOWN))
    print("ROUND_HALF_EVEN\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_HALF_EVEN))
    print("ROUND_HALF_UP\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_HALF_UP))
    print("ROUND_UP\t", Decimal(value).quantize(Decimal(scaleStr), rounding = decimal.ROUND_UP))
    

def fixdScale(strVal, scale):
    pattern = r"^([-]?\d+)\.?(\d{0," + str(scale) + "})"
    mth = re.match(pattern, str(strVal), re.DOTALL | re.MULTILINE)
    if mth :
        num1 = mth.group(1)
        num2 = mth.group(2)
        if len(num2) == 0:
            return num1
        num2 = num2.ljust(scale, '0')
        return num1 + "." + num2
    return strVal


if __name__ == '__main__' :
    print(roundHalfUp(4.42, 3))
    print(roundHalfUp(4.4445, 3))
    print(roundHalfUp(4.4446, 3))
    print(floor(4.4446))
    print(ceil(4.4446))
    print(fixdScale(103330.1, 3))


