package satori.crypto;

import junit.framework.Assert;

import org.junit.Test;

public class TestCrypter {

    @Test
    public void doSome() {

        for (int i = 0; i < 200; i++) {
            String out = Crypter.encrypt("Michael Night " + i);
            Assert.assertEquals("Michael Night " + i, Crypter.decrypt(out));
        }

    }

    @Test
    public void twoWay() {

        String one = Crypter.encrypt("Michael Night");
        String two = Crypter.encrypt("Kitt is nice");

        Assert.assertEquals("Michael Night", Crypter.decrypt(one));
        Assert.assertEquals("Kitt is nice", Crypter.decrypt(two));

    }

}
