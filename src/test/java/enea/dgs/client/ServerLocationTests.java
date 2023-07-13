package enea.dgs.client;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetAddress;

import static org.testng.Assert.*;

public class ServerLocationTests {

    InetAddress inetAddress;

    @Test
    public void testGetName() {
        ServerLocation serverLocationSpy = Mockito.spy(new ServerLocation("localhost", inetAddress, 4666));
        Assert.assertEquals(serverLocationSpy.getName(), "localhost");
    }
}