/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package euromillions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 *
 * @author ico0
 */
public class DipTest {
    
	private Dip dip;

    
    @BeforeEach
    public void setUp() {
    	dip = new Dip( new int[] {10,20,30,40,50}, new int[] {1,2});
    }
    
    @AfterEach
    public void tearDown() {
    	dip = null;
    }

   
    @Test
    public void testConstructorFromBadArrays() {
    	// todo: instantiating a dip passing invalid arrays should raise an exception
        assertThrowsExactly(
            IllegalArgumentException.class,
    	    () -> new Dip( new int[] {0,20,30,40,50}, new int[] {1,2}),
            "Constructor with bad arrays (Illegal number chosen) not returning IllegalArgumentException"
        );
        assertThrowsExactly(
            IllegalArgumentException.class,
    	    () -> new Dip( new int[] {10,20,30,40,51}, new int[] {1,2}),
            "Constructor with bad arrays (Illegal number chosen) not returning IllegalArgumentException"
        );
        assertThrowsExactly(
            IllegalArgumentException.class,
    	    () -> new Dip( new int[] {10,20,30,40,50}, new int[] {13,2}),
            "Constructor with bad arrays (Illegal Star chosen) not returning IllegalArgumentException"
        );
        assertThrowsExactly(
            IllegalArgumentException.class,
    	    () -> new Dip( new int[] {10,20,30,40,50}, new int[] {11,0}),
            "Constructor with bad arrays (Illegal Star chosen) not returning IllegalArgumentException"
        );
        assertThrowsExactly(
            IllegalArgumentException.class,
    	    () -> new Dip( new int[] {10,30,40,50}, new int[] {11,1}),
            "Constructor with bad arrays (Not respecting number of numbers) not returning IllegalArgumentException"
        );
        assertThrowsExactly(
            IllegalArgumentException.class,
    	    () -> new Dip( new int[] {10,20, 30,40,50}, new int[] {11}),
            "Constructor with bad array (Not respecting number of stars) not returning IllegalArgumentException"
        );
    }
     
    @Test
    @DisplayName("pretty format of a dip")
    public void testPrettyFormat() {

        // note: correct the implementation, not the test ;)
        String result = dip.format();
        assertEquals( "N[ 10 20 30 40 50] S[  1  2]", result, "format as string: formatted string not as expected. ");        
    }

}
