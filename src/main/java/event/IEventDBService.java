package event;

import java.time.LocalDate;
import java.util.List;

public interface IEventDBService {
    void addEvent(Event event);
    void deleteEvent(int idEvent);
    void joinEvent(int idEvent,int idReader);
    int ifReaderJoined(int idEvent, int idReader);
    void resignEvent(int idEvent, int idReader);
    Event readEvent(int idEvent);
    List<Event> getAllEventsFromDB(int sort1, int sort2);
    List<Event> getAllEventsFromDB();
    List<Event> getAllEventsForUser(int readerId);
    void updateEventInDB(int idEvent, String title, LocalDate dateEvent, int imgId, String shortDescription);
    int getParticipants(int idEvent);
}
