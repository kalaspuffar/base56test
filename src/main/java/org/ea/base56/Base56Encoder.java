package org.ea.base56;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public final class Base56Encoder
{
    public static String[] base56Encode(final byte[] binaryData)
    {
        final int expectedLength = (int) Math.ceil((binaryData.length*8)/(Math.log(Common.BASE_INT)/Math.log(2)));

        final List<String> lines = new ArrayList<>();
        final byte[] bytesConvertedToLittleEndian = binaryData.clone();
        ArrayUtils.reverse(bytesConvertedToLittleEndian);
        BigInteger val = new BigInteger(1, bytesConvertedToLittleEndian);
        StringBuffer lineUnderContruction = new StringBuffer();
        int numberOfDigitsOnLine = 0;
        int totalNumberOfDigits = 0;
        int lineNumber = 0;
        while (totalNumberOfDigits < expectedLength)
        {
            if (val.compareTo(BigInteger.ZERO) > 0)
            {
                final BigInteger twoVals[] = val.divideAndRemainder(Common.BASE);
                final int digitLookup = twoVals[1].intValue();
                val=twoVals[0];
                lineUnderContruction.append(Common.lookupBase56Char(digitLookup));
            }
            else
            {
                lineUnderContruction.append(Common.BASE56_CHARS[0]);  // pad with "zero"
            }
            numberOfDigitsOnLine++;
            totalNumberOfDigits++;
            if (numberOfDigitsOnLine >= Common.CHARS_PER_LINE)
            {
                lineUnderContruction.append(Common.calculateCheckDigit(lineUnderContruction.toString(), lineNumber++));
                lines.add(formatLineForHuman(lineUnderContruction.toString()));
                lineUnderContruction = new StringBuffer();
                numberOfDigitsOnLine = 0;
            }
        }
        if (numberOfDigitsOnLine > 0)
        {
            lineUnderContruction.append(Common.calculateCheckDigit(lineUnderContruction.toString(), lineNumber++));
            lines.add(formatLineForHuman(lineUnderContruction.toString()));
        }

        return lines.toArray(new String[0]);
    }

    private static String formatLineForHuman(final String inputLine)
    {
        final StringBuffer sb = new StringBuffer();
        int charMod = 0;
        for (final char c : inputLine.toCharArray())
        {
            sb.append(c);
            charMod++;
            if (charMod == 4)
            {
                sb.append(' ');
                charMod = 0;
            }
        }
        return sb.toString();
    }
}
