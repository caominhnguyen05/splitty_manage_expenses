### Notes for week 6 meeting

Key | Value |
| --- | --- |
| Date: | 19-03-2024 |
| Time: | 13:45 |
| Location: | DW Project Room 4 |
| Chair | Kiarash Karimi |
| Minute Taker | Rares Patrut |
| Attendees | Jochem Kralt, Jakub Florek, Bas Bruijnis, Nguyen Cao Minh |


### Agenda items:
- Opening by chair (1 min)
- Check-in: How is everyone doing? (3 min)
- Approval of the agenda - Does anyone have any additions? (1 min)

- Announcements by the TA 

- **Talking Points:** (Inform/ brainstorm/ decision making/ discuss)
  - **What are the impressions after last weeks tasks? Is everyone finished with their work? (3 min)**

    _Everyone finished their tasks from week 4-5. The admin part is mostly done and the  web-socket connection too. The basic Debt functionallity is done, but some refactorings and additions are needed. The email functionality is now also working._

  - Present the current progress to the TA (3 min)
  - **Ask for clarity about splitting expenses (the question we asked before midterm)** (2 min)

    _The total amount of payments within a group should be N-1, as we establish during a previous meeting._
    
    _Some other important stuff highlighted by the TA:_
    
    _The server's URL should be able to be changed in the config file._

    _A user should have the ability to add a new language -> we have to create a template for the user to fill every translation in_


  - **Discussing how to handle the calculation parts related to payments and debts (8 min)**

    _Until now, the amount is split, so the basic calculations are done. This week, we decided to focus on the basic requirements, so we are finished with everything by the end of the week._

  - Decide the progress for next week (15 min)  
    **- Cleaning up the template project files**

    **- Removing mock parts of code introduced in week 2 due to the lack of data models**

      _Both actions will be done this week, because from now on there will be no need for those files anymore_
    
    **- Split the tasks regarding the basic features (Jakub's document)** 

    _The following tasks will be done this week:_

    _BAS – admin part_

    _KIARASH – show for every participant all expenses where he/she is a creditor OR a debtor, when is his/her name searched in the DebtsOverview page_ + block adding expense when there is no participant_

    _MINH – edit + remove participant + language handling_

    _JAKUB – websockets_

    _JOCHEM – implement the feature that enables deleting a participant if and only if his balance is 0_
    
    _RARES – refactoring +  issues solving_



- Summarize action points: Who , what , when? (2 min)
- Feedback round: What went well and what can be improved next time? (1 min)
- **Planned meeting duration != actual duration? Where/why did you mis -estimate? (1 min)**

    _The planning was mostly accurate, but there was need for a further meeting after this one, in order to establish future aspects and clarify some things._

- Question round: Does anyone have anything to add before the meeting closes? (1 min)
- Closure (1 min)

_**!! Issues for every task for this week have been created, so everyone can include them in theirs MRs.**_
