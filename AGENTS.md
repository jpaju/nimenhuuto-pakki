# Agents guide

Toolkit for scraping data from nimenhuuto.com (Finnish team management/RSVP platform).
Parses player registration status (in/out/unknown) from event pages.

## Commands

```bash
scala-cli run app -- <args>       # run
scala-cli fmt app                 # format
scala-cli test app                # run all tests
scala-cli test app -- "*MyTest*"  # run single test
```

## Code style

- Scala 3 with scala-cli directives (`//> using`)
- Format with scalafmt (max 120 cols, align.preset=most, Scala 3 braceless syntax)
- Prefer immutable vals, case classes for data
- Use `def` for top-level functions (no object wrappers needed in scala-cli)
- Explicit return types on public functions
- Group imports: stdlib, then deps, then local
- Error handling: fail fast with `System.exit(1)` for CLI errors
- No comments unless explicitly requested
