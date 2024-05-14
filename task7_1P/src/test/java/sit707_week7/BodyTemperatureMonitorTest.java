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

import org.junit.Test;

public class BodyTemperatureMonitorTest {

	 // Your student ID
    private final String studentID = "224093772";
    // Your student name
    private final String studentName = "Sanju";
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
        // Mocking the TemperatureSensor
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        // Configure the mock to return a negative temperature value
        when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        // Creating an instance of BodyTemperatureMonitor with the mock TemperatureSensor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Calling the method to be tested
        double temperature = bodyTemperatureMonitor.readTemperature();

        // Asserting that the temperature is negative
        assertTrue("Temperature should be negative", temperature < 0);

        // Asserting that the getFixedCustomer() and getFamilyDoctor() methods return non-null values
        assertNotNull(bodyTemperatureMonitor.getFixedCustomer());
        assertNotNull(bodyTemperatureMonitor.getFamilyDoctor());
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
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
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
        // Mocking the TemperatureSensor
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        // Configure the mock to return an abnormally high temperature value
        when(temperatureSensor.readTemperatureValue()).thenReturn(200.0);

        // Creating an instance of BodyTemperatureMonitor with the mock TemperatureSensor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Calling the method to be tested
        double temperature = bodyTemperatureMonitor.readTemperature();

        // Asserting that the temperature is abnormally high
        assertTrue("Temperature should be abnormally high", temperature > 100);

        // Asserting that the getFixedCustomer() and getFamilyDoctor() methods return non-null values
        assertNotNull(bodyTemperatureMonitor.getFixedCustomer());
        assertNotNull(bodyTemperatureMonitor.getFamilyDoctor());
    }
    
    @Test
    public void testReportTemperatureReadingToCloud() {
        // Mock cloudService to verify its method is called
        CloudService cloudService = mock(CloudService.class);
        // Create an instance of BodyTemperatureMonitor with the mock CloudService
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, cloudService, null);

        // Call the method to be tested
        bodyTemperatureMonitor.reportTemperatureReadingToCloud(new TemperatureReading());

        // Verify that sendTemperatureToCloud method is called once
        verify(cloudService, times(1)).sendTemperatureToCloud(any(TemperatureReading.class));
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        // Mock cloudService to return NORMAL status
        CloudService cloudService = mock(CloudService.class);
        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("NORMAL");
        // Mock notificationSender
        NotificationSender notificationSender = mock(NotificationSender.class);

        // Create an instance of BodyTemperatureMonitor with the mock CloudService and NotificationSender
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);

        // Call the method to be tested
        bodyTemperatureMonitor.inquireBodyStatus();

        // Verify that sendEmailNotification method is called with appropriate parameters
        verify(notificationSender, times(1)).sendEmailNotification(any(Customer.class), eq("Thumbs Up!"));
    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        // Mock cloudService to return ABNORMAL status
        CloudService cloudService = mock(CloudService.class);
        when(cloudService.queryCustomerBodyStatus(any(Customer.class))).thenReturn("ABNORMAL");
        // Mock notificationSender
        NotificationSender notificationSender = mock(NotificationSender.class);

        // Create an instance of BodyTemperatureMonitor with the mock CloudService and NotificationSender
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);

        // Call the method to be tested
        bodyTemperatureMonitor.inquireBodyStatus();

        // Verify that sendEmailNotification method is called with appropriate parameters
        verify(notificationSender, times(1)).sendEmailNotification(any(FamilyDoctor.class), eq("Emergency!"));
    }
    
    @Test
    public void testGetFixedCustomer() {
        // Create an instance of BodyTemperatureMonitor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, null, null);
        
        // Get the fixed customer
        Customer customer = bodyTemperatureMonitor.getFixedCustomer();
        
        // Assert that the fixed customer is not null
        assertNotNull("Fixed customer should not be null", customer);
    }

    @Test
    public void testGetFamilyDoctor() {
        // Create an instance of BodyTemperatureMonitor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, null, null);
        
        // Get the family doctor
        FamilyDoctor doctor = bodyTemperatureMonitor.getFamilyDoctor();
        
        // Assert that the family doctor is not null
        assertNotNull("Family doctor should not be null", doctor);
    }
    
    @Test
    public void testReadTemperatureNegativeHandling() {
        // Configure mock to return a negative temperature value
        TemperatureSensor temperatureSensor = mock(TemperatureSensor.class);
        when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        // Create an instance of BodyTemperatureMonitor with the mock TemperatureSensor
        BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Call the method to be tested
        double temperature = bodyTemperatureMonitor.readTemperature();

        // Assert that the temperature is negative
        assertTrue("Temperature should be negative", temperature < 0);
    }

}
