import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.IntVector;
import org.renjin.sexp.SEXP;

public class ByteImageVector extends IntVector {
  private byte[] array;
  private boolean signed;
  private boolean little;

  public ByteImageVector(byte[] array, boolean signed, boolean little, AttributeMap attributeMap) {
    super(attributeMap);
    this.array = array;
    this.signed = signed;
    this.little = little;
  }

  public int length() {
    return array.length;
  }

  public int getElementAsInt(int i) {
    if(signed) {
      return array[i];
    } else {
      return ((int)array[i]) & 0xFF;
    }
  }

  public boolean isConstantAccessTime() {
    return true;
  }

  protected SEXP cloneWithNewAttributes(AttributeMap attributes) {
    return new ByteImageVector(array, signed, attributes);
  }
}
