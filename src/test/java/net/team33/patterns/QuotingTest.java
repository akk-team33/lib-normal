package net.team33.patterns;

import org.junit.Assert;
import org.junit.Test;

public class QuotingTest {

    private Quoting subject = new Quoting();

    @Test
    public final void quotePrimary() {
        Assert.assertEquals(
                "\"dies ist ein String\"",
                subject.quote("dies ist ein String")
        );
    }

    @Test
    public final void quoteLiterals() {
        Assert.assertEquals(
                "\"\\tdies\\rist\\nein\\\"\\bString\\\\'\\f\"",
                subject.quote("\tdies\rist\nein\"\bString\\\'\f")
        );
    }

    @Test
    public final void quoteLowValues() {
        Assert.assertEquals(
                "\"\\000\\001\\002\\003\\004\\005\\006\\007\\b\\t\\n\\013\\f\\r\\016O\\u0090\\u00D1\\022S\\u0094\"",
                subject.quote("\0\1\2\3\4\5\6\7\10\11\12\13\14\15\16\117\220\321\22\123\224")
        );
    }

    @Test
    public final void quoteHighValues() {
        Assert.assertEquals(
                "\"\\u00E0\\u00F0\\u00E1\\u00F1\\u5A7B\"",
                subject.quote("àðáñ婻")
        );
    }
}