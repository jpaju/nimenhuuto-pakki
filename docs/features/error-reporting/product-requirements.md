# Product Requirements: Error reporting

## Problem

The toolkit reads team data from Nimenhuuto and reports on it. Sometimes it cannot actually read all the data it needs, but it still finishes as if everything was fine, showing empty or partial results. There is nothing to tell the user apart a real "there is no data" answer from a "we could not get the data" failure.

This has happened in practice: the session id the toolkit uses to read Nimenhuuto expires from time to time, and when it does the toolkit just returns empty output. The same can happen if Nimenhuuto changes its HTML so the toolkit can no longer parse the data out of it. In either case the user is left guessing whether the team genuinely had no events, or whether the run was broken and the result should not be trusted.

The user has no signal that the result is incomplete and that they need to act before relying on it.

## Goal

When the toolkit cannot fully read the data a command needs, it should tell the user the result is incomplete and stop, instead of quietly showing empty or partial results as if they were complete.

## Who it's for

The user running the toolkit, who is technical and just needs an unambiguous signal that a run did not produce trustworthy results.

## What success looks like

- When the toolkit cannot read the data a command depends on, it reports that the result is incomplete instead of showing empty or partial output.
- A reported failure stops the command, rather than letting it finish as if it had succeeded.
- The message is clear enough that the user understands the result cannot be trusted and that they need to act. Polished, end-user-friendly wording is not required.

## Out of scope

- Recovering automatically or retrying on the user's behalf.
- Distinguishing a genuinely empty but valid result from a failure in cases where the two are truly indistinguishable; the aim is to catch the cases that are detectable.
