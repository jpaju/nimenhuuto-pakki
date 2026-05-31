# Product Requirements: Season bill invoicing

## Problem

The toolkit already computes a fair, attendance-based distribution: each player's share of the season's bill. But turning that distribution into the invoice the team actually receives is still manual. The organizer hand-copies each player's share into a Google Sheets invoice template and adds a display name and a reference number for each player.

This hand-off is tedious and error-prone. Values are copied cell by cell, reference numbers are looked up and matched by hand, and mistakes slip through: this season two different players ended up with the same reference number (10168), which would misattribute their payments.

Once the invoice is ready, the organizer emails it to the players. Not everyone on the roster has taken part this season, so the recipients should be only the players the bill is split between, but today that list is assembled by hand and risks blindly including the whole roster.

## Goal

Get from a computed distribution to a send-ready invoice (per-player rows of full name, reference number, and share) together with the matching list of recipient emails, with as little manual transcription as possible and without copy/paste errors.

## Who it's for

The same organizer who runs the distribution and maintains the Google Sheets invoice template, and who is responsible for collecting money from the team.

## What success looks like

- Each player's row carries their full first and last name, their reference number, and their share.
- Reference numbers are unique within an invoice, so no two players share one.
- The organizer transfers the result into the Google Sheets template without retyping values or hand-matching reference numbers.
- The organizer gets a copyable list of emails for exactly the players the bill is split between (those in the distribution), not the whole roster.
- Nice to have: reference numbers stay stable for a player across seasons (the same player gets the same number each time).

## Out of scope

- Computing the distribution itself (covered by the season bill distribution feature).
- Collecting or transferring money.
- The exact delivery mechanism (clipboard, CSV/TSV export, direct Google Sheets write), deferred to solution design.
- Generating reference numbers ourselves.

## Open questions for solution design

- The distribution is keyed by short name, while full names and emails live with the roster (long name). Both the invoice rows and the recipient email list need data from the roster, so the short-name / long-name join has to be resolved here.

## Candidate directions (parked, not requirements)

Ideas to explore in the solution phase, recorded for illustration only:

- A paste-ready copy format whose columns match the Google Sheets invoice template layout, so a single paste lands in the right cells.
- Accept a pool of reference numbers as input and match them to players.
- A copyable email recipient list as a separate output.
