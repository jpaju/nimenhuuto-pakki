# Nimenhuuto pakki

Toolkit (aka pakki) for scraping data from [nimenhuuto.com](https://nimenhuuto.com)

Currently only scrapes player attendance/registration data from events.

## Usage

### Environment variables

- `BASE_URL` - your team's nimenhuuto URL (e.g. `https://yourteam.nimenhuuto.com`)
- `NIMENHUUTO_SESSION_ID` - your `_session_id` cookie from browser dev tools after logging in

### Commands

```bash
# list events from archive
scala-cli run app -- list-events

# show attendance for a single event
scala-cli run app -- show-event "https://yourteam.nimenhuuto.com/events/12345"

# show attendance for all archived events
scala-cli run app -- count-attendance

# show help
scala-cli run app -- help
```

## Development

Built with [Scala 3](https://scala-lang.org/), [scala-cli](https://scala-cli.virtuslab.org/), and [scala-scraper](https://github.com/ruippeixotog/scala-scraper#browsers)

All required tools are available in nix development shell, activate with `nix develop`.

## TODO

- [x] Discover all events automatically
- [ ] Calculate attendance for every player across multiple events
- [ ] Error handling
  - When login is not successful
  - When page cannot be read
- [ ] Support filtering events by dates
- [ ] Add concurrency, rate limits?
