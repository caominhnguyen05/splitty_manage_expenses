# Notes Meeting Week 9

 Key | Value |
| --- | --- |
| Date: | 09-04-2024 |
| Time: | 13:45 |
| Location: | DW Project Room 4 |
| Chair | Nguyen Cao Minh |
| Minute Taker | Bas Bruijnis|
| Attendees | Jochem Kralt, Jakub Florek, Raresh Patrut, Kiarash Karimi |


### Agenda items:
- Opening by chair (1 min)

This is the last meeting, and the last week to write code: Code-freeze on Sunday, product pitch on Tuesday.

- Check-in: How is everyone doing? (3 min)

Last week, a lot of tests were made (41% coverage), some features were added and bugs resolved. Everybody is busy with preparing for resits and endterms. We currently have an failing build on the main branch, related to testing and a memory-cap of JUnit.

- Approval of the agenda - Does anyone have any additions? (1 min)

Testing. Fix the main-pipeline. Also don't forget to talk about code, apart from assignments, since of course we all need to pass the course requirements. (7x weekly requirements)


- Announcements by the TA

No announcements.

- **Talking Points:** (Inform/ brainstorm/ decision making/ discuss)
  - Assign and resolve/close open Issues we have (5 min)

We closed issues which were implemented last week (issue count went from 21 to 13 still open)

  - See current test coverage for the project and see if we should write more tests. (5 min)

Some test-related issue items still have to be worked on (not all views are tested yet). Also right now we have the issue that the tests fail on the main branch. Rares also ran into the issue where the tests would pass when running without coverage but fail when the tests were ran with coverage.

We are at 41% coverage now, 80% is unrealistic, so we will just test every view and not neccsarily aim for 80% (only at this percentage we get extra points).

#### Important: everybody needs to have 7 merge requests: check your amount!

#### To reach code contributions this week: fix current open issues, refactor/polish the app (this also might help the coverage) and write tests.

  - Discuss (summative) assignments that we should improve on: (25 min)
     - Code of Conduct: Refine and reduce number of words. 

    No impact on grade, we will not bother with it any further.

     - HCI/Accessibility assignment

    No 'undo' operation implemented, this might also be pretty hard and could be unexpectedly big and break other parts of the app. We decided not to implement this.

    Bas will double check if the eneter-keypress on startsceen still works

     - Check Implemented Features assignment and see which feature we still need to work on.

    Minh will double check if the language template file download still works.
    
    Sending a default email to check if user's credentials are correct is not implemented, and also won't be this week.

     - Tasks and Planning assignment: ensure that every issue is included in a MR.

    We are doing good on this assignment.

     - Project Pitch: prepare anything necessary for Friday pratice presentation.

    Bas will add the part about the views he created.

    This week we should collaborate about how we will work on the product pitch.

    We could give the pitch in a hybrid matter: both images of the app as well as a live demo.

    #### Reminder: Friday we have a room booked in the library to work further on this: We have a room booked from 13-14 and 17-19 on Friday 12.04 (upcoming friday)

    Everybody should read the whole document by then and read through the code and make sure they understand everything and can ask questions in time.

    During the pitch we should but an **Emphasis** on the design and the feeling we were trying to create with it (professional and still friendly) 
    
    Jakub mentioned we could end the pitch on a funny note, showing that we own the splitty.nl domain.

    TA mentioned extra requirements

    ## IMPORTANT: Saturday evening we have decided to make our own deadline(!) to make sure the app doesn't break and we have a stable version

- Summarize action points: Who , what , when? (2 min)

## This week we need to make the final buddycheck assignment and self-reflection. We will prepare the product pitch. Polish the app and make a stable version everybody is happy with. Make some more tests and resolve some issues still left.

- Feedback round: What went well and what can be improved next time? (1 min)
- Planned meeting duration != actual duration? Where/why did you mis -estimate? (1 min)

We were done a bit early, this is because everybody was already pretty clear about what has been done, what is currently being done and what still needs to be done. We have already done a lot, the product is functional and some have already reached all their weekly requirements

- Question round: Does anyone have anything to add before the meeting closes? (1 min)

After the meeting we immediately went to try to fix the build pipeline fail of main. As a (hopefully just temporary) fix for now we commented out the tests that were failing.

- Closure (1 min)
