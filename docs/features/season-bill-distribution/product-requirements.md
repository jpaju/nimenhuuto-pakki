# Product Requirements: Season bill distribution

## Problem

Every season the team shares a single bill (hall rent, equipment, referees, etc.). Distributing that bill evenly across players is unfair: some players attend almost every event, others only a few. People who show up rarely should not pay the same as people who are there every week.

Today the distribution is computed by hand outside the tool, even though the tool already knows each player's attendances. That manual work is tedious and error-prone, and the result is hard to justify to the team.

## Goal

Let the organizer distribute a season's bill across players based on each player's attendances, so that everyone pays a fair share and the math is transparent and trusted.

## Who it's for

The person responsible for collecting money from the team (the organizer), who already uses the tool to track attendances.

## What success looks like

- The organizer enters the season's bill once.
- The tool produces each player's share.
- A player with more attendances pays proportionally more than one with fewer.
- The distribution is clear enough that the organizer can forward it to the team without further explanation or hand calculation.

## Out of scope

- Collecting or transferring money (this only computes the distribution).
- Multiple currencies.
- Per-event or variable pricing; the input is one bill for the season.
