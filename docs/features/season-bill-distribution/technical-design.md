# Technical Design: Season bill distribution

See [product-requirements.md](./product-requirements.md) for the problem statement and goals.

## Approach

Usage-based cost allocation. The bill is divided by the total number of attendances to get a unit cost (cost per attendance), and each player pays the unit cost for every attendance they have:

```
unitCost      = bill / totalAttendances        (rounded up to whole cents)
share(player) = unitCost * attendances(player)
```

Rounding the unit cost up means the collected total is always at least the bill: we never under-collect. Over-collection is bounded by less than one cent per attendance, which is acceptable for a hobby team bill.

The user supplies the bill in one of two ways:

- **Total bill**: a single amount for the whole season.
- **Per-event bill**: a fixed amount per event. The total is derived as `perEventBill * totalEvents`.

Exactly one of the two must be supplied. The derivation runs at the boundary; everything downstream operates on the resulting total. The rendered output always shows both values regardless of which was supplied.

## Building blocks and responsibilities

**Money** — the central type for the whole feature. An exact monetary amount, represented to the cent. It owns all arithmetic (add, subtract, multiply by a count, divide) and comparison, and is the only place that knows how to render an amount for display. Nothing outside it deals in raw numeric types.

**Cost distribution** — the strategy that turns a bill and a set of per-player attendance counts into a distribution. It is the home for the distribution logic and is designed so a different strategy (e.g. exact-to-the-cent) can be added later without changing callers. The distribution carries the unit cost and each player's share. It is generic over the participant identifier; the concrete identity (short name) is supplied by the integration layer.

**Integration layer** — the existing application pipeline (command parsing -> application -> data fetching -> rendering). It is responsible for collecting the bill from the user, turning fetched event attendances into per-player attendance counts, invoking the distribution, and presenting the result. None of the distribution or money logic lives here; it only adapts between the outside world and the domain.

## Interactions

The integration layer parses the user's bill into a money value at the boundary and derives per-player attendance counts from fetched events. It hands both to cost distribution, which returns the distribution. The integration layer then renders the distribution, relying on money for formatting. The distribution returns "no result" when there are no attendances, so the boundary between "valid distribution" and "nothing to distribute" is explicit rather than a silent empty value.

## Rendered output

The render is self-justifying: a reader should be able to verify any share without further explanation. It has two parts: a summary block (bill, cost per attendance, collected total, event range) and a per-player table (player, attendances, share) sorted by attendances descending.

```
--------------------------------------------------
First event: 07.01.2025
Last event: 18.12.2025
Total events: 24
--------------------------------------------------
Per-event bill: 18.75 €
Bill: 450.00 €
Cost per attendance: 14.29 €
Collected: 450.03 €
--------------------------------------------------
╭──────────┬─────────────┬──────────╮
│ Player   │ Attendances │ Share    │
├──────────┼─────────────┼──────────┤
│ Alice    │ 3           │ 42.87 €  │
│ Bob      │ 2           │ 28.58 €  │
│ Jane     │ 2           │ 28.58 €  │
╰──────────┴─────────────┴──────────╯
```

The attendances column makes the share calculation visible (`attendances * cost per attendance = share`). The collected total surfaces the (intentional) over-collection from rounding the unit cost up, instead of hiding it. The event range tells the reader which period the bill covers.

## Gotchas

- **Use an exact decimal type for money, never floating point.** Cent amounts must be exact; floats accumulate representation errors. This is non-negotiable for anything handling money.
- **Two different roundings for two different purposes.** Interpreting user input rounds to the *nearest* cent (neutral). Dividing the bill rounds *up* (so a distribution never under-collects). Conflating them would either bias every parsed amount or let the distribution come up short.
- **Keep the numeric representation hidden inside money.** Exposing it invites ad-hoc arithmetic elsewhere and reintroduces the rounding/precision pitfalls money exists to prevent.

## Open questions

- Where attendance counts come from: reuse the existing stats computation or count independently in the distribution path.
- How the bill is supplied on the command line and where its parsing/validation boundary sits.
- How much detail the rendered distribution should show.
- What the user sees when there are no attendances to distribute.

## Alternatives considered

- **Exact-to-the-cent (largest-remainder) allocation** — distributes leftover cents so shares sum exactly to the bill. Deferred; round-up is simpler and good enough.
- **Newtype/refinement libraries for money** — would give compile-time-checked literals. Deferred; a plain constructor plus a test helper covers current needs without a dependency.

---

## Progress

Design above is stable. This section tracks implementation status.

- [x] Money domain type (parse, arithmetic, comparison, render)
- [x] Cost distribution strategy and distribution result type
- [x] Unit and property-based tests for the core domain
- [x] Derive per-player attendance counts from fetched events
- [x] Command to distribute a bill, taking the bill as input
- [x] Application wiring of the distribution
- [x] Rendering of the distribution
