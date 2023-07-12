package enea.dgs.client;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class EventBusTests {

    @Test
    public void testSendEvent() {
        EventBus eventBusSpy = Mockito.spy(EventBus.getInstance());
        try (MockedStatic<EventBus> randomMock = Mockito.mockStatic(EventBus.class)) {
            randomMock.when(EventBus::getInstance).thenReturn(eventBusSpy);
            Mockito.doAnswer(invocation -> {
                EventBus.Event argumentFirst = invocation.getArgument(0);
                EventMessage argumentSecond = invocation.getArgument(1);
                System.out.println(
                        "We are inside sendEvent method. " + argumentFirst + " -> " + argumentSecond.toString());

                return null;
            }).when(eventBusSpy).sendEvent(Mockito.any(), Mockito.any(EventMessage.class));

            // inside this try block every call to the EventBus singleton, from any implementation (not just here)
            // will trigger static mock
            EventBus.getInstance().sendEvent(EventBus.Event.MOVED_AVATAR, EventMessage.of("test1"));
            EventBus.getInstance().sendEvent(null, EventMessage.of("test2"));
        }
        // will not be part of the static mock
        EventBus.getInstance().sendEvent(EventBus.Event.MOVED_ENEMY, EventMessage.of("test3"));
    }

}