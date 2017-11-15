import loci.formats.DimensionSwapper;
import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.IntVector;
import org.renjin.sexp.SEXP;

/**
 * Created by alex on 15-11-17.
 */
public class RollingCacheImageVector extends IntVector {

  private DimensionSwapper reader;
  private final int length;

  private ImageChunk currentChunk = null;

  public RollingCacheImageVector(DimensionSwapper reader) {
    this.reader = reader;
    this.length = reader.getSizeX() * reader.getSizeY() * reader.getSizeZ();

  }

  public int length() {
    return length
  }

  private ImageChunk loadChunk(int index) {
    int row = index / reader.getSizeY();
    int col = index % reader.getSizeY();

    int chunkX = col / reader.getOptimalTileWidth();
    int chunkY = row / reader.getOptimalTileHeight();

    int chunkStartX = chunkX * reader.getOptimalTileWidth();
    int chunkStartY = chunkY * reader.getOptimalTileHeight();

    byte[] buffer = new byte[0];

    reader.openBytes(0, buffer, chunkStartX, chunkStartY, reader.getOptimalTileWidth(), reader.getOptimalTileHeight());

    return new ImageChunk(buffer,


  }

  public int getElementAsInt(int i) {

    if(currentChunk != null) {
      if(currentChunk.contains(i)) {
        return currentChunk.get(i);
      }
    }

    currentChunk = loadChunk(i);

    reader.o

    throw new UnsupportedOperationException("TODO");
  }

  private ImageChunk loadChunk(int i) {

  }

  public boolean isConstantAccessTime() {
    throw new UnsupportedOperationException("TODO");
  }

  protected SEXP cloneWithNewAttributes(AttributeMap attributes) {
    throw new UnsupportedOperationException("TODO");
  }
}
