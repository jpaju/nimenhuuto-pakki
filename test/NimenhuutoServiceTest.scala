import java.time.LocalDate

class NimenhuutoServiceTest extends munit.FunSuite:
  test("listEvents - limits by count"):
    val events  = List(
      event("1", "2025-01-10T20:00"),
      event("2", "2025-01-09T20:00"),
      event("3", "2025-01-08T20:00")
    )
    val client  = StubNimenhuutoClient(events = events)
    val service = NimenhuutoService(client)

    val result = service.listEvents(EventFilter.ByCount(2))

    assertEquals(result, events.take(2))

  test("listEvents - filters events newer than date"):
    val events  = List(
      event("1", "2025-01-10T20:00"),
      event("2", "2025-01-09T20:00"),
      event("3", "2025-01-08T20:00")
    )
    val client  = StubNimenhuutoClient(events = events)
    val service = NimenhuutoService(client)

    val result = service.listEvents(EventFilter.NewerThan(LocalDate.parse("2025-01-09")))

    assertEquals(result, events.take(2))

  test("listEvents - filters events by date range"):
    val events  = List(
      event("1", "2025-01-10T20:00"),
      event("2", "2025-01-09T20:00"),
      event("3", "2025-01-08T20:00"),
      event("4", "2025-01-07T20:00")
    )
    val client  = StubNimenhuutoClient(events = events)
    val service = NimenhuutoService(client)

    val filter = EventFilter.DateRange(LocalDate.parse("2025-01-08"), LocalDate.parse("2025-01-09"))
    val result = service.listEvents(filter)

    assertEquals(result, List(events(1), events(2)))

  test("fetchEventAttendances - limits by count"):
    val responses   = attendanceResponses(in = List("Alice"))
    val attendances = List(
      EventAttendance(event("1", "2025-01-10T20:00"), responses),
      EventAttendance(event("2", "2025-01-09T20:00"), responses),
      EventAttendance(event("3", "2025-01-08T20:00"), responses)
    )
    val client      = StubNimenhuutoClient(eventAttendances = attendances)
    val service     = NimenhuutoService(client)

    val result = service.fetchEventAttendances(EventFilter.ByCount(2))

    assertEquals(result, attendances.take(2))

  test("fetchEventAttendances - filters events newer than date"):
    val responses   = attendanceResponses(in = List("Alice"))
    val attendances = List(
      EventAttendance(event("1", "2025-01-10T20:00"), responses),
      EventAttendance(event("2", "2025-01-09T20:00"), responses),
      EventAttendance(event("3", "2025-01-08T20:00"), responses)
    )
    val client      = StubNimenhuutoClient(eventAttendances = attendances)
    val service     = NimenhuutoService(client)

    val result = service.fetchEventAttendances(EventFilter.NewerThan(LocalDate.parse("2025-01-09")))

    assertEquals(result, attendances.take(2))

  test("fetchEventAttendances - filters events by date range"):
    val responses   = attendanceResponses(in = List("Alice"))
    val attendances = List(
      EventAttendance(event("1", "2025-01-10T20:00"), responses),
      EventAttendance(event("2", "2025-01-09T20:00"), responses),
      EventAttendance(event("3", "2025-01-08T20:00"), responses),
      EventAttendance(event("4", "2025-01-07T20:00"), responses)
    )
    val client      = StubNimenhuutoClient(eventAttendances = attendances)
    val service     = NimenhuutoService(client)

    val filter = EventFilter.DateRange(LocalDate.parse("2025-01-08"), LocalDate.parse("2025-01-09"))
    val result = service.fetchEventAttendances(filter)

    assertEquals(result, List(attendances(1), attendances(2)))

class StubNimenhuutoClient(
    responses: AttendanceResponses = attendanceResponses(),
    events: List[Event] = Nil,
    eventAttendances: List[EventAttendance] = Nil,
    players: List[Player] = Nil
) extends NimenhuutoClient:
  def fetchAttendanceResponses(eventId: String): AttendanceResponses = responses
  def fetchEvents(): Iterator[Event]                                 = events.iterator
  def fetchEventAttendances(): Iterator[EventAttendance]             = eventAttendances.iterator
  def fetchPlayers(): List[Player]                                   = players
