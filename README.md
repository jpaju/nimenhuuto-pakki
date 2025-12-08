# Nimenhuuto pakki

Toolkit (aka pakki) for scraping data from [nimenhuuto.com](https://nimenhuuto.com)

Currently only scrapes player attendance/registration data from events.

## Usage

Get your `_session_id` cookie from browser dev tools after logging in to nimenhuuto.
The session id is read from `NIMENHUUTO_SESSION_ID` environment variable (needs to be set before running the app)

```bash
scala-cli run app -- "https://<yourteam>.nimenhuuto.com/events/<event-id>"
```

Prints players grouped by registration status (in/out/unknown).

## Development

Built with [Scala 3](https://scala-lang.org/), [scala-cli](https://scala-cli.virtuslab.org/), and [scala-scraper](https://github.com/ruippeixotog/scala-scraper#browsers)

All required tools are available in nix development shell, activate with `nix develop`.

## TODO

- [ ] Discover all events automatically
- [ ] Calculate attendance for every player across multiple events
- [ ] Error handling
  - When login is not successful
  - When page cannot be read
- [ ] Support filtering events by dates
- [ ] Add concurrency, rate limits?
