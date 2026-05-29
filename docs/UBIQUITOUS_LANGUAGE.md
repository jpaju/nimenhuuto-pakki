# Ubiquitous Language

## Source platform

| Term           | Definition                                                              | Aliases to avoid          |
| -------------- | ----------------------------------------------------------------------- | ------------------------- |
| **Nimenhuuto** | The external team management / RSVP platform that this toolkit scrapes. | "the site", "the service" |

## Events & attendance

| Term                    | Definition                                                                                 | Aliases to avoid                 |
| ----------------------- | ------------------------------------------------------------------------------------------ | -------------------------------- |
| **Event**               | A scheduled team occurrence on Nimenhuuto, such as a training or a match.                  | "game", "session", "happening"   |
| **Attendance response** | A Player's RSVP to an Event: in, out, or unknown.                                          | "registration", "RSVP", "status" |
| **Attendance**          | A single in response for one Player at one Event; the countable unit for stats and splits. | "registration", "participation"  |
| **Event attendance**    | An Event together with the Players in each Attendance response state.                      | "event record", "event result"   |

## People

| Term           | Definition                                                                 | Aliases to avoid     |
| -------------- | -------------------------------------------------------------------------- | -------------------- |
| **Player**     | A person on the Roster.                                                    | "member", "user"     |
| **Roster**     | The full list of Players, together with their contact details.             | "squad", "directory" |
| **Short name** | The Player identifier as shown in Event attendance lists.                  | "name", "handle"     |
| **Long name**  | The Player's full name as shown on the Roster.                             | "name", "full name"  |

Short name and Long name are kept distinct because Nimenhuuto exposes them in different places with no reliable join key; collapsing them would invent matches that do not exist in the source.

## Bill splitting

| Term             | Definition                                                                        | Aliases to avoid                   |
| ---------------- | --------------------------------------------------------------------------------- | ---------------------------------- |
| **Bill**         | The single total cost supplied as input to be split across Players.               | "invoice", "total", "season cost"  |
| **Unit cost**    | Cost per Attendance.                                                              | "rate", "per-event price"          |
| **Share**        | What one Player owes.                                                             | "portion", "due", "owe"            |
| **Distribution** | The result of splitting a Bill: the Unit cost together with each Player's Share.  | "split", "breakdown", "allocation" |

## Relationships

- An **Event** groups **Players** by their **Attendance response**.
- An **Attendance** is one in response by one **Player** at one **Event**.
- A **Distribution** splits one **Bill** across **Players** by their **Attendance** counts, yielding a **Unit cost** and a **Share** per Player.

## Example dialogue

> **Dev:** "For the split, what defines the set of Events we count?"

> **Domain expert:** "Whatever **Events** the user picks are the input for that **Bill**."

> **Dev:** "And only an in response counts as an **Attendance**?"

> **Domain expert:** "Right. Out and unknown are **Attendance responses** but not **Attendances**. Only in responses feed the **Distribution**."

> **Dev:** "Players appear as **Short names** on **Events** and **Long names** on the **Roster**. Do we join them for the **Distribution**?"

> **Domain expert:** "Not today. The **Distribution** is keyed by **Short name** because that is what the Event attendance lists give us reliably; the **Roster** uses **Long names** for contact info and we deliberately keep the two apart."

## Flagged ambiguities

- **Short name vs Long name** refer to the same Player but come from different places with no reliable join. Keep both as first-class terms; do not collapse to "name".
- **"Attendance"** is used both for an Event's response record and for a per-Player count of in responses. Recommendation: **Event attendance** for the record, **Attendance** for the countable unit, **Attendances** for counts and aggregates.
- **"Registration"** is sometimes used for what is really an **Attendance response** (the state) plus an **Attendance** (a counted in). Standardize on those two terms; retire "registration".
- **"Bill" vs "total cost" vs "season cost"** appear interchangeably in bill-splitting discussions. Standardize on **Bill** for the input amount; "total cost" only as descriptive prose.

## Candidate terms

Concepts under discussion but not yet first-class in the model.

| Term       | Proposed definition                            | Status                                                                      |
| ---------- | ---------------------------------------------- | --------------------------------------------------------------------------- |
| **Season** | The set of Events selected for one Bill split. | Not modeled explicitly today; expressed implicitly by how Events are picked. |
