class NimenhuutoService(client: NimenhuutoClient):
  def fetchAttendanceResponses(eventId: String): AttendanceResponses =
    client.fetchAttendanceResponses(eventId)

  def listEvents(count: Int): List[Event] =
    client
      .fetchEvents()
      .take(count)
      .toList

  def fetchEventAttendances(count: Int): List[EventAttendance] =
    client
      .fetchEventAttendances()
      .take(count)
      .toList
