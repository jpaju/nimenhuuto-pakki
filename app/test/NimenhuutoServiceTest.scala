import java.time.LocalDateTime

class NimenhuutoServiceTest extends munit.FunSuite:
  test("listEvents - limits by count"):
    val events  = List(
      Event("1", "/1", "Harkka", LocalDateTime.parse("2025-01-10T20:00")),
      Event("2", "/2", "Harkka", LocalDateTime.parse("2025-01-09T20:00")),
      Event("3", "/3", "Harkka", LocalDateTime.parse("2025-01-08T20:00"))
    )
    val client  = StubNimenhuutoClient(events = events)
    val service = NimenhuutoService(client)

    val result = service.listEvents(2)

    assertEquals(result, events.take(2))

  test("fetchAttendanceResponses - delegates to client"):
    val responses = AttendanceResponses(List(PlayerName("Alice")), List(PlayerName("Bob")), Nil)
    val client    = StubNimenhuutoClient(responses, Nil)
    val service   = NimenhuutoService(client)

    val result = service.fetchAttendanceResponses("123")

    assertEquals(result, responses)

  test("fetchEventAttendances - limits by count"):
    val responses   = AttendanceResponses(List(PlayerName("Alice")), Nil, Nil)
    val attendances = List(
      EventAttendance(Event("1", "/1", "Harkka", LocalDateTime.parse("2025-01-10T20:00")), responses),
      EventAttendance(Event("2", "/2", "Harkka", LocalDateTime.parse("2025-01-09T20:00")), responses),
      EventAttendance(Event("3", "/3", "Harkka", LocalDateTime.parse("2025-01-08T20:00")), responses)
    )
    val client      = StubNimenhuutoClient(eventAttendances = attendances)
    val service     = NimenhuutoService(client)

    val result = service.fetchEventAttendances(2)

    assertEquals(result, attendances.take(2))

class StubNimenhuutoClient(
    attendanceResponses: AttendanceResponses = AttendanceResponses(Nil, Nil, Nil),
    events: List[Event] = Nil,
    eventAttendances: List[EventAttendance] = Nil
) extends NimenhuutoClient:
  def fetchAttendanceResponses(eventId: String): AttendanceResponses = attendanceResponses
  def fetchEvents(): Iterator[Event]                                 = events.iterator
  def fetchEventAttendances(): Iterator[EventAttendance]             = eventAttendances.iterator
