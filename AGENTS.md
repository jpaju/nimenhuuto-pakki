# Agents guide

Toolkit for scraping data from nimenhuuto.com (Finnish team management/RSVP platform).
Parses player registration status (in/out/unknown) from event pages.

## Commands

```bash
scala-cli run . -- <args>       # run
scala-cli fmt .                 # format
scala-cli test .                # run all tests
scala-cli test . -- "*MyTest*"  # run single test
```

## Docs

Feature docs live together under `docs/features/<feature>/`, e.g.
`product-requirements.md` and `technical-design.md` (engineering approach + progress).

## Code style

- Scala 3 with scala-cli directives (`//> using`)
- Format with scalafmt (max 120 cols, align.preset=most, Scala 3 braceless syntax)
- Prefer immutable vals, case classes for data
- Explicit return types on public functions
- Group imports: stdlib, then deps, then local
- Error handling: fail fast with `System.exit(1)` for CLI errors
- No comments unless explicitly requested
