package bizibox.idbKeyDesktop;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;

public class PasscodeGenerator {
    private static final int B = 6;
    private static final int H = 30;
    private static final int F = 1;
    private static final int E = (int)Math.pow(10.0D, 6.0D);
    private final _B G;
    private final int D;
    private final int A;
    private _A C;

    public PasscodeGenerator(Mac var1) {
        this((Mac)var1, 6, 30);
    }

    public PasscodeGenerator(final Mac var1, int var2, int var3) {
        this(new _B() {
            public byte[] A(byte[] var1x) {
                return var1.doFinal(var1x);
            }
        }, var2, var3);
    }

    public PasscodeGenerator(_B var1, int var2, int var3) {
        this.C = new _A() {
            public long A() {
                long var1 = System.currentTimeMillis() / 1000L;
                return var1 / (long)this.B();
            }

            public int B() {
                return PasscodeGenerator.this.A;
            }
        };
        this.G = var1;
        this.D = var2;
        this.A = var3;
    }

    private String A(int var1) {
        String var2 = Integer.toString(var1);

        for(int var3 = var2.length(); var3 < this.D; ++var3) {
            var2 = "0" + var2;
        }

        return var2;
    }

    public String generateTimeoutCode() throws GeneralSecurityException {
        return this.generateResponseCode(this.C.A());
    }

    public String generateNextTimeoutCode() throws GeneralSecurityException {
        return this.generateResponseCode(this.C.A() + 1L);
    }

    public String generateResponseCode(long var1) throws GeneralSecurityException {
        byte[] var3 = ByteBuffer.allocate(8).putLong(var1).array();
        return this.generateResponseCode(var3);
    }

    public String generateResponseCode(byte[] var1) throws GeneralSecurityException {
        byte[] var2 = this.G.A(var1);
        int var3 = var2[var2.length - 1] & 15;
        int var4 = this.A(var2, var3) & 2147483647;
        int var5 = var4 % E;
        return this.A(var5);
    }

    private int A(byte[] var1, int var2) {
        DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1, var2, var1.length - var2));

        try {
            int var4 = var3.readInt();
            return var4;
        } catch (IOException var6) {
            throw new IllegalStateException(var6);
        }
    }

    public boolean verifyResponseCode(long var1, String var3) throws GeneralSecurityException {
        String var4 = this.generateResponseCode(var1);
        return var4.equals(var3);
    }

    public boolean verifyTimeoutCode(String var1) throws GeneralSecurityException {
        return this.verifyTimeoutCode(var1, 1, 1);
    }

    public boolean verifyTimeoutCode(String var1, int var2, int var3) throws GeneralSecurityException {
        long var4 = this.C.A();
        String var6 = this.generateResponseCode(var4);
        if(var6.equals(var1)) {
            return true;
        } else {
            int var7;
            String var8;
            for(var7 = 1; var7 <= var2; ++var7) {
                var8 = this.generateResponseCode(var4 - (long)var7);
                if(var8.equals(var1)) {
                    return true;
                }
            }

            for(var7 = 1; var7 <= var3; ++var7) {
                var8 = this.generateResponseCode(var4 + (long)var7);
                if(var8.equals(var1)) {
                    return true;
                }
            }

            return false;
        }
    }

    interface _A {
        int B();
        long A();
    }

    interface _B {
        byte[] A(byte[] var1) throws GeneralSecurityException;
    }
}
