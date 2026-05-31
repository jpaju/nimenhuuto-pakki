# Product Requirements: Attendance tampering

## Problem

The bill is split by attendance: the more events a player attended, the larger their share. That creates an incentive to cancel an attendance after an event has already happened, in order to shrink one's own share of the bill.

The toolkit splits the bill from whatever the attendance shows at the time it reads the data. A cancellation made after the fact looks no different from a player who was never there: it silently lowers that player's share and quietly raises everyone else's, and nothing surfaces that it happened. The organizer has no easy way to notice it.

Nimenhuuto keeps a per-event audit log that records when attendance changes were made, so the information needed to spot these after-the-fact cancellations exists; it is just not surfaced anywhere today. That audit log is only available for one month after the event, so a cancellation can only be detected within that window.

## Goal

Let the organizer see when a player cancelled their attendance after an event occurred, so they can review whether a bill split was influenced by an after-the-fact change.

## Who it's for

The organizer responsible for collecting money from the team, the same person who runs the bill distribution.

## What success looks like

- When a player's attendance for an event was cancelled after the event occurred, the toolkit surfaces it for the organizer to review.
- The flag identifies the player and the event involved.
- Flags are presented as cases to review, not as accusations or verdicts. A flag does not by itself mean wrongdoing: a player may have simply forgotten to cancel in time and corrected it late.

## Out of scope

- Judging intent or deciding whether a flagged cancellation is a real violation.
- Automatically changing or recomputing the bill in response to a flag.
- Tracking attendance changes made before the event; only cancellations after the event occurred are in scope.
- Detecting cancellations once the event's audit log is no longer available.
