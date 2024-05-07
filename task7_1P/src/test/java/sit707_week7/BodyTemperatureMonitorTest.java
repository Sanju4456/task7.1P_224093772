package sit707_week7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

public class BodyTemperatureMonitorTest {

	@Test
    public void testStudentIdentity() {
        String studentID = "224093772"; // Update with the correct student ID
        assertEquals("Student ID is incorrect", "224093772", studentID);
    }

	 @Test
	    public void testStudentName() {
	        String studentName = "sanju"; // Update with the correct student name
	        assertNotNull("Student name is null", studentName);
	    }
	
	@Test
    public void testReadTemperatureNegative() {
        // Configure mock to return a negative temperature value
        when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);
        
        // Create an instance of BodyTemperatureMonitor with the mock TemperatureSensor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Call the method to be tested
        double temperature = bodyTemperatureMonitor.readTemperature();

        // Assert that the temperature is negative
        assertTrue("Temperature should be negative", temperature < 0);
    }
	@Test
	public void testReadTemperatureZero() {
	    // Create a mock TemperatureSensor
	    TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
	    // Configure the mock to return zero
	    when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);

	    // Create an instance of BodyTemperatureMonitor with the mock TemperatureSensor
	    BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

	    // Call the method to be tested
	    double temperature = bodyTemperatureMonitor.readTemperature();

	    // Assert that the temperature is zero
	    assertEquals("Temperature should be zero", 0.0, temperature, 0.001);
	}
	
	  @Test
	    public void testReadTemperatureNormal() {
	        // Configure mock to return a normal temperature value
	        when(temperatureSensor.readTemperatureValue()).thenReturn(37.0);
	        
	        // Create an instance of BodyTemperatureMonitor with the mock TemperatureSensor
	        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

	        // Call the method to be tested
	        double temperature = bodyTemperatureMonitor.readTemperature();

	        // Assert that the temperature is within the normal range
	        assertEquals("Temperature should be normal", 37.0, temperature, 0.001);
	    }
	@Test
    public void testReadTemperatureAbnormallyHigh() {
        // Configure mock to return an abnormally high temperature value
        when(temperatureSensor.readTemperatureValue()).thenReturn(200.0);
        
        // Create an instance of BodyTemperatureMonitor with the mock TemperatureSensor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Call the method to be tested
        double temperature = bodyTemperatureMonitor.readTemperature();

        // Assert that the temperature is within the normal range
        assertTrue("Temperature should be abnormally high", temperature > 100);
    }
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
//	@Test
//	public void testReportTemperatureReadingToCloud() {
//		// Mock reportTemperatureReadingToCloud() calls cloudService.sendTemperatureToCloud()
//		
//		Assert.assertTrue("Not tested", false);
//	}
	
	
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
//	@Test
//	public void testInquireBodyStatusNormalNotification() {
//		Assert.assertTrue("Not tested", false);
//	}
	
	/*
	 * CREDIT or above level students, Remove comments. 
	 */
//	@Test
//	public void testInquireBodyStatusAbnormalNotification() {
//		Assert.assertTrue("Not tested", false);
//	}
	
	// Instantiate the BodyTemperatureMonitor with mocked dependencies
    TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
    CloudService cloudService = mock(CloudService.class);
    NotificationSender notificationSender = mock(NotificationSender.class);
    BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);

    @Test
    public void testReadTemperatureNegative1() {
        // Stub temperatureSensor to return negative temperature reading
        when(temperatureSensor.readTemperatureValue()).thenReturn((double) -1);
        // Call the method to be tested
        int temperature = (int) bodyTemperatureMonitor.readTemperature();
        // Assert the result
        assertEquals(-1, temperature);
    }

    @Test
    public void testReadTemperatureZero1() {
        // Stub temperatureSensor to return 0 temperature reading
        when(temperatureSensor.readTemperatureValue()).thenReturn((double) 0);
        // Call the method to be tested
        int temperature = (int) bodyTemperatureMonitor.readTemperature();
        // Assert the result
        assertEquals(0, temperature);
    }

    @Test
    public void testReadTemperatureNormal1() {
        // Stub temperatureSensor to return normal temperature reading
        when(temperatureSensor.readTemperatureValue()).thenReturn((double) 36);
        // Call the method to be tested
        int temperature = (int) bodyTemperatureMonitor.readTemperature();
        // Assert the result
        assertEquals(36, temperature);
    }

    @Test
    public void testReadTemperatureAbnormallyHigh1() {
        // Stub temperatureSensor to return abnormally high temperature reading
        when(temperatureSensor.readTemperatureValue()).thenReturn((double) 50);
        // Call the method to be tested
        int temperature = (int) bodyTemperatureMonitor.readTemperature();
        // Assert the result
        assertEquals(50, temperature);
    }

   
    @Test
    public void testInquireBodyStatus_Normal() {
        // Arrange
        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("NORMAL");
        
        // Act
        bodyTemperatureMonitor.inquireBodyStatus();

        // Assert
        verify(notificationSender, times(1)).sendEmailNotification(any(Customer.class), eq("Thumbs Up!"));
    }

    @Test
    public void testInquireBodyStatus_Abnormal() {
        // Arrange
        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("ABNORMAL");
        
        // Act
        bodyTemperatureMonitor.inquireBodyStatus();

        // Assert
        verify(notificationSender, times(1)).sendEmailNotification(any(FamilyDoctor.class), eq("Emergency!"));
    }
}
