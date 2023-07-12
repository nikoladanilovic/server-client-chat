package enea.dgs.server;

import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Duration;

/**
 * Unit tests for the {@link KKMultiServerThread} class.
 */
public class KKMultiServerThreadTests {

    /**
     * Verifies whether the last known location of other clients is reported with delay to the
     * newly connected client.
     */
    @Test
    public void testLastKnowLocationNotify() throws IOException {
        LastKnownPlayerLocations.getInstance()
                .onMessageReceived("other_client","last_know_location_test");

        StringBuilder stringBuilder = new StringBuilder();

        // mockito
        OutputStream outputStreamMock = Mockito.mock(OutputStream.class);
        Mockito.doAnswer(invocation -> {
            int argument = invocation.getArgument(0);
            stringBuilder.append((char) argument);
            return null;
        }).when(outputStreamMock).write(Mockito.any(int.class));
        Mockito.doCallRealMethod().when(outputStreamMock).write(Mockito.any(byte[].class),
                Mockito.any(int.class), Mockito.any(int.class));

        // anonymous class - cleaner in this case
//        OutputStream outputStreamMock = new OutputStream() {
//            @Override
//            public void write(int x) throws IOException {
//                stringBuilder.append((char) x);
//            }
//        };

        // mock of input stream, need for buffered reader inside server thread to not throw exception
        InputStream inputStreamMock = Mockito.mock(InputStream.class);
        Mockito.when(inputStreamMock.read(Mockito.any(byte[].class), Mockito.any(int.class),
                Mockito.any(int.class))).thenReturn(1);

        Socket clientSocket = Mockito.mock(Socket.class);
        Mockito.when(clientSocket.getInputStream()).thenReturn(inputStreamMock);
        Mockito.when(clientSocket.getOutputStream()).thenReturn(outputStreamMock);

        KKMultiServerThread kkMultiServerThread = new KKMultiServerThread(clientSocket, 666L);
        kkMultiServerThread.start();

        Awaitility.await()
                .atLeast(Duration.ofMillis(1000L))
                .atMost(Durations.TWO_SECONDS)
                .until(() -> stringBuilder.toString()
                        .endsWith("\nother_client!last_know_location_test\n"));
    }

}