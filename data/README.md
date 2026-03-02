# Test HTML fixtures

HTML files scraped from nimenhuuto.com for testing `HtmlParser.scala`.

## Sanitizing new HTML files

When copying fresh HTML from the site, sanitize it before committing:

### 1. Replace team name
```bash
sed -i.bak 's/REAL_TEAM_NAME/Example Team/g' data/FILE.html
sed -i.bak 's/realteamname/exampleteam/g' data/FILE.html
rm data/FILE.html.bak
```

### 2. Reduce data size

**For event.html** - keep only 2-3 players per zone (in/out/unknown):
- Find `id="zone_1"`, `id="zone_2"`, `id="zone_3"` sections
- Keep only 2-3 `<span class="player_label">` elements per zone
- Update the count in each zone's `<span>` inside `.info-container`

**For archive.html** - keep only 2 events:
- Find `class="event-detailed-container"` divs
- Keep first 2, delete the rest
- Each event block starts with `<div id="event_XXXXX"` and ends with `</div>\n\n`

**For players.html** - keep only 3 players:
- Find `class="playercard"` divs, keep first 3, delete the rest
- Update sidebar player count (`jäsentä`)

### 3. Replace personal data

In the kept player elements, replace real data with fake:
- Names: Alice Anderson / Alice A, Bob Brown / Bob B, Charlie Clark / Charlie C
- Emails: alice@example.com, bob@example.com, charlie@example.com
- Phone numbers: use dummy Finnish format (+358 40 111 2233)
- Update both display text and any `title`/`alt` attributes

For event/archive pages use short names:
- Alice A, Bob B, Charlie C (for "in")
- Diana D, Eve E (for "out")
- Frank F, Grace G (for "unknown")

### 4. Sanitize page-level metadata

- Replace CSRF token with a dummy value
- Replace GA `user_id` hash with zeroes
- Replace logged-in user display name in the top nav
- Numeric IDs (player IDs, user IDs) can be left as-is

### 5. Verify tests pass
```bash
scala-cli test app -- "*HtmlParserTest*"
```

## Current fixtures

- `event.html` - single event page with 3 in, 2 out, 2 unknown players
- `archive.html` - archive page with 2 events
- `players.html` - player list page with 3 players
