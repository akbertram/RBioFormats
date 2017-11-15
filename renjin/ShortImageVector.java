import com.google.common.primitives.Shorts;
import org.renjin.repackaged.guava.primitives.Chars;
import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.IntVector;
import org.renjin.sexp.SEXP;

public class ShortImageVector extends IntVector {
  private final byte[] data;
  private final boolean signed;
  private final AttributeMap empty;

  public ShortImageVector(byte[] data, boolean signed, AttributeMap empty) {
    this.data = data;
    this.signed = signed;
    this.empty = empty;
  }


  public int length() {
    return data.length / 2;
  }

  public int getElementAsInt(int i) {
    byte b1 = data[i * 2];
    byte b2 = data[i * 2 + 1];
    if(signed) {
      return Shorts.fromBytes(b1, b2);
    } else {
      return Chars.fromBytes(b1, b2);
    }
  }

  public boolean isConstantAccessTime() {
    return true;
  }

  protected SEXP cloneWithNewAttributes(AttributeMap attributes) {
    return new ShortImageVector(data, signed, attributes);
  }
}
