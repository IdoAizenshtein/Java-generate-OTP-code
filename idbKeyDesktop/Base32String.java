package bizibox.idbKeyDesktop;

import java.util.HashMap;

public class Base32String {
    private static final Base32String F = new Base32String("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567");
    private String E;
    private char[] B;
    private int D;
    private int A;
    private HashMap<Character, Integer> G;
    static final String C = "-";
    static Base32String A() {
        return F;
    }
    protected Base32String(String var1) {
        this.E = var1;
        this.B = this.E.toCharArray();
        this.D = this.B.length - 1;
        this.A = Integer.numberOfTrailingZeros(this.B.length);
        this.G = new HashMap();
        for(int var2 = 0; var2 < this.B.length; ++var2) {
            this.G.put(Character.valueOf(this.B[var2]), Integer.valueOf(var2));
        }
    }
    public static byte[] decode(String var0) throws _A {
        return A().decodeInternal(var0);
    }
    protected byte[] decodeInternal(String var1) throws _A {
        var1 = var1.trim().replaceAll("-", "").replaceAll(" ", "");
        var1 = var1.toUpperCase();
        if(var1.length() == 0) {
            return new byte[0];
        } else {
            int var2 = var1.length();
            int var3 = var2 * this.A / 8;
            byte[] var4 = new byte[var3];
            int var5 = 0;
            int var6 = 0;
            int var7 = 0;
            char[] var8 = var1.toCharArray();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                char var11 = var8[var10];
                if(!this.G.containsKey(Character.valueOf(var11))) {
                    throw new _A("Illegal character: " + var11);
                }

                var5 <<= this.A;
                var5 |= ((Integer)this.G.get(Character.valueOf(var11))).intValue() & this.D;
                var7 += this.A;
                if(var7 >= 8) {
                    var4[var6++] = (byte)(var5 >> var7 - 8);
                    var7 -= 8;
                }
            }

            return var4;
        }
    }

    public static String encode(byte[] var0) {
        return A().encodeInternal(var0);
    }

    protected String encodeInternal(byte[] var1) {
        if(var1.length == 0) {
            return "";
        } else if(var1.length >= 268435456) {
            throw new IllegalArgumentException();
        } else {
            int var2 = (var1.length * 8 + this.A - 1) / this.A;
            StringBuilder var3 = new StringBuilder(var2);
            int var4 = var1[0];
            int var5 = 1;
            int var6 = 8;

            while(var6 > 0 || var5 < var1.length) {
                int var7;
                if(var6 < this.A) {
                    if(var5 < var1.length) {
                        var4 <<= 8;
                        var4 |= var1[var5++] & 255;
                        var6 += 8;
                    } else {
                        var7 = this.A - var6;
                        var4 <<= var7;
                        var6 += var7;
                    }
                }

                var7 = this.D & var4 >> var6 - this.A;
                var6 -= this.A;
                var3.append(this.B[var7]);
            }

            return var3.toString();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    static class _A extends Exception {
        private static final long A = 5741875898375990656L;

        public _A(String var1) {
            super(var1);
        }
    }
}
