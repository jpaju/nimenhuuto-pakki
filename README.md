# Nimenhuuto pakki

Toolkit (aka pakki) for scraping data from [nimenhuuto.com](https://nimenhuuto.com)

Currently only scrapes player attendance/registration data from events.

## Usage

### Environment variables

- `BASE_URL` - your team's nimenhuuto URL (e.g. `https://yourteam.nimenhuuto.com`)
- `NIMENHUUTO_SESSION_ID` - your `_session_id` cookie from browser dev tools after logging in

### Commands

```bash
# Run commands
scala-cli run app -- <cmd> <arguments>

# Show help/available commands
scala-cli run app -- help
```

## Development

Built with [Scala 3](https://scala-lang.org/), [scala-cli](https://scala-cli.virtuslab.org/), and [scala-scraper](https://github.com/ruippeixotog/scala-scraper#browsers)

All required tools are available in nix development shell, activate with `nix develop`.

## TODO

- [x] Discover all events automatically
  - [ ] Support filtering events by dates
- [x] Calculate attendance for every player across multiple events
- [ ] Add parsing tests with HTML files downloaded from browser
- [ ] Check that no one has canceled their registration after the event
- [ ] Error handling
  - When login is not successful
  - When page cannot be read
- [ ] Interactive login?
- [ ] Fancy TUI?
- [ ] Add concurrency, rate limits?
