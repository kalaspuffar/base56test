package org.ea.base56;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base56Test {

    private String[] splitString(String testIdentity) {
        List<String> strings = new ArrayList<String>();
        while(testIdentity.length() > 20) {
            strings.add(testIdentity.substring(0, 20));
            testIdentity = testIdentity.substring(20);
        }
        return strings.toArray(new String[strings.size()]);
    }

    private String mergeStrings(String[] strings) {
        String res = "";
        for(String s : strings) {
            res += s.replaceAll("[^"+Common.BASE56_CHAR_STR+"]", "");
        }
        return res;
    }

    @Test
    public void testEncodeAndDecodeNormal() throws Exception {
        final String testIdentity = "KKUtzSvTsNiNDdPQZdqCpZJCwzCdyQh6kk9vU7wRg6trU6cP6xVqLvNAff4iNv2PW8sw3tYcu7CaxQ5trcTCeB7WbaeDjxTbh6VEiNNPNBd";

        String[] stringArr = splitString(testIdentity);
        byte[] decodedBytes = Base56Decoder.base56Decode(stringArr);
        String[] encoded = Base56Encoder.base56Encode(decodedBytes);

        assertEquals(testIdentity, mergeStrings(encoded), "Encoding should be the same before and after");
    }

    @Test
    public void testEncodeAndDecodePadded() throws Exception {
        final String testIdentity = "jwvG2Ee7cTT55NxVt5Ja8b32urmmmvbYyMDMKQR3Q2eXMsAKJCYg6SqaihcN69s8P3reQz2xxhy4eRyDgTc3F5zrSmMhSF9SwrDqRAU7E2u";

        String[] stringArr = splitString(testIdentity);
        byte[] decodedBytes = Base56Decoder.base56Decode(stringArr);
        String[] encoded = Base56Encoder.base56Encode(decodedBytes);

        assertEquals(testIdentity, mergeStrings(encoded), "Encoding should be the same before and after");
    }

    @Test
    public void testEncodeAndDecodePaddedLong() throws Exception {
        final String testIdentity = "tP8zx8kY8EMPLqzWVq6aPL2my25Pc6KwrVyrUQwRiBKwRc6FPAmPumtrGQ8CtBhxqkRLExuE7tsFbrzUTfb2qSZ9eqDbcfjv8Zdqi6DXa5Ztn3CWsVAZFFF6jafiyixYhGqSeZdg4zjCi7cMgEsBYwWHevsB2H6y9gam2GbXk5A4SSek4Rmrmx64qxUnQJer62WnGcWArqCBzQYVw4GcvAsBjvjDvjN7RhKGdhLpLMTKB7DpfTkhCeaPDWCZxy3AVzxbwKSdjHcZmnaMegiE2t";

        String[] stringArr = splitString(testIdentity);
        byte[] decodedBytes = Base56Decoder.base56Decode(stringArr);
        String[] encoded = Base56Encoder.base56Encode(decodedBytes);

        assertEquals(testIdentity, mergeStrings(encoded), "Encoding should be the same before and after");
    }

}