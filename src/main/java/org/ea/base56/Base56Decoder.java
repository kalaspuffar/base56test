package org.ea.base56;

import java.math.BigInteger;
import org.apache.commons.lang3.ArrayUtils;

public final class Base56Decoder
{
    public static byte[] base56Decode(final String[] textInput)
    {
        final StringBuilder inputToBeReversed = new StringBuilder();
        int lineNumber = 0;
        for (final String line : textInput)
        {
            final String valueLine = line.replaceAll("[^"+Common.BASE56_CHAR_STR+"]", "");
            final String checkLine = valueLine.substring(0, valueLine.length()-1);
            final char expectedCheckChar = Common.calculateCheckDigit(checkLine, lineNumber++);
            if (expectedCheckChar != valueLine.charAt(valueLine.length()-1))
            {
                System.out.println("Invalid checksum on line \"" + line + "\" , expected: " + expectedCheckChar);
                return new byte[0];
            }
            inputToBeReversed.append(checkLine);
        }
        final String reversedInput = inputToBeReversed.reverse().toString();
        final int expectedNumberOfBytes = (int)(reversedInput.length()*(Math.log(Common.BASE_INT)/Math.log(2))/8);
        BigInteger val = BigInteger.ZERO;
        for (final char c : reversedInput.toCharArray())
        {
            val = val.multiply(Common.BASE).add(Common.getDigitFromChar(c));
        }
        final byte[] arrayToConvertToBigEndian = val.toByteArray();
        //HexDumper.dumpInHex(arrayToConvertToBigEndian);
        if (arrayToConvertToBigEndian.length < expectedNumberOfBytes)
        {
            //System.out.format("zero padding: expectedNumberOfBytes=%d, arraySize=%d%n", expectedNumberOfBytes, arrayToConvertToBigEndian.length);
            final byte[] result = new byte[expectedNumberOfBytes];
            int i=0;
            for(; i<expectedNumberOfBytes-arrayToConvertToBigEndian.length; i++)
            {
                result[i] = 0;
            }
            for(int j=0; j<arrayToConvertToBigEndian.length; j++)
            {
                result[i++]=arrayToConvertToBigEndian[j];
            }
            ArrayUtils.reverse(result);
            return result;
        }
        else if (arrayToConvertToBigEndian.length > expectedNumberOfBytes)
        {
            //Extra zero byte because of sign in BigInteger
            //System.out.format("sign issue: expectedNumberOfBytes=%d, arraySize=%d%n", expectedNumberOfBytes, arrayToConvertToBigEndian.length);
            //HexDumper.dumpInHex("sign issue", arrayToConvertToBigEndian);
            final byte[] result = new byte[expectedNumberOfBytes];
            for(int i=0; i<expectedNumberOfBytes; i++)
            {
                result[i] = arrayToConvertToBigEndian[i+1];
            }
            ArrayUtils.reverse(result);
            return result;
        }

        ArrayUtils.reverse(arrayToConvertToBigEndian);
        return arrayToConvertToBigEndian;
    }

    public static boolean checkALineOfInput(final String lineTocheck, final int lineNumber)
    {
        final String valueLine = lineTocheck.replaceAll("[^"+Common.BASE56_CHAR_STR+"]", "");
        if (valueLine.isEmpty())  return false;
        final String checkLine = valueLine.substring(0, valueLine.length()-1);
        final char actualCheckChar = valueLine.charAt(valueLine.length()-1);
        final char expectedCheckChar = Common.calculateCheckDigit(checkLine, lineNumber);
        return actualCheckChar == expectedCheckChar;
    }
}
