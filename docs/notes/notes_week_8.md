# Meeting Week 8

 Key | Value |
| --- | ---|
| Date: | 02-04-2024 |
| Time: | 13:45 |
| Location: | DW Project Room 4 |
| Chair | Jochem Kralt |
| Minute Taker | Jakub Florek |
| Attendees | Bas Bruijnis, Nguyen Cao Minh, Kiarash Karimi, Rares Patrut |


### Agenda items:
- **Opening by chair (1 min)**
- **Check-in: How is everyone doing? (3 min)**
  - Kiarash started working on the algorithm with Rares. Continued with writing tests, but the pipeline failed. He figured out the problem though, so it's getting better.
  - Rares worked on simplifying debts 
  - Bas hasn't made the merge request with label functionality yet
  - Jakub introduced long polling and adding url to config file
  - Jochem worked on displaying payments
  - Minh worked on the new view design
- **Approval of the agenda - Does anyone have any additions? (1 min)**
  - Kiarash suggested looking through the implemented features feedback
- **Announcements by the TA (1 min)**

- **Talking Points:** (Review+Updates/ Discuss/ Decision Making)
  - **How is everyone doing, What are the impressions after last weeks tasks, Is everyone finished with their work? (5 min)**
    - Eeveryone is doing fine. Some Merge Requests haven't been merged yet, but the functionality is there
  - **Present the current progress to the TA (4 min)**
    - High contrast is yet to be implemented
    - Payments and new debt algorithm works, but web socket refresh hasn't been implemented
    - Mirella suggested the bank button should refer to the creditor bank details
    - Currency hasn't been implemented yet due to the problem with finding a right api (we could potentially add a local method for mock currency exchanges)
    - Add expense sometimes adds participants twice (that's why the associated rubrick entry was not excellent)
  - **Assignments (3 min)**
    - Statistics are insufficient due to many features still missing
    - We should change `Available Debtors` to `Available Participants` in the Settle Debts screen
    - We could implement a drop down menu in Settle Debts view (if we decide to stick with the current implementation)
    - We've reached all basic, live language and detailed expense requirements.
    - Mirella will ask Sebastian if we could just store exchange rates in a config file
    - Bas will keep working on statistics
    - Mail functionality seems stuck. The emails are not connected to the participants. Bas suggested working on it next week. The functionality is there, but needs to be implemented better.
  - **Debtors / Creditors lists in Open Debts (2 min)**
  - **Two more weeks until examination: what do we still want to achieve for our final product? What is realistic? (5 min)**
    - We agreed to focus on tests this week
    - Bas showed almost finished type feature
    - We don't have to implement multiple types per expense
    - Absolute expense value is the total value of the expenses
    - We should consider keyboard navigation
    - We'll check if it's possible to connect to at least 10 clients simultanouslt
    - We should revamp the popups (issue #41)
    - We could link Merge Requests to already finished issues (for extra clarity)
    - Pie chart sometimes appears to be transparent (Bug)
  - **Decide the progress for next week (14 min)**
    - We'll create issues for each page and then anyone can take any screen and start working on the tests
    - Bas will keep working on the types feature
    - Rares will work on keyboard navigation for accessiblity 
    - Kiarash will keep working on the currency

- **Summarize action points: Who , what , when? (2 min)**
  - As in the progress
- **Feedback round: What went well and what can be improved next time? (1 min)**
  - Everyone is doing fine
- **Planned meeting duration != actual duration? Where/why did we mis -estimate? (1 min)**
  - We were just on time
- **Question round: Does anyone have anything to add before the meeting closes? (1 min)**
  - We could book a room in the library for practicing the presentation
- **Closure (1 min)**

## Meeting continuation in Flux
- We have a room booked for product pitch rehersal 13-14 and 17-19 on Friday 12.04
- Additionally we've further split the tasks accordingly:
  - Jochem will add tests to 3 views
  - Jakub will add tests to the `EventOverviewController` and fix several bugs
  - Minh will add tests to the `AddEditParticipantController` and keep working on accessibility
