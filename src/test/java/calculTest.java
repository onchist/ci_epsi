
import org.junit.Test;

import static org.junit.Assert.*;

public class calculTest {

    @Test
    public void additionner() {
    assertEquals(5, calcul.additionner(3,2));
    assertEquals(10,calcul.additionner(5,5));
    assertNotEquals(15,calcul.additionner(3,10));
    }
}