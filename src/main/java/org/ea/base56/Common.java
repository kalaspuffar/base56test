package org.ea.base56;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.ArrayUtils;

final class Common
{
    final static String BASE56_CHAR_STR = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
    final static char[] BASE56_CHARS = BASE56_CHAR_STR.toCharArray();
    final static int BASE_INT = 56;
    final static BigInteger BASE = BigInteger.valueOf(BASE_INT);
    final static int CHARS_PER_LINE = 19;
    final static int charLookup[] = new int[256];

    static
    {
        for (int i=0; i<256; i++) charLookup[i]=-1;
        int v = 0;
        for (final char c: BASE56_CHARS)
        {
            charLookup[(int)c] = v++;
        }
    }

    static char calculateCheckDigit(final String lineToCalculateCheckDigitOf, final int lineNumber)
    {
        if ((lineNumber <0) || (lineNumber >255))
            throw new IllegalArgumentException("Input line number is not valid (in byte range 0-255): " + lineNumber);

        final MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (final NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("Unable to get SHA-256 hash", e);
        }
        md.update(lineToCalculateCheckDigitOf.getBytes());
        final byte[] lineNumberByte = new byte[1];
        lineNumberByte[0] = (byte)lineNumber;
        final byte[] digest = md.digest(lineNumberByte);
        ArrayUtils.reverse(digest);
        final BigInteger val = new BigInteger(1, digest);
        return lookupBase56Char(val.mod(Common.BASE).intValue());
    }

    static char lookupBase56Char(int intValue)
    {
        return Common.BASE56_CHARS[intValue];
    }

    public static BigInteger getDigitFromChar(final char c)
    {
        final int result = charLookup[(int)c];
        if (result <0) throw new IllegalStateException("Invalid input char: " + c);
        return BigInteger.valueOf(result);
    }
}