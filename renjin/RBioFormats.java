import loci.common.DataTools;
import loci.formats.DimensionSwapper;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.SEXP;

import java.io.IOException;

public class RBioFormats {

  private static DimensionSwapper reader;

  public static SEXP readPixels(int i, int x, int y, int w, int h, boolean normalize) throws FormatException, IOException {
    int pixelType = reader.getPixelType();
    int size = w * h * FormatTools.getBytesPerPixel(pixelType) * reader.getRGBChannelCount();

    byte[] buf = new byte[size];

    return new

    reader.openBytes(i, buf, x, y, w, h);

    int bpp = FormatTools.getBytesPerPixel(pixelType);
    boolean fp = FormatTools.isFloatingPoint(pixelType);
    boolean little = reader.isLittleEndian();



    if (normalize)
      return normalizedDataArray(buf, bpp, fp, little, pixelType);
    else
      return rawDataArray(buf, bpp, FormatTools.isSigned(pixelType), fp, little);
  }


  private static Object rawDataArray(byte[] b, int bpp, boolean signed, boolean fp, boolean little) {
    // unsigned types need to be stored in a longer signed type
    int type = signed ? bpp : bpp * 2;
    int len = b.length / bpp;

    // int8
    // convert bytes to shorts in order to have integer rather than raw vector in R
    if(fp) {
    if (bpp == 1) {
      return new ByteImageVector(b, signed, AttributeMap.EMPTY);
    } else if(bpp == 2)) {
      return new ShortImageVector(b, signed, AttributeMap.EMPTY);
    } else if(bpp == 4) {
        return new
      }
    // uint8, int16
    else if (type == 2) {
      return new Int16ImageVector(b, bpp, little, AttributeMap.EMPTY);
      short[] s = new short[len];
      for (int i=0, j=0; i<len; i++, j+=bpp) {
        s[i] = DataTools.bytesToShort(b, j, bpp, little);
      }
      return s;
    }
    // float
    else if (type == 4 && fp) {
      float[] f = new float[len];
      for (int i=0, j=0; i<len; i++, j+=bpp) {
        f[i] = DataTools.bytesToFloat(b, j, bpp, little);
      }
      return f;
    }
    // uint16
    else if (type == 4 && !signed) {
      int[] l = new int[len];
      for (int i=0, j=0; i<len; i++, j+=bpp) {
        l[i] = DataTools.bytesToInt(b, j, bpp, little);
      }
      return l;
    }
    // int32
    // we cannot use 32bit ints for signed values because
    // the minimal int value in Java -2^31 = -2147483648 represents NA in R
    // https://github.com/s-u/rJava/issues/39#issuecomment-72207912
    else if (type == 4) {
      double[] d = new double[len];
      for (int i=0, j=0; i<len; i++, j+=bpp) {
        d[i] = (double) DataTools.bytesToInt(b, j, bpp, little);
      }
      return d;
    }
    // double
    else if (type == 8 && fp) {
      double[] d = new double[len];
      for (int i=0, j=0; i<len; i++, j+=bpp) {
        d[i] = DataTools.bytesToDouble(b, j, bpp, little);
      }
      return d;
    }
    // uint32
    // use Java long which is returned as double in R
    else if (type == 8) {
      long[] l = new long[len];
      for (int i=0, j=0; i<len; i++, j+=bpp) {
        l[i] = DataTools.bytesToLong(b, j, bpp, little);
      }
      return l;
    }
    return null;
  }
}
